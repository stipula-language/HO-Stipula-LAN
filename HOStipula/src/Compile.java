import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.Stack;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import ast.*;
import lib.Pair;
import parser.*;



public class Compile {

	public static void main(String[] args) throws Exception {
	    
		String fileName = "Flour.stipula";
		FileInputStream is = new FileInputStream(fileName);
		ANTLRInputStream input = new ANTLRInputStream(is);
		HOStipulaLexer lexer = new HOStipulaLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		HOStipulaParser parser = new HOStipulaParser(tokens);
		
		if (parser.getNumberOfSyntaxErrors() == 0) { 

			ParseTree syntree = parser.prog();

			/* TYPE CHECKING */
			TypeChecker code = new TypeChecker();
			@SuppressWarnings("unchecked")
			Map<Pair<String, Integer>, Type> types = (Map<Pair<String, Integer>, Type>) code.visit(syntree);
			ArrayList<Pair<String,ArrayList<Pair<String,Type>>>> funParams = code.getFunParams();
			ArrayList<String> nc = code.getNames();
			System.out.println("TYPE CHECKING:");
			System.out.println("==================");
			TypeInference typeinferer = new TypeInference(types,nc,funParams);			
			typeinferer.print_map();
			System.out.println("==================");

			/* INTERPRETER */
			Interpreter codeInterpreter = new Interpreter();
			Object program = codeInterpreter.visit(syntree);
			((Program) program).runProgram(typeinferer);

			
		}
	}
}