package operations;

import core.BasicEquation;
import core.Equation;

public class SubstractOneToCoefficient implements IBasicOperation {
	public Equation apply(Equation e) {
		BasicEquation be = (BasicEquation) e;
		return new BasicEquation(be.getCoefficient() - 1,be.getExponent());
	}
	@Override
	public String toString() {		
		return this.getClass().getSimpleName();
	}

}
