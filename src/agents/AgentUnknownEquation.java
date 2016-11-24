package agents;

import java.io.IOException;

import core.Constant;
import core.Equation;
import core.UnknownEquation;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class AgentUnknownEquation extends Agent{
	private static final long serialVersionUID = -8564986329709013737L;

	@SuppressWarnings("static-method")
	protected Equation derivate(UnknownEquation ue){
		if(ue.getExponent() == 1){
			return new Constant(ue.getCoefficient());
		}
		else{
			return new UnknownEquation(ue.getCoefficient()*ue.getExponent(),ue.getExponent()-1,0);
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
		sd.setType(UnknownEquation.class.getSimpleName());
		sd.setName("UnknownEquationAgent");
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
					if(msg!=null && msg.getContentObject() instanceof UnknownEquation){
						UnknownEquation be = (UnknownEquation) msg.getContentObject();												
						System.out.println(this.getClass().getSimpleName() + ":Réception d'une UnknownEquation à dériver.");
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
