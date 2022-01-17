package edu.ufl.cise.plpfa21.assignment1;

import edu.ufl.cise.plpfa21.assignment3.IPLPParser;
import edu.ufl.cise.plpfa21.assignment3.PLPParser;
import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment4.TypeCheckVisitor;

public class CompilerComponentFactory {

	/*
	 * For Assignment 1
	 */
	public static IPLPLexer getLexer(String input) {
		//TODO  create and return a Lexer instance to parse the given input.
		IPLPLexer lexer = new PLPLexer();
		PLPUtils pLPUtils = new PLPUtils();
		lexer.setInput(input);
		lexer.setpLPUtils(pLPUtils);
		return lexer;
	}
	
	
	/*
	 * For Assignment 2
	 */
	public static edu.ufl.cise.plpfa21.assignment2.IPLPParser getParserForAssignment2(String input) {
		// TODO Auto-generated method stub
		edu.ufl.cise.plpfa21.assignment2.IPLPParser pLPParser = new edu.ufl.cise.plpfa21.assignment2.PLPParser(input);
		return pLPParser;
	}
	
	
	/*
	 * For Assignment 3
	 */
	public static IPLPParser getParser(String input) {
		// TODO Auto-generated method stub
		IPLPParser pLPParser = new PLPParser(input);
		return pLPParser;
	}
	
	/*
	 * For Assignment 4
	 */
	public static ASTVisitor getTypeCheckVisitor() {
		// Replace this with whatever is needed for your compiler
		return new TypeCheckVisitor();
	}
	
}