package agents;

import java.io.IOException;

import core.AbstractEquation;
import core.Equation;
import core.MultiplicativeEquation;
import core.SummativeEquation;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class AgentMultiplicativeEquation extends Agent{
	private static final String DERIVATION_REQUEST = "derivation-request";
	private static final String DERIVATION_ANSWER = "derivation-answer";
	private static final String SENDER = "derivation-sender";
	private static final long serialVersionUID = -4229267745481551474L;	



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

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
		System.out.println(this.getClass().getSimpleName() + ": a été démarré.");

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(MultiplicativeEquation.class.getSimpleName());
		sd.setName("MultiplicativeEquationAgent");
		dfd.addServices(sd);		
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		// ------------- FSM
		FSMBehaviour fsm = new FSMBehaviour(this);
		// ------------- Phase 1 - Reception
		OneShotBehaviour reception = new OneShotBehaviour(this) {
			private static final long serialVersionUID = -3563888301751231452L;
			private int received = 0;
			@Override
			public void action() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				MessageTemplate type = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
				System.out.println("receiving...");
				ACLMessage msg=receive(type);					
				if(msg!=null){
					try {
						if(msg.getContentObject() instanceof MultiplicativeEquation){
							MultiplicativeEquation me = (MultiplicativeEquation) msg.getContentObject();												
							System.out.println(this.getClass().getSimpleName() + ":Réception d'une MultiplicativeEquation à dériver.");
							me.printUserReadable();
							fsm.getDataStore().put(DERIVATION_REQUEST, me);
							fsm.getDataStore().put(SENDER, msg.getSender());
							System.out.println("Reception datastore size : "+fsm.getDataStore().size());
							received = 1;
						}
					} catch (UnreadableException e) {
						e.printStackTrace();
					}
				}
			}
			@Override
			public void reset() {
				super.reset();
				received = 0;
			}
			@Override
			public int onEnd() {
				return received;
			}
		};

		// ------------- Phase 2 - Derivation (u' et v')
		OneShotBehaviour derivation = new OneShotBehaviour(this) {

			private static final long serialVersionUID = -6494249068795271606L;

			protected Equation derivate(MultiplicativeEquation me){
				AbstractEquation u = me.getFirst();
				AbstractEquation v = me.getSecond();
				AbstractEquation uPrime = requestDerivation(u);
				AbstractEquation vPrime = requestDerivation(v);
				return new SummativeEquation(new MultiplicativeEquation(uPrime,me.getSecond()),new MultiplicativeEquation(me.getFirst(),vPrime));
			}

			AbstractEquation requestDerivation(AbstractEquation eq){
				// Envoi
				System.out.print("Requete de dérivation pour l'équation "+eq.getClass().getSimpleName()+" : ");
				eq.printUserReadable();
				if(eq instanceof MultiplicativeEquation){ // probleme de receive quand j'envoi le msg a l'agent courant
					return (AbstractEquation) derivate((MultiplicativeEquation) eq);
				}
				AID agent = getService(eq.getClass().getSimpleName());
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				try {
					msg.setContentObject(eq);
					msg.addReceiver(agent);
					send(msg);
				} catch (IOException e) {e.printStackTrace();}

				// Reception
				MessageTemplate type = MessageTemplate.and(
						MessageTemplate.MatchPerformative(ACLMessage.INFORM), 
						MessageTemplate.MatchSender(agent));
				
				ACLMessage rMsg = blockingReceive(type);
				try {
					AbstractEquation rEq = (AbstractEquation) rMsg.getContentObject();
					return rEq;
				} catch (UnreadableException e) {e.printStackTrace();}

				return null;

			}

			@Override
			public void action() {
				System.out.println("Phase 2 datastore size : "+fsm.getDataStore().size());
				MultiplicativeEquation me = (MultiplicativeEquation) fsm.getDataStore().get(DERIVATION_REQUEST);
				
				Equation e = derivate(me);

				fsm.getDataStore().put(DERIVATION_ANSWER, e);
			}
		};

		// ------------- Phase 3 - Renvoi de la reponse
		OneShotBehaviour renvoi = new OneShotBehaviour(this) {

			private static final long serialVersionUID = -6168462529609417775L;

			@Override
			public void action() {
				// Reception
				Equation eq = (Equation) fsm.getDataStore().get(DERIVATION_ANSWER);
				AID sender = (AID) fsm.getDataStore().get(SENDER);
				//Formulation de la réponse
				ACLMessage message = new ACLMessage(ACLMessage.INFORM);
				message.addReceiver(sender);
				try {
					message.setContentObject(eq);
				} catch (IOException e) {
					e.printStackTrace();
				}
				send(message);
				System.out.println("Renvoi de la réponse");
			}

		};
		
		fsm.registerFirstState(reception, "Reception");
		fsm.registerState(derivation, "Derivation");
		fsm.registerState(renvoi, "Renvoi");
		
		fsm.registerTransition("Reception", "Derivation",1);
		fsm.registerTransition("Reception", "Reception",0);
		fsm.registerDefaultTransition("Derivation", "Renvoi");
		fsm.registerDefaultTransition("Renvoi", "Reception", new String[]{"Reception","Derivation","Renvoi"});
		addBehaviour(fsm);
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

