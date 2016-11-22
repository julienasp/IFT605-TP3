package agents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import operations.IBasicOperation;
import operations.OperationHandler;

import core.BasicEquation;
import core.Constant;
import core.Equation;
import core.EquationsProvider;
import core.MultiplicativeEquation;
import core.SummativeEquation;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class ClientAgent extends Agent{

	private static final long serialVersionUID = 5456872404952223059L;

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
		addBehaviour(new OneShotBehaviour(){
			//STATIC TEST #1
			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				SummativeEquation s = new SummativeEquation(new BasicEquation(3,2),new Constant(8));
				BasicEquation d = new BasicEquation(6,1);
				ArrayList<Equation> l = new ArrayList<Equation>();
				l.add(0, s); // source
				l.add(1, d);// derivated random
				
				AID destination = getService("VieuxSage");
				try {
					ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
					msg.addReceiver(destination);
					msg.setContentObject(l);
					send(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
				ACLMessage msg = blockingReceive(template);
				if(msg != null){
					try {
						System.out.println("la dérivation est: " + msg.getContentObject().toString());						
					} catch (UnreadableException e) {
						e.printStackTrace();
					}
					
				}
				
			}
			
		});
		addBehaviour(new OneShotBehaviour(){
			//STATIC TEST #2
			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				SummativeEquation s = new SummativeEquation(new BasicEquation(5,3),new SummativeEquation(new BasicEquation(3,2),new BasicEquation(2,1)));
				SummativeEquation s2 = new SummativeEquation(new BasicEquation(15,2),new SummativeEquation(new BasicEquation(6,1),new Constant(2)));

				ArrayList<Equation> l = new ArrayList<Equation>();
				l.add(0, s); // source
				l.add(1, s2);// derivated random
				
				AID destination = getService("VieuxSage");
				try {
					ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
					msg.addReceiver(destination);
					msg.setContentObject(l);
					send(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
				ACLMessage msg = blockingReceive(template);
				if(msg != null){
					try {
						System.out.println("la dérivation est: " + msg.getContentObject().toString());						
					} catch (UnreadableException e) {
						e.printStackTrace();
					}
					
				}
				
			}
			
		});

	addBehaviour(new OneShotBehaviour(){
		//STATIC TEST #3 WRONG
		private static final long serialVersionUID = 1L;
	
		@Override
		public void action() {
			SummativeEquation s = new SummativeEquation(new BasicEquation(5,3),new SummativeEquation(new BasicEquation(3,2),new BasicEquation(2,1)));
			SummativeEquation s2 = new SummativeEquation(new BasicEquation(13,2),new SummativeEquation(new BasicEquation(6,2),new Constant(2)));
	
			ArrayList<Equation> l = new ArrayList<Equation>();
			l.add(0, s); // source
			l.add(1, s2);// derivated random
			
			AID destination = getService("VieuxSage");
			try {
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.addReceiver(destination);
				msg.setContentObject(l);
				send(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			ACLMessage msg = blockingReceive(template);
			if(msg != null){
				try {
					System.out.println("la dérivation est: " + msg.getContentObject().toString());						
				} catch (UnreadableException e) {
					e.printStackTrace();
				}
				
			}
			
		}
		
	});

		addBehaviour(new OneShotBehaviour(){
			//STATIC TEST #4 ULTRA-WRONG
			private static final long serialVersionUID = 1L;
		
			@Override
			public void action() {
				SummativeEquation s = new SummativeEquation(new BasicEquation(5,3),new SummativeEquation(new BasicEquation(3,2),new BasicEquation(2,1)));
				SummativeEquation s2 = new SummativeEquation(new BasicEquation(-13,2),new SummativeEquation(new BasicEquation(6,2),new Constant(2)));
		
				ArrayList<Equation> l = new ArrayList<Equation>();
				l.add(0, s); // source
				l.add(1, s2);// derivated random
				
				AID destination = getService("VieuxSage");
				try {
					ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
					msg.addReceiver(destination);
					msg.setContentObject(l);
					send(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
				ACLMessage msg = blockingReceive(template);
				if(msg != null){
					try {
						System.out.println("la dérivation est: " + msg.getContentObject().toString());						
					} catch (UnreadableException e) {
						e.printStackTrace();
					}
					
				}
				
			}
			
		});

	addBehaviour(new OneShotBehaviour(){
	
		private static final long serialVersionUID = -3068556774651966589L;
	
		private Vector<Equation> equations;
		private OperationHandler oh;
		private List<IBasicOperation>favCombinaison;
	
		@Override
		public void onStart() {
			EquationsProvider ep = new EquationsProvider();
			equations = ep.getList();
			oh = new OperationHandler();
		}
	
		@Override
		public void action() {
			for(Equation sEquation : equations){
				System.out.println(" ");
				System.out.println("------------------------------------------------");
				System.out.println("We retreive the current equation.");			
				System.out.println("The current equation is:");
				sEquation.printUserReadable();
				System.out.println("Searching for service :Vieux Sage");
				
				AID destination = getService("VieuxSage");
				
				if(destination == null){
					System.out.println("No agent handles type : ");
					return;
				}
				
				int i = 0;
				for(List<IBasicOperation> bo : oh.getCombinationOperationList()){			
					try {
						List<IBasicOperation> basicOperationUsed;
						ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
						msg.addReceiver(destination);
						ArrayList<Equation> equationList = new ArrayList<Equation>();
						equationList.add(sEquation);
						if(favCombinaison != null && i == 0){
							System.out.println("Test avec la combinaison favorite: " + favCombinaison.toString());
							equationList.add(oh.getModifiedEquation(sEquation, favCombinaison));
							basicOperationUsed = favCombinaison;
						}
						else{
							System.out.println("Test avec la combinaison courante: " + bo.toString());
							equationList.add(oh.getModifiedEquation(sEquation, bo));							
							basicOperationUsed = bo;
						}
						//Hardcoded Operation basique pour MultiplicativeEquation 
						if(sEquation instanceof MultiplicativeEquation){
							equationList.set(1, oh.SumOfMultiplyEquationOneWithEquationTwoPrimeAndMultiplyEquationOnePrimeWithEquationTwo((MultiplicativeEquation)sEquation,(MultiplicativeEquation)equationList.get(1)));
						}
						
						msg.setContentObject(equationList);
						send(msg);
						
						
						MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
						ACLMessage msgrcv = blockingReceive(template);
						if(msgrcv != null){					
							if((Boolean) msgrcv.getContentObject()){
								favCombinaison = basicOperationUsed;
								System.out.println(" ");
								System.out.println("la dérivation pour:");	
								equationList.get(0).printUserReadable();
								System.out.println("À été trouver avec la combinaison suivante: " + basicOperationUsed.toString() );
								
								//Hardcoded pour MultiplicativeEquation
								if(sEquation instanceof MultiplicativeEquation){
									System.out.println("En combinaison avec l'opération suivante: [AddMultiplyOfEquationOneWithEquationTwoPrimeWithMultiplyOfEquationOnePrimeWithEquationTwo]");
								}
								
								System.out.println("le resultat trouvé pour la dérivation est:");
								equationList.get(1).printUserReadable();
								break;
							}		
						}//end if				
					} catch (IOException e) {
						e.printStackTrace();
					} catch (UnreadableException e) {
						e.printStackTrace();
					}				
					i++;
				}//end for
			}		
		}//end action
	}); // fin behaviour
	} // fin setup
}
