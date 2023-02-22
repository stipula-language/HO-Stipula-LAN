package ast;

import java.util.ArrayList;

public class HOcode {
	
	private ArrayList<Field> fields = null;
	private ArrayList<Asset> assets = null;
	private ArrayList<Party> parties = null;
	private ArrayList<Contract> functions = null;
	
	public HOcode(ArrayList<Field> f, ArrayList<Asset> a, ArrayList<Party> p, ArrayList<Contract> funs) {
		fields = f;
		assets = a;
		parties = p;
		functions = funs;
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
