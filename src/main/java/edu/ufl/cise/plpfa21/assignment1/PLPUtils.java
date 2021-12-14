package edu.ufl.cise.plpfa21.assignment1;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import edu.ufl.cise.plpfa21.assignment1.PLPTokenKinds.Kind;

public class PLPUtils {
	
	public static Map<String, Kind> keywordsList = new HashMap<String, Kind>();
	static {
		keywordsList.put("VAR", Kind.KW_VAR);
		keywordsList.put("VAL", Kind.KW_VAL);
		keywordsList.put("FUN", Kind.KW_FUN);
		keywordsList.put("DO", Kind.KW_DO);
		keywordsList.put("END", Kind.KW_END);
		keywordsList.put("LET", Kind.KW_LET);
		keywordsList.put("SWITCH", Kind.KW_SWITCH);
		keywordsList.put("CASE", Kind.KW_CASE);
		keywordsList.put("DEFAULT", Kind.KW_DEFAULT);
		keywordsList.put("IF", Kind.KW_IF);
		keywordsList.put("ELSE", Kind.KW_ELSE);
		keywordsList.put("WHILE", Kind.KW_WHILE);
		keywordsList.put("RETURN", Kind.KW_RETURN);
		keywordsList.put("NIL", Kind.KW_NIL);
		keywordsList.put("TRUE", Kind.KW_TRUE);
		keywordsList.put("FALSE", Kind.KW_FALSE);
		keywordsList.put("INT", Kind.KW_INT);
		keywordsList.put("STRING", Kind.KW_STRING);
		keywordsList.put("BOOLEAN", Kind.KW_BOOLEAN);
		keywordsList.put("FLOAT", Kind.KW_FLOAT);
		keywordsList.put("LIST", Kind.KW_LIST);
	}
	
	
	
	public boolean checkIfLine(char characterToBeChecked)
	{
		if( characterToBeChecked == '\n' )
			return true;
		else return false;
	}
	
	public boolean checkIfReturnCarriage(char characterToBeChecked)
	{
		if( characterToBeChecked == '\r' )
			return true;
		else return false;
	}
	
	public boolean skipTheCharacter(char characterToBeChecked)
	{
		if( characterToBeChecked == ' ' || characterToBeChecked == '\t' || characterToBeChecked == '\n' || characterToBeChecked == '\r' )
			return true;
		else return false;
	}
	
	public boolean checkIfSymbol( String tokenToBeChecked ) {
		boolean isSymbol = false;
		switch( tokenToBeChecked )
		{
			case "=": 
				isSymbol = true;
				break;
			case ",":
				isSymbol = true;
				break;
			case ";":
				isSymbol = true;
				break;
			case ":":
				isSymbol = true;
				break;
			case "(":
				isSymbol = true;
				break;
			case ")":
				isSymbol = true;
				break;
			case "[":
				isSymbol = true;
				break;
			case "]":
				isSymbol = true;
				break;
			case "<":
				isSymbol = true;
				break;
			case ">":
				isSymbol = true;
				break;
			case "!":
				isSymbol = true;
				break;
			case "+":
				isSymbol = true;
				break;
			case "-":
				isSymbol = true;
				break;
			case "*":
				isSymbol = true;
				break;
			case "/":
				isSymbol = true;
				break;
			case "&&":
				isSymbol = true;
				break;
			case "||":
				isSymbol = true;
				break;
			case "==":
				isSymbol = true;
				break;
			case "!=":
				isSymbol = true;
				break;
		}
		return isSymbol;
	}
	
	public Kind getSymbolType(String StringToBeChecked) {
		Kind kindType = null;
		switch( StringToBeChecked )
		{
			case "=": 
				kindType = Kind.ASSIGN;
				break;
			case ",":
				kindType = Kind.COMMA;
				break;
			case ";":
				kindType = Kind.SEMI;
				break;
			case ":":
				kindType = Kind.COLON;
				break;
			case "(":
				kindType = Kind.LPAREN;
				break;
			case ")":
				kindType = Kind.RPAREN;
				break;
			case "[":
				kindType = Kind.LSQUARE;
				break;
			case "]":
				kindType = Kind.RSQUARE;
				break;
			case "<":
				kindType = Kind.LT;
				break;
			case ">":
				kindType = Kind.GT;
				break;
			case "!":
				kindType = Kind.BANG;
				break;
			case "+":
				kindType = Kind.PLUS;
				break;
			case "-":
				kindType = Kind.MINUS;
				break;
			case "*":
				kindType = Kind.TIMES;
				break;
			case "/":
				kindType = Kind.DIV;
				break;
			case "&&":
				kindType = Kind.AND;
				break;
			case "||":
				kindType = Kind.OR;
				break;
			case "==":
				kindType = Kind.EQUALS;
				break;
			case "!=":
				kindType = Kind.NOT_EQUALS;
				break;
		}
		
		return kindType;
		
	}
	
	public boolean checkIfKeyword( String tokenToBeChecked ) {
		boolean isKeyword = false;
		switch(tokenToBeChecked)
		{
			case "VAR":
				isKeyword = true;
				break;
			case "VAL":
				isKeyword = true;
				break;
			case "FUN":
				isKeyword = true;
				break;
			case "DO":
				isKeyword = true;
				break;
			case "END":
				isKeyword = true;
				break;
			case "LET":
				isKeyword = true;
				break;
			case "SWITCH":
				isKeyword = true;
				break;
			case "CASE":
				isKeyword = true;
				break;
			case "DEFAULT":
				isKeyword = true;
				break;
			case "IF":
				isKeyword = true;
				break;
			case "ELSE":
				isKeyword = true;
				break;
			case "WHILE":
				isKeyword = true;
				break;
			case "RETURN":
				isKeyword = true;
				break;
			case "NIL":
				isKeyword = true;
				break;
			case "TRUE":
				isKeyword = true;
				break;
			case "FALSE":
				isKeyword = true;
				break;
			case "INT":
				isKeyword = true;
				break;
			case "STRING":
				isKeyword = true;
				break;
			case "BOOLEAN":
				isKeyword = true;
				break;
			case "FLOAT":
				isKeyword = true;
				break;
			case "LIST":
				isKeyword = true;
				break;
		}	
		return isKeyword;
	}
	
	public Kind getKeywordType(String StringToBeChecked) {
		
		Kind kindType = null;
		
		switch( StringToBeChecked )
		{
			case "VAR":
				kindType = Kind.KW_VAR;
				break;
			case "VAL":
				kindType = Kind.KW_VAL;
				break;
			case "FUN":
				kindType = Kind.KW_FUN;
				break;
			case "DO":
				kindType = Kind.KW_DO;
				break;
			case "END":
				kindType = Kind.KW_END;
				break;
			case "LET":
				kindType = Kind.KW_LET;
				break;
			case "SWITCH":
				kindType = Kind.KW_SWITCH;
				break;
			case "CASE":
				kindType = Kind.KW_CASE;
				break;
			case "DEFAULT":
				kindType = Kind.KW_DEFAULT;
				break;
			case "IF":
				kindType = Kind.KW_IF;
				break;
			case "ELSE":
				kindType = Kind.KW_ELSE;
				break;
			case "WHILE":
				kindType = Kind.KW_WHILE;
				break;
			case "RETURN":
				kindType = Kind.KW_RETURN;
				break;
			case "NIL":
				kindType = Kind.KW_NIL;
				break;
			case "TRUE":
				kindType = Kind.KW_TRUE;
				break;
			case "FALSE":
				kindType = Kind.KW_FALSE;
				break;
			case "INT":
				kindType = Kind.KW_INT;
				break;
			case "STRING":
				kindType = Kind.KW_STRING;
				break;
			case "BOOLEAN":
				kindType = Kind.KW_BOOLEAN;
				break;
			case "FLOAT":
				kindType = Kind.KW_FLOAT;
				break;
			case "LIST":
				kindType = Kind.KW_LIST;
				break;
		}
		
		return kindType;
		
	}
	
	public boolean checkIfIdentifier( String tokenToBeChecked ) {
		boolean isIdentifier = Pattern.matches( Constants.identifierRegex, tokenToBeChecked );	
		return isIdentifier;
	}
	
	public boolean checkIfIntLiteral( String tokenToBeChecked ) {
		boolean isIntLiteral = Pattern.matches( Constants.intLiteralRegex, tokenToBeChecked );	
		return isIntLiteral;
	}
	
	public Map<String, String> handleComments(String input) throws LexicalException
	{
		Map<String, String> outputMap = new HashMap<String, String>(); 
		char[] inputCharacters = input.toCharArray();
		int inputTracker = 0;
		int inputLine = 1;
		int charPositionInLine = 0;
		while(true)
		{
			if(inputTracker >= input.length())
			{
				input = input.substring(inputTracker);
				outputMap.put("InputString", input);
				outputMap.put("InputLine", String.valueOf(inputLine));
				outputMap.put("CharPositionInLine", String.valueOf(charPositionInLine));
				return outputMap;
			}
			if( this.skipTheCharacter( inputCharacters[inputTracker] ) )
			{
				if(this.checkIfLine(inputCharacters[inputTracker]))
				{
					inputLine++;
					charPositionInLine=0;
				}
				inputTracker++;
				charPositionInLine++;
				continue;
			}
			else break;
		}
		
		int startToken = inputTracker;
		boolean endOfCommentsFound = false;
		int endOfComments = startToken+2;
		if(input.charAt(startToken)=='/' && input.length() > startToken+1 && input.charAt(startToken+1)=='*')
		{
			while(input.charAt(endOfComments)!='*' && input.length()>endOfComments && input.charAt(endOfComments+1)!='/' )
			{
				if(this.checkIfLine(input.charAt(endOfComments)))
				{
					inputLine++;
					charPositionInLine=0;
				}
				else
				{
					charPositionInLine++;
				}
				endOfComments++;
			}
		}
		
		if(input.charAt(startToken)=='/' && input.length() > startToken+1 && input.charAt(startToken+1)=='*' && input.charAt(endOfComments)=='*' &&  input.length()>endOfComments && input.charAt(endOfComments+1)=='/' )
		{
			endOfCommentsFound = true;
		}
		
		if(input.charAt(startToken)=='/' && input.length() > startToken+1 && input.charAt(startToken+1)=='*')
		{
			if(!endOfCommentsFound)
			{
				throw new LexicalException("Could not find the end of the comments", inputLine, charPositionInLine);
			}
			else
			{
				inputTracker = endOfComments+2;
				while(true)
				{
					if(inputTracker >= input.length())
					{
						input = input.substring(inputTracker);
						outputMap.put("InputString", input);
						outputMap.put("InputLine", String.valueOf(inputLine));
						outputMap.put("CharPositionInLine", String.valueOf(charPositionInLine));
						return outputMap;
					}
					if( this.skipTheCharacter( inputCharacters[inputTracker] ) )
					{
						if(this.checkIfLine(inputCharacters[inputTracker]))
						{
							inputLine++;
							charPositionInLine=0;
						}
						else charPositionInLine++;
						inputTracker++;
						continue;
					}
					else break;
				}
				input = input.substring(inputTracker);
				outputMap.put("InputString", input);
				outputMap.put("InputLine", String.valueOf(inputLine));
				outputMap.put("CharPositionInLine", String.valueOf(charPositionInLine));
			}
		}
		return outputMap;
		
	}
	
	public void handleCommentsChanged(IPLPLexer pLpLexer) throws LexicalException
	{	
		while(pLpLexer.getInput().contains("/*"))
		{
			
			int startPoint = pLpLexer.getInput().indexOf("/*");
			int endPoint = pLpLexer.getInput().indexOf("*/", startPoint);
			
			if(endPoint == -1)
				break;
			
			for(int i=startPoint;i<endPoint+2;i++)
			{
				if(pLpLexer.getInput().charAt(i)=='\n')
					continue;
				else pLpLexer.setInput(pLpLexer.getInput().substring(0,i) + " " + pLpLexer.getInput().substring(i+1));
			}
		}
		
		return;
	}
	
	
}
