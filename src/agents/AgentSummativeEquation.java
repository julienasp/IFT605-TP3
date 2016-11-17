package agents;

import java.io.IOException;

import core.AbstractEquation;
import core.Equation;
import core.SummativeEquation;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class AgentSummativeEquation extends Agent {

	private static final long serialVersionUID = 8040609610985189379L;

	protected Equation derivate(SummativeEquation se){
		AbstractEquation u = se.getFirst();
		AbstractEquation v = se.getSecond();
		AbstractEquation uPrime = requestDerivation(u);
		AbstractEquation vPrime = requestDerivation(v);
		return new SummativeEquation(uPrime,vPrime);
	}

	AID getService(String service){
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType( service );
		dfd.addServices(sd);
		try
		{
			DFAgentDescription[] result = DFService.search(this, dfd);
			if (result.length>0)
				return result[0].getName() ;
		}
		catch (FIPAException fe) { fe.printStackTrace(); }
		return null;
	}

	AbstractEquation requestDerivation(AbstractEquation eq){

		// Envoi
		System.out.print("Requete de dérivation pour l'équation "+eq.getClass().getSimpleName()+" : ");
		eq.printUserReadable();
		if(eq instanceof SummativeEquation){ // probleme de receive quand j'envoi le msg a l'agent courant
			return (AbstractEquation) derivate((SummativeEquation) eq);
		}
		AID agent = getService(eq.getClass().getSimpleName());
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		try {
			System.out.println("envoi");
			msg.setContentObject(eq);
			msg.addReceiver(agent);
			send(msg);
		} catch (IOException e) {e.printStackTrace();}
		System.out.println("envoyé");
		// Reception
		MessageTemplate type = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		System.out.println("j'attends");
		ACLMessage rMsg = blockingReceive(type);
		try {
			AbstractEquation rEq = (AbstractEquation) rMsg.getContentObject();
			return rEq;
		} catch (UnreadableException e) {e.printStackTrace();}
		return null;
	}

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
		System.out.println(this.getClass().getSimpleName() + ": a été démarré.");

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(SummativeEquation.class.getSimpleName());
		sd.setName("SummativeEquationAgent");
		dfd.addServices(sd);		
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		addBehaviour(new CyclicBehaviour() {			
			private static final long serialVersionUID = 8464649767606932209L;

			@Override
			public void action() {
				try {
					System.out.println(this.getClass().getSimpleName() + ": CyclicBehaviour a été démarré.");
					MessageTemplate type = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
					ACLMessage msg=receive(type);
					System.out.println("recu ");
					if(msg!=null && msg.getContentObject() instanceof SummativeEquation){
						SummativeEquation se = (SummativeEquation) msg.getContentObject();												
						System.out.println(this.getClass().getSimpleName() + ":Réception d'une SummativeEquation à dériver.");
						se.printUserReadable();

						//Formulation de la réponse
						ACLMessage message = new ACLMessage(ACLMessage.INFORM);
						message.addReceiver(msg.getSender());
						message.setContentObject(derivate(se));
						send(message);
					}
					else{
						block();
					}
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
		});
	}
	@Override
	protected void takeDown() {
		// Deregister from the yellow pages
		try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}
}
