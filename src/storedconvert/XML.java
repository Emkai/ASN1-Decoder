package storedconvert;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import rules.BERTag;
import rules.Tag;




public class XML {
	

	public static void convert(String rule, ArrayList<Tag> tags) {
		
		switch(rule) {
			case "BER":
				convertBERtoXML(tags);
		}
	}

	private static void convertBERtoXML(ArrayList<Tag> tags) {
		PrintWriter writer;
		try {
			BERTag tag = (BERTag)tags.get(0);
			writer = new PrintWriter("res/xml/FileName.xml", "UTF-8");
			writer.println("<firstTag>1</firstTag>");
	
			System.out.println(tag.getTagLength());
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
