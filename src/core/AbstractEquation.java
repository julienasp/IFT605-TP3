/**
 * udes.ds.agent.hw
 * AbstractEquation.java
 * 7 oct. 09
 */
package core;


/**
 * TODO add type comments
 * 
 * @author      Luc Bergevin
 * @version     1.0
 * @see          
 */
public abstract class AbstractEquation implements Equation {

	private static final long serialVersionUID = -2350741610141063310L;

	/**
	 * Returns a user-readable form of the equation
	 *
	 * @return      String          
	 */
	abstract protected String getUserReadableString();

	/**   
	 * @see udes.ds.rmi.hw.Equation#printUserReadable()      
	 */
	public void printUserReadable() {
		System.out.println("y = " + this.getUserReadableString());
	}

}
