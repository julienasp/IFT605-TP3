package core;
import java.util.Vector;


public class EquationsProvider {
	private final Vector<Equation> list;
	
	public EquationsProvider(){
		list = new Vector<Equation>();
		initialise();
	}
	
	private void initialise() {
		BasicEquation b1 = new BasicEquation(2,2);
		BasicEquation b2 = new BasicEquation(-3,2);
		BasicEquation b3 = new BasicEquation(6,2);
		SummativeEquation s1 = new SummativeEquation(b1,b2);
		SummativeEquation s2 = new SummativeEquation(b2,b3);
		SummativeEquation s3 = new SummativeEquation(s1,s2);
		SummativeEquation s4 = new SummativeEquation(s3,b3);
		MultiplicativeEquation m1 = new MultiplicativeEquation(b1,b2);
		MultiplicativeEquation m2 = new MultiplicativeEquation(s1,s2);
		MultiplicativeEquation m3 = new MultiplicativeEquation(s3,s4);
		MultiplicativeEquation m4 = new MultiplicativeEquation(m2,m3);	
		//MyEquation my = new MyEquation(2,2);
		//list.add((Equation)my);
		list.add((Equation) b1);
		list.add((Equation) b2);
		list.add((Equation) b3);
		list.add((Equation) s1);
		list.add((Equation) s2);
		list.add((Equation) s3);
		list.add((Equation) s4);
		list.add((Equation) m1);
		list.add((Equation) m2);
		list.add((Equation) m3);
		list.add((Equation) m4);
	}

	public Vector<Equation> getList() {
		return list;
	}
	
	
}