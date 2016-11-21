package operations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import core.AbstractEquation;
import core.BasicEquation;
import core.Constant;
import core.MultiplicativeEquation;
import core.SummativeEquation;

public class OperationHandler {
	private List<IBasicOperation> operationsList;
	private List<List<IBasicOperation>> combinationOperationList;		
	
	public OperationHandler() {
		super();				
		this.operationsList = new ArrayList<IBasicOperation>();
		
		//Basic Add operations
		operationsList.add(new AddCoefficientToExponent());
		operationsList.add(new AddExponentToCoefficient());
		operationsList.add(new AddOneToCoefficient());
		operationsList.add(new AddOneToExponent());
		
		//Basic Multiply operations
		operationsList.add(new MultiplyCoefficientByExponent());
		operationsList.add(new MultiplyExponentByCoefficient());
				
		//Basic Substract operations
		operationsList.add(new SubstractCoefficientToExponent());
		operationsList.add(new SubstractExponentToCoefficient());
		operationsList.add(new SubstractOneToCoefficient());
		operationsList.add(new SubstractOneToExponent());	
				
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
	
	public MultiplicativeEquation getModifiedEquation(MultiplicativeEquation source, List<IBasicOperation> equationModifiers){
		AbstractEquation m1;
		AbstractEquation m2;
		
		//Part 1: for the first equation in source
		if(source.getFirst() instanceof SummativeEquation){ 
			m1 = getModifiedEquation( (SummativeEquation) (source.getFirst()),equationModifiers);
		}
		else if(source.getFirst() instanceof MultiplicativeEquation){
			m1 = getModifiedEquation( (MultiplicativeEquation) (source.getFirst()),equationModifiers);
		}
		else if (source.getFirst() instanceof Constant){
			Constant c = (Constant) source.getFirst();
			m1 = getModifiedEquation( new BasicEquation(c.getValue(),1), equationModifiers);
		}
		else{
			m1 = getModifiedEquation((BasicEquation)source.getFirst(),equationModifiers );
		}
		
		//Part 2: for the second equation in source
		if(source.getFirst() instanceof SummativeEquation){ 
			m2 = getModifiedEquation( (SummativeEquation) (source.getFirst()),equationModifiers);
		}
		else if(source.getFirst() instanceof MultiplicativeEquation){
			m2 = getModifiedEquation( (MultiplicativeEquation) (source.getFirst()),equationModifiers);
		}
		else if (source.getFirst() instanceof Constant){
			Constant c = (Constant) source.getFirst();
			m2 = getModifiedEquation( new BasicEquation(c.getValue(),1), equationModifiers);
		}
		else{
			m2 = getModifiedEquation((BasicEquation)source.getFirst(),equationModifiers );
		}
		
		return new MultiplicativeEquation(m1,m2);
	}
	
	
	public SummativeEquation getModifiedEquation(SummativeEquation source, List<IBasicOperation> equationModifiers){
		AbstractEquation m1;
		AbstractEquation m2;
		
		//Part 1: for the first equation in source
		if(source.getFirst() instanceof SummativeEquation){ 
			m1 = getModifiedEquation( (SummativeEquation) (source.getFirst()),equationModifiers);
		}
		else if(source.getFirst() instanceof MultiplicativeEquation){
			m1 = getModifiedEquation( (MultiplicativeEquation) (source.getFirst()),equationModifiers);
		}
		else if (source.getFirst() instanceof Constant){
			Constant c = (Constant) source.getFirst();
			m1 = getModifiedEquation( new BasicEquation(c.getValue(),1), equationModifiers);
		}
		else{
			m1 = getModifiedEquation((BasicEquation)source.getFirst(),equationModifiers );
		}
		
		//Part 2: for the second equation in source
		if(source.getFirst() instanceof SummativeEquation){ 
			m2 = getModifiedEquation( (SummativeEquation) (source.getFirst()),equationModifiers);
		}
		else if(source.getFirst() instanceof MultiplicativeEquation){
			m2 = getModifiedEquation( (MultiplicativeEquation) (source.getFirst()),equationModifiers);
		}
		else if (source.getFirst() instanceof Constant){
			Constant c = (Constant) source.getFirst();
			m2 = getModifiedEquation( new BasicEquation(c.getValue(),1), equationModifiers);
		}
		else{
			m2 = getModifiedEquation((BasicEquation)source.getFirst(),equationModifiers );
		}
		
		return new SummativeEquation(m1,m2);
	}
	
	
	
	public BasicEquation getModifiedEquation(BasicEquation source, List<IBasicOperation> equationModifiers){
		BasicEquation temp = source;
		for(IBasicOperation bo : equationModifiers){
			temp = (BasicEquation) bo.apply(temp);
		}
		return temp;
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
	
	public static void main(String[] args) {
		
		OperationHandler o = new OperationHandler();
		System.out.println(o.getCombinationOperationList().toString());
		System.out.println("Il existe: " + o.getCombinationOperationList().size() + " combinaisons possibles.");
	}
}
