package edu.ufl.cise.plpfa21.assignment1;

import edu.ufl.cise.plpfa21.assignment2.IPLPParser;
import edu.ufl.cise.plpfa21.assignment2.PLPParser;

public class CompilerComponentFactory {

	public static IPLPLexer getLexer(String input) {
		//TODO  create and return a Lexer instance to parse the given input.
		IPLPLexer lexer = new PLPLexer();
		PLPUtils pLPUtils = new PLPUtils();
		lexer.setInput(input);
		lexer.setpLPUtils(pLPUtils);
		return lexer;
	}

	public static IPLPParser getParser(String input) {
		// TODO Auto-generated method stub
		IPLPParser pLPParser = new PLPParser(input);
		return pLPParser;
	}
	
}