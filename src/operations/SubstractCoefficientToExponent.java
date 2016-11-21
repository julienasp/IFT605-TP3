package operations;

import core.BasicEquation;
import core.Equation;

public class SubstractCoefficientToExponent implements IBasicOperation {
	public Equation apply(Equation e) {
		BasicEquation be = (BasicEquation) e;
		return new BasicEquation(be.getCoefficient(),(int) (be.getExponent() - be.getCoefficient()));
	}
	@Override
	public String toString() {		
		return this.getClass().getSimpleName();
	}
}
