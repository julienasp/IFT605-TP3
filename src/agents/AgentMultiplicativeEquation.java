package agents;

import java.io.IOException;

import core.AbstractEquation;
import core.BasicEquation;
import core.Constant;
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

public class AgentMultiplicativeEquation extends Agent{
	private static final long serialVersionUID = -4229267745481551474L;	
	
	private Equation derivate(MultiplicativeEquation me){
		AbstractEquation uPrime = derivate(me.getFirst());
		AbstractEquation vPrime = derivate(me.getSecond());
		return new SummativeEquation(new MultiplicativeEquation(uPrime,me.getSecond()),new MultiplicativeEquation(me.getFirst(),vPrime));
	}	

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
		System.out.println(this.getClass().getSimpleName() + ": a été démarré.");
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("MultiplicativeEquation");
		sd.setName("MultiplicativeEquationAgent");
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
					if(msg!=null && msg.getContentObject() instanceof MultiplicativeEquation){
						MultiplicativeEquation me = (MultiplicativeEquation) msg.getContentObject();												
						System.out.println(this.getClass().getSimpleName() + ":Réception d'une MultiplicativeEquation à dériver.");
						me.printUserReadable();
						
						//Formulation de la réponse
						ACLMessage message = new ACLMessage(ACLMessage.INFORM);
						message.addReceiver(msg.getSender());
						message.setContentObject(derivate(me));
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
