package edu.ufl.cise.plpfa21.assignment1;

public interface IPLPLexer {
	
	void setInput(String input);
	String getInput();
	
	PLPUtils getpLPUtils();
	void setpLPUtils(PLPUtils pLPUtils);
	
	IPLPToken nextToken() throws LexicalException;
	
}
