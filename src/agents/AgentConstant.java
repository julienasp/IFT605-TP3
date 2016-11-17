package agents;

import java.io.IOException;

import core.Constant;
import core.Equation;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class AgentConstant extends Agent{
	private static final long serialVersionUID = -8564986329709013737L;
	
	protected static Equation derivate(Constant c){
		return new Constant(0);		
	}

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
		System.out.println(this.getClass().getSimpleName() + ": a été démarré.");
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Constant.class.getSimpleName());
		sd.setName("ConstantAgent");
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
					if(msg!=null && msg.getContentObject() instanceof Constant){
						Constant c = (Constant) msg.getContentObject();												
						System.out.println(this.getClass().getSimpleName() + ":Réception d'une Constante à dériver.");
						c.printUserReadable();
						
						//Formulation de la réponse
						ACLMessage message = new ACLMessage(ACLMessage.INFORM);
						message.addReceiver(msg.getSender());
						message.setContentObject(derivate(c));
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
