package agents;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import core.Equation;
import core.EquationsProvider;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
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
				/*else{
					block();
				}*/
			}



		});
	}

}
