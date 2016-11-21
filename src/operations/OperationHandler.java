package operations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import core.BasicEquation;
import core.Equation;

public class OperationHandler {
	private List<IBasicOperation> operationsList;
	private List<List<IBasicOperation>> combinationOperationList;
	private Equation source;	
	private ArrayList<Equation> vResult;
	
	
	public OperationHandler(Equation source) {
		super();
		this.source = source;		
		this.vResult = new ArrayList<Equation>();
		this.operationsList = new ArrayList<IBasicOperation>();
		
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
		
		List<List<IBasicOperation>> powerSet = new ArrayList<List<IBasicOperation>>();	
		
		
		for (int i = 1; i <= operationsList.size(); i++){
		        powerSet.addAll(combination(operationsList, i));
		        
		}
		System.out.println(powerSet.toString());
		this.combinationOperationList = powerSet;			
	}
	
	//Une forme de l'implémentation du lien ci-dessous
	//https://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Sets.html#powerSet(java.util.Set)
	private static <T> List<List<T>> combination(List<T> values, int size) {

	    if (0 == size) {
	        return Collections.singletonList(Collections.<T> emptyList());
	    }

	    if (values.isEmpty()) {
	        return Collections.emptyList();
	    }

	    List<List<T>> combination = new ArrayList<List<T>>();

	    T actual = values.iterator().next();

	    List<T> subSet = new ArrayList<T>(values);
	    subSet.remove(actual);

	    List<List<T>> subSetCombination = combination(subSet, size - 1);

	    for (List<T> set : subSetCombination) {
	        List<T> newSet = new ArrayList<T>(set);
	        newSet.add(0, actual);
	        combination.add(newSet);
	    }

	    combination.addAll(combination(subSet, size));

	    return combination;
	}
	
	
	
	public Equation getSource() {
		return source;
	}
	public void setSource(Equation source) {
		this.source = source;
	}

	public List<IBasicOperation> getOperationsList() {
		return operationsList;
	}

	public void setOperationsList(List<IBasicOperation> operationsList) {
		this.operationsList = operationsList;
	}

	public List<List<IBasicOperation>> getCombinationOperationList() {
		return combinationOperationList;
	}

	public void setCombinationOperationList(
			List<List<IBasicOperation>> combinationOperationList) {
		this.combinationOperationList = combinationOperationList;
	}

	public ArrayList<Equation> getvResult() {
		return vResult;
	}

	public void setvResult(ArrayList<Equation> vResult) {
		this.vResult = vResult;
	}
	public static void main(String[] args) {
		
		OperationHandler o = new OperationHandler(new BasicEquation(2,3));
		System.out.println(o.getCombinationOperationList().toString());
		System.out.println("Il existe: " + o.getCombinationOperationList().size() + " combinaisons possibles.");
	}
}
