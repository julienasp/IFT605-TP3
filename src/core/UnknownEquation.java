/**
 * udes.ds.rmi
 * BasicEquation.java
 * 3 sept. 08
 */
package core;


/**
 * Stores an equation of the type kx^n
 * 
 * @author      Luc Bergevin
 * @version     1.0
 * @see          
 */
public class UnknownEquation extends AbstractEquation {
	
	// De la forme f(x) = _coefficient^_exponent + _constant
	private static final long	 serialVersionUID	= 1L;
	private double _coefficient;
	private int _exponent;
	private int _constant;
	
	public UnknownEquation(double coefficient, int exponent, int constant) {
		super();
		_coefficient = coefficient;
		_exponent = exponent;
		_constant = constant;
	}

	public double getCoefficient() {
		return _coefficient;
	}

	public int getExponent() {
		return _exponent;
	}

	public int get_constant() {
		return _constant;
	}

	/**  
	 * @see udes.ds.rmi.hw.Equation#getFunctionValue(double)      
	 */
	public double getFunctionValue(double x) {
		return ((Math.pow(x,_exponent))*_coefficient + _constant);
	}

	/**  
	 * @see udes.ds.rmi.hw.AbstractEquation#getUserReadableString()      
	 */
	protected String getUserReadableString() {
		return new String(Double.toString(_coefficient) + "x^" + Integer.toString(_exponent) + " + " + Integer.toString(_constant));
	}

}
