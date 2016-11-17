package agents;

import java.io.IOException;

import core.AbstractEquation;
import core.Equation;
import core.MultiplicativeEquation;
import core.SummativeEquation;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class AgentSummativeEquation extends Agent {

	private static final long serialVersionUID = 8040609610985189379L;

	private Equation derivate(SummativeEquation se){
		AbstractEquation u = derivate(se.getFirst());
		AbstractEquation v = derivate(se.getSecond());
		return new SummativeEquation(u,v);
	}	

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
		System.out.println(this.getClass().getSimpleName() + ": a été démarré.");
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("SummativeEquation");
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
					ACLMessage msg=receive();					
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
