package agents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

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

	private boolean isAcceptableDerivation(Equation sourceEquation, Equation derivedEquation, Double acceptableErrorRate, int nbIterationTest){
		Random randomGenerator = new Random();
		Double averageResult = 0.0;		
		boolean bResult = true;
		
		//Heuristic calculations - Slope from two early given points on f(x) and f'(x) on random x's
		for(int i=0; i < nbIterationTest; i++){
			int randomX = randomGenerator.nextInt(1500);
			Double slope = sourceEquation.getFunctionValue(randomX+1) - sourceEquation.getFunctionValue(randomX);		
			Double averageDerivation = (derivedEquation.getFunctionValue(randomX+1) + derivedEquation.getFunctionValue(randomX)) / 2 ;
			Double result = Math.abs( ( slope- averageDerivation ) / slope) * 100;
			
			System.out.println(this.getClass().getSimpleName() +":-isAcceptableDerivation(): iteration #" + i + " with x=" + randomX + " slope:" + slope.toString());
			System.out.println(this.getClass().getSimpleName() +":-isAcceptableDerivation(): iteration #" + i + " with x=" + randomX + " average: " + averageDerivation.toString());
			System.out.println(this.getClass().getSimpleName() +":-isAcceptableDerivation(): iteration #" + i + " with x=" + randomX + " result: "+ result.toString());
		
			averageResult = averageResult + result;
			if(result > acceptableErrorRate) bResult=false;
		}
		
		averageResult = averageResult/nbIterationTest;
		System.out.println(this.getClass().getSimpleName() +":-isAcceptableDerivation(): averageResult: " + averageResult.toString());
		return bResult; // false if at least one of the random x's tested exceeds the acceptableErrorRate
	}

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
		System.out.println(this.getClass().getSimpleName() + ": a été démarré.");

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("VieuxSage");
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
					System.out.println(this.getClass().getSimpleName() + ": CyclicBehaviour a été démarré.");
					ACLMessage msg=receive();					
					if(msg!=null && msg.getContentObject() instanceof ArrayList<?>){
						@SuppressWarnings("unchecked")
						ArrayList<Equation> equationList = (ArrayList<Equation>) msg.getContentObject();												
						System.out.println(this.getClass().getSimpleName() + ":Réception d'une List d'équation à valider.");
						if(equationList.size() == 2){
							for (Equation e : equationList){
								e.printUserReadable();
							}

							//Formulation de la réponse
							ACLMessage message = new ACLMessage(ACLMessage.INFORM);
							message.addReceiver(msg.getSender());
							message.setContentObject( isAcceptableDerivation((Equation)equationList.get(0),(Equation)equationList.get(1), (double) 5.5, 15 ));
							send(message);
						}
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
