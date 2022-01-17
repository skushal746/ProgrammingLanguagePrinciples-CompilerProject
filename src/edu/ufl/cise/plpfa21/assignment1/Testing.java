package edu.ufl.cise.plpfa21.assignment1;

import java.util.HashMap;
import java.util.Map;

public class Testing {
	public static void main(String[] args) {
		
        Map<String, String> escapeSequence = new HashMap<String, String>();
		escapeSequence.put("\\\"","\"" );
		escapeSequence.put("\\\'", "'");
		escapeSequence.put("\\\\", "\\");
		
		for(Map.Entry<String, String> singleEntry : escapeSequence.entrySet())
		{
			System.out.println(singleEntry.getKey() + " " + singleEntry.getValue());
		}
        
		Integer yolo = 1;
		Testing.changeInteger(yolo);
		
		System.out.println(yolo);
		
	}
	
	public static void changeInteger(Integer yolo)
	{
		yolo = yolo+110;
		System.out.println(yolo);
	}
}

