package containers;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

public class ConstantContainer {
	public static void main(String[] args) {
		try {
			Runtime r = Runtime.instance();
			ProfileImpl pI = new ProfileImpl(false);
			pI.setParameter(ProfileImpl.MAIN_HOST, "Localhost");
			AgentContainer aC = r.createAgentContainer(pI);
			AgentController agentController = aC.createNewAgent("ConstantAgent", "agents.Constant", new Object[] {});
			agentController.start();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
