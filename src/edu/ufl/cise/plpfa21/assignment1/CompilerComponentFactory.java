package edu.ufl.cise.plpfa21.assignment1;

public class CompilerComponentFactory {

	static IPLPLexer getLexer(String input) {
		//TODO  create and return a Lexer instance to parse the given input.
		IPLPLexer lexer = new PLPLexer();
		PLPUtils pLPUtils = new PLPUtils();
		lexer.setInput(input);
		lexer.setpLPUtils(pLPUtils);
		return lexer;
	}
	
}