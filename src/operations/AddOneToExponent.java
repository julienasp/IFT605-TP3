package operations;

import core.BasicEquation;
import core.Equation;

public class AddOneToExponent implements IBasicOperation{
	public Equation apply(Equation e) {
		BasicEquation be = (BasicEquation) e;
		return new BasicEquation(be.getCoefficient(),be.getExponent() + 1);
	}
}
