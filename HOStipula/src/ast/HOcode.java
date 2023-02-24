package ast;

import java.util.ArrayList;

import lib.Pair;

public class HOcode {

	private ArrayList<Field> fields = null;
	private ArrayList<Asset> assets = null;
	private ArrayList<Party> parties = null;
	private ArrayList<Contract> functions = null;
	private String name ;
	private ArrayList<Statement> statements = null;
	private ArrayList<Event> events = null;
	private String stateFin = null;
	private ArrayList<Pair<Expression,ArrayList<Statement>>> ifThenElse = null;

	public HOcode(ArrayList<Field> f, ArrayList<Asset> a, ArrayList<Party> p, ArrayList<Contract> funs, ArrayList<Statement> stats,ArrayList<Pair<Expression,ArrayList<Statement>>> ite, ArrayList<Event> evs, String state) {
		fields = f;
		assets = a;
		parties = p;
		functions = funs;
		statements = stats;
		ifThenElse = ite;
		events = evs;
		stateFin = state;
	}

	public void addName(String n) {
		name = n;
	}

	public String getName() {
		return name;
	}

	public String getLastState() {
		return stateFin;
	}

	public ArrayList<Statement> getStats(){
		return statements;
	}
	
	public ArrayList<Pair<Expression,ArrayList<Statement>>> getIfThenElse(){
		return ifThenElse;
	}

	public ArrayList<Event> getEvents(){
		return events;
	}

	public ArrayList<Field> getFields(){
		return fields;
	}

	public ArrayList<Asset> getAssets(){
		return assets;
	}

	public ArrayList<Party> getParties(){
		return parties;
	}

	public ArrayList<Contract> getContracts(){
		return functions;
	}

}
