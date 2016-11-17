package agents;

import java.io.IOException;

import core.BasicEquation;
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

public class AgentBasicEquation extends Agent{
	private static final long serialVersionUID = -8564986329709013737L;
	
	private Equation derivate(BasicEquation be){
		if(be.getExponent() == 1){
			return new Constant(be.getCoefficient());
		}
		else{
			return new BasicEquation(be.getCoefficient()*be.getExponent(),be.getExponent()-1);
		}		
	}

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
		System.out.println(this.getClass().getSimpleName() + ": a été démarré.");
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("BasicEquationHandling");
		sd.setName("BasicEquationAgent");
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
					if(msg!=null && msg.getContentObject() instanceof BasicEquation){
						BasicEquation be = (BasicEquation) msg.getContentObject();												
						System.out.println(this.getClass().getSimpleName() + ":Réception d'un BasicEquation à dériver.");
						be.printUserReadable();
						
						//Formulation de la réponse
						ACLMessage message = new ACLMessage(ACLMessage.INFORM);
						message.addReceiver(msg.getSender());
						message.setContentObject(derivate(be));
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

}
