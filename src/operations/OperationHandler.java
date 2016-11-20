package operations;

import java.util.Vector;

import core.Equation;

public class OperationHandler {
	private Vector<Vector<IBasicOperation>> operationsList;
	private Equation source;	
	private Vector<Equation> vResult;
	
	
	public OperationHandler(Equation source) {
		super();
		this.source = source;		
		this.vResult = new Vector<Equation>();
		this.operationsList = new Vector<Vector<IBasicOperation>>();
		
		//Basic Add operations
		operationsList.add(new AddCoefficientToExponent());
		operationsList.add(new AddExponentToCoefficient());
		operationsList.add(new AddOneToCoefficient());
		operationsList.add(new AddOneToExponent());
		
		//Basic Substract operations
		operationsList.add(new SubstractCoefficientToExponent());
		operationsList.add(new SubstractExponentToCoefficient());
		operationsList.add(new SubstractOneToCoefficient());
		operationsList.add(new SubstractOneToExponent());
		
		//Basic Multiply operations
		operationsList.add(new MultiplyCoefficientByExponent());
		operationsList.add(new MultiplyExponentByCoefficient());
		
		//Basic Divide operations
		operationsList.add(new DivideCoefficientByExponent());
		operationsList.add(new DivideExponentByCoefficient());		
	}
	
	public Vector<IBasicOperation> getVo() {
		return operationsList;
	}
	public void setVo(Vector<IBasicOperation> vo) {
		this.operationsList = vo;
	}
	public Equation getSource() {
		return source;
	}
	public void setSource(Equation source) {
		this.source = source;
	}
	public Vector<Equation> getvResult() {
		return vResult;
	}
	public void setvResult(Vector<Equation> vResult) {
		this.vResult = vResult;
	}	
}
