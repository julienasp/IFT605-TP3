package agents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import core.BasicEquation;
import core.Constant;
import core.Equation;
import core.EquationsProvider;
import core.SummativeEquation;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
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
		/*
		addBehaviour(new CyclicBehaviour(){

			private static final long serialVersionUID = -3068556774651966589L;

			private Vector<Equation> equations; 
			private Iterator<Equation> it;

			@Override
			public void onStart() {
				EquationsProvider ep = new EquationsProvider();
				equations = ep.getList();
				it = equations.iterator();
			}

			@Override
			public void action() {
				Equation sEquation = null;
				if(it.hasNext()){
					sEquation = it.next();
				}
				System.out.println("Searching for service :"+sEquation.getClass().getSimpleName());
				AID destination = getService(sEquation.getClass().getSimpleName());
				if(destination == null){
					System.out.println("No agent handles this kind of Equation : "+sEquation.getClass().getSimpleName());
					return;
				}
				
				try {
					ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
					msg.addReceiver(destination);
					msg.setContentObject(sEquation);
					send(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
				ACLMessage msg = blockingReceive(template);
				if(msg != null){
					try {
						Equation rEquation = (Equation) msg.getContentObject();
						rEquation.printUserReadable();
					} catch (UnreadableException e) {
						e.printStackTrace();
					}
					
				}
				//else{
				//	block();
				//}
			}



		});*/
	}

}
