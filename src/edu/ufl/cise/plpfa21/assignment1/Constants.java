package edu.ufl.cise.plpfa21.assignment1;

import java.util.HashMap;
import java.util.Map;

public class Constants {
	
	public final static String identifierRegex = "[a-zA-Z|_|$][A-Za-z|_|$|0-9]*";
	public final static String intLiteralRegex = "[0-9]+";
	public final static Map<String, String> escapeSequence;
	
	static {
		escapeSequence = new HashMap<String, String>();
		escapeSequence.put("\\\"","\"" );
		escapeSequence.put("\\\'", "'");
		escapeSequence.put("\\\\", "\\");
		
		
	}
	
}
