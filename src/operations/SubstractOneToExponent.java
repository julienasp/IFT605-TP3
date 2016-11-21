package operations;

import core.BasicEquation;
import core.Equation;

public class SubstractOneToExponent implements IBasicOperation{

	public Equation apply(Equation e) {
		BasicEquation be = (BasicEquation) e;
		return new BasicEquation(be.getCoefficient(),be.getExponent() - 1);
	}
	@Override
	public String toString() {		
		return this.getClass().getSimpleName();
	}

}
