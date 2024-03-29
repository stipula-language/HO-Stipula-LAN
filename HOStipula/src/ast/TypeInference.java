package ast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import lib.Pair;

public class TypeInference {

	Map<Pair<String,Integer>,Type> types = null;
	ArrayList<String> contractNames = null;
	ArrayList<Pair<String,Boolean>> assetsHO = null;
	ArrayList<Pair<String,Boolean>> fieldsHO = null;
	ArrayList<Pair<String,Boolean>> functionsHO = null;
	ArrayList<Pair<String,Boolean>> partiesHO = null;
	ArrayList<Pair<String,ArrayList<Pair<String,Type>>>> funParams = null;

	public TypeInference(Map<Pair<String,Integer>,Type> t,ArrayList<String> cn, ArrayList<Pair<String,ArrayList<Pair<String,Type>>>> fP) {
		types = t;
		contractNames = cn;
		funParams = fP;

	}

	public Map<Pair<String,Integer>,Type> getTypes(){
		return types;
	}

	public ArrayList<Pair<String,Boolean>> getPartiesHO(){
		return partiesHO;
	}

	public ArrayList<Pair<String,Boolean>> getFieldsHO(){
		return fieldsHO;
	}

	public ArrayList<Pair<String,Boolean>> getAssetsHO(){
		return assetsHO;
	}
	public ArrayList<Pair<String,Boolean>> getFunctionsHO(){
		return functionsHO;
	}

	public void setPartiesHO(ArrayList<Pair<String,Boolean>> f){
		partiesHO = f;
	}

	public void setFieldsHO(ArrayList<Pair<String,Boolean>> f){
		fieldsHO = f;
	}

	public void setAssetsHO(ArrayList<Pair<String,Boolean>> f){
		assetsHO = f;
	}
	public void setFunctionsHO(ArrayList<Pair<String,Boolean>> f){
		functionsHO = f;
	}

	public void print_map(){
		Map<Pair<String,Integer>,Type> assets = new LinkedHashMap<Pair<String,Integer>,Type>();
		Map<Pair<String,Integer>,Type> fields = new LinkedHashMap<Pair<String,Integer>,Type>();
		Map<Pair<String,Integer>,Type> functions = new LinkedHashMap<Pair<String,Integer>,Type>();

		for(Pair<String,Integer> s: types.keySet()) {

			if(s.getValue()==0 && types.get(s) instanceof AssetType) {
				assets.put(s,types.get(s));
			}
			else if(s.getValue()==0 && !(types.get(s) instanceof AssetType)) {
				fields.put(s,types.get(s)); 
			}
			else  {
				for(int i = 0; i<funParams.size(); i++) {
					if(i == s.getValue()-1) {
						for(Pair<String,Type> el : funParams.get(i).getValue()) {
							if(el.getKey().equals(s.getKey())) {
								functions.put(s,types.get(s));
							}
						}
					}
				}
			}

		}

		if(assets.size()>0) {
			System.out.println("Assets:");
			Map<Pair<String,Integer>,Type> tmpAssets = new LinkedHashMap<Pair<String,Integer>,Type>();
			for(Pair<String,Integer> el : assets.keySet()) {
				boolean flag = false;
				if(assetsHO!=null) {
					for(Pair<String,Boolean> el2 : assetsHO) {
						if(el2.getKey().equals(el.getKey()) && !el2.getValue()) {
							flag = true;
						}
					}
				}
				if(!flag) {
					tmpAssets.put(el,assets.get(el));
				}
			}
			print_global(tmpAssets);
		}
		if(fields.size()>0) {
			System.out.println("Fields:");
			Map<Pair<String,Integer>,Type> tmpFields = new LinkedHashMap<Pair<String,Integer>,Type>();
			for(Pair<String,Integer> el : fields.keySet()) {
				boolean flag = false;
				if(fieldsHO!=null) {
					for(Pair<String,Boolean> el2 : fieldsHO) {
						if(el2.getKey().equals(el.getKey()) && !el2.getValue()) {
							flag = true;
						}
					}
				}
				if(!flag) {
					tmpFields.put(el,fields.get(el));
				}
			}
			print_global(tmpFields);
		}
		System.out.println("Functions:");
		print_functions(functions);
	}

	public void print_global(Map<Pair<String,Integer>,Type> map) {
		for(Pair<String,Integer> pair : map.keySet()) {
			System.out.println("\t"+pair.getKey() +" type: " + map.get(pair).getTypeName());
		}
	}

	public void print_functions(Map<Pair<String,Integer>,Type> map) {
		Map<Pair<String,Integer>,Type> tmp1 = null;
		Map<Pair<String,Integer>,Type> tmp2 = null;

		for(int i=0; i<contractNames.size(); i++) {
			boolean toPrint = true;
			if(functionsHO!=null) {
				for(Pair<String,Boolean> el2 : functionsHO) {
					if(el2.getKey().equals(contractNames.get(i)) && !el2.getValue()) {
						toPrint = false;
					}
				}
			}
			if(toPrint) {
				tmp1 = new LinkedHashMap<Pair<String,Integer>,Type>();
				tmp2 = new LinkedHashMap<Pair<String,Integer>,Type>();
				for(Pair<String,Integer> pair : map.keySet()) {
					if(pair.getValue()-1==i) {
						if(map.get(pair) instanceof AssetType) {
							tmp2.put(pair,map.get(pair));
						}
						else {
							tmp1.put(pair,map.get(pair));
						}
					}
				}
				print_fun(tmp1,tmp2,i);
			}
		}

	}


	public void print_fun(Map<Pair<String,Integer>,Type> tmp1, Map<Pair<String,Integer>,Type> tmp2, int index) {
		String str = "\t"+contractNames.get(index)+"(";
		for(Pair<String,Integer> pair : tmp1.keySet()) {
			str = str + tmp1.get(pair).getTypeName()+",";
		}
		if(tmp1.size()>0) {
			str = str.substring(0, str.length()-1) + ")";
		}
		else {
			str = str + ")";
		}
		str = str + "[";
		for(Pair<String,Integer> pair : tmp2.keySet()) {
			str = str + tmp2.get(pair).getTypeName()+",";
		}
		if(tmp2.size()>0) {
			str = str.substring(0, str.length()-1) + "]";
		}
		else {
			str = str + "]";
		}
		System.out.println(str);

	}

	public Type getCorrectType(Field v1, int index) {
		Type toRet = null;
		for(Pair<String,Integer> pair : types.keySet()) {
			if(pair.getKey().equals(v1.getId()) && (index==pair.getValue() || pair.getValue()==0)) {
				toRet = types.get(pair);
			}
		}
		return toRet;
	}

	public boolean isTypeCorrect(Field v1, Field v2, int index) {
		boolean correct = true;
		Type t1 = null;
		Type t2 = null;
		for(Pair<String,Integer> pair : types.keySet()) {
			if(pair.getKey().equals(v1.getId()) && (index==pair.getValue() || pair.getValue()==0)) {
				t1 = types.get(pair);
			}
			else if(pair.getKey().equals(v2.getId()) && (index==pair.getValue() || pair.getValue()==0)) {
				t2 = types.get(pair);
			}
		}

		if(t1 instanceof GeneralType ) {
			if (!(t2 instanceof GeneralType)) {
				t1 = t2 ;
			}
		}
		else if(t2 instanceof GeneralType) {
			if (!(t1 instanceof GeneralType)) {
				t2 = t1;

			}
		}

		else if(!(t2 instanceof GeneralType) && !(t1 instanceof GeneralType) && !t1.getTypeName().equals(t2.getTypeName())){
			if((t1 instanceof AssetType && t2 instanceof RealType) || (t2 instanceof AssetType && t1 instanceof RealType)) {
				correct = true;
			}
			else {
				correct = false;
			}
		}
		return correct;
	}

	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public void addTypes(ArrayList<ArrayList<Pair<Field,String>>> values) {

		ArrayList<Pair<Field,String>> tmp = new ArrayList<Pair<Field,String>>();
		for(ArrayList<Pair<Field,String>> el : values) {
			tmp.addAll(el);
		}
		for(Pair<Field,String> el : tmp) {

			for(Pair<String,Integer> pair : types.keySet()) {

				if(el.getKey().getId().equals(pair.getKey()) && pair.getValue()==0) {
					DateUtils d = new DateUtils();
					if(d.isValidDate(el.getValue())  ) {
						types.put(pair,new TimeType());
					}
					else if(isNumeric(el.getValue())) {
						if(!(types.get(pair) instanceof TimeType)) {
							types.put(pair,new RealType());
						}
					}
					else if(el.getValue().equals("true") || el.getValue().equals("false")) {
						types.put(pair,new BooleanType());
					}
					else {
						types.put(pair,new StringType());
					}
				}
			}
		}
	}

	public void addType(String str, Type t, int index) {
		for(Pair<String,Integer> pair : types.keySet()) {
			if((str.equals(pair.getKey()) && ((pair.getValue()==index+1)|| (pair.getValue()==0 && index==0)) && t!=null)) {
				if(!(types.get(pair) instanceof TimeType)){
					types.put(pair,t);
				}
			}
		}
	}


}
