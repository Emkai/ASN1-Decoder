package storedconvert;


import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.ObjectInputStream.GetField;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.sound.sampled.TargetDataLine;

import org.omg.IOP.TAG_MULTIPLE_COMPONENTS;
import org.omg.IOP.TAG_ORB_TYPE;

import rules.BERTag;
import rules.Tag;




public class XML {
	
	private static String fileContent = "";

	public static void convert(String rule, ArrayList<Tag> tags) {		
		switch(rule) {
			case "BER":
				convertBERtoXML(tags);
		}
	}

	private static void convertBERtoXML(ArrayList<Tag> tags) {
	
		
		String indent = "";
		
		/*DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	        Date today = Calendar.getInstance().getTime();*/
	        
		PrintWriter writer;
		try {
		
		
			for ( Tag tag : tags){
				PrimitiveOrConstructed((BERTag)tag);
			}
			writer = new PrintWriter("res/xml/DecodedMessage.xml", "UTF-8");
			writer.println(fileContent);
			writer.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	private static void PrimitiveOrConstructed(BERTag tag) {
		if (tag.getTagCP() == "Primitive" ) {
			fileContent +=""+  
					"<"+tag.getTagType()+">" + tag.getTagData() + "</"+tag.getTagType()+">" ;	
		} else {
			System.out.println(tag.getTagData());
		  ArrayList<Tag> tags = tag.getTagTags();
			for ( Tag tag2 : tags){
				if(tag2 != null){
					PrimitiveOrConstructed((BERTag)tag2);
				}				
			}
		}
	}
}


		
	
	
		

