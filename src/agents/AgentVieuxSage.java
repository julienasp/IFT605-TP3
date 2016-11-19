package agents;

import java.io.IOException;
import core.BasicEquation;
import core.Equation;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class AgentVieuxSage extends Agent{
	private static final long serialVersionUID = -8564986329709013737L;

	private boolean isAcceptableDerivation(Equation sourceEquation, Equation derivedEquation, Double acceptableErrorRate){
		//Part one - Slope from two early given points on f(x) and f'(x) where x=1 and x=2		
		Double slopePartOne = sourceEquation.getFunctionValue(2) - sourceEquation.getFunctionValue(1);		
		Double averageDerivationPartOne = (derivedEquation.getFunctionValue(1) + derivedEquation.getFunctionValue(2)) / 2 ;
		Double resultPartOne = Math.abs( ( slopePartOne - averageDerivationPartOne ) / slopePartOne) * 100;
		
		//Part two - Slope from two early given points on f(x) and f'(x) where x=11 and x=12	
		Double slopePartTwo = sourceEquation.getFunctionValue(12) - sourceEquation.getFunctionValue(11);		
		Double averageDerivationPartTwo = (derivedEquation.getFunctionValue(11) + derivedEquation.getFunctionValue(12)) / 2 ;
		Double resultPartTwo = Math.abs( ( slopePartTwo - averageDerivationPartTwo ) / slopePartTwo) * 100;
		
		//Part three - Slope from two early given points on f(x) and f'(x) where x=21 and x=22	
		Double slopePartThree = sourceEquation.getFunctionValue(22) - sourceEquation.getFunctionValue(21);		
		Double averageDerivationPartThree = (derivedEquation.getFunctionValue(22) + derivedEquation.getFunctionValue(21)) / 2 ;
		Double resultPartThree = Math.abs( ( slopePartThree - averageDerivationPartThree ) / slopePartThree) * 100;
		
		return ( (resultPartOne + resultPartTwo + resultPartThree)/3 <= acceptableErrorRate);
	}

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
		System.out.println(this.getClass().getSimpleName() + ": a �t� d�marr�.");

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(BasicEquation.class.getSimpleName());
		sd.setName("VieuxSageAgent");
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
					System.out.println(this.getClass().getSimpleName() + ": CyclicBehaviour a �t� d�marr�.");
					ACLMessage msg=receive();					
					if(msg!=null && msg.getContentObject() instanceof BasicEquation){
						BasicEquation be = (BasicEquation) msg.getContentObject();												
						System.out.println(this.getClass().getSimpleName() + ":R�ception d'une BasicEquation � d�river.");
						be.printUserReadable();

						//Formulation de la r�ponse
						ACLMessage message = new ACLMessage(ACLMessage.INFORM);
						message.addReceiver(msg.getSender());
						//message.setContentObject(isAcceptableDerivation(....));
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
