package operations;

import core.BasicEquation;
import core.Equation;

public class MultiplyCoefficientByExponent implements IBasicOperation{

	public Equation apply(Equation e) {
		BasicEquation be = (BasicEquation) e;
		return new BasicEquation(be.getCoefficient() * be.getExponent(),be.getExponent());
	}
	@Override
	public String toString() {		
		return this.getClass().getSimpleName();
	}

}
