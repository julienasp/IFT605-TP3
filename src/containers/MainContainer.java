package containers;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;
public class MainContainer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Runtime r = Runtime.instance();
			Properties p = new ExtendedProperties();
			p.setProperty(Profile.GUI,"true");
			ProfileImpl pI = new ProfileImpl(p);
			AgentContainer mC = r.createMainContainer(pI);
			mC.start();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
