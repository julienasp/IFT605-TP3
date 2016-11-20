package operations;

import core.BasicEquation;
import core.Equation;

public class DivideExponentByCoefficient implements IBasicOperations {
	public Equation apply(Equation e) {
		BasicEquation be = (BasicEquation) e;
		return new BasicEquation(be.getCoefficient() , (int) (be.getExponent()/be.getCoefficient()));        
	}
}
