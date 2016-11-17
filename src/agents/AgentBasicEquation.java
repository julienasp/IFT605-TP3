package agents;

import jade.core.Agent;

public class AgentBasicEquation extends Agent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8564986329709013737L;

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
		System.out.println(this.getClass().getSimpleName() + ": a été démarré.");
	}

}
