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
import java.util.List;

import javax.sound.sampled.TargetDataLine;

import org.omg.IOP.TAG_MULTIPLE_COMPONENTS;
import org.omg.IOP.TAG_ORB_TYPE;

import rules.BERTag;
import rules.Tag;
import tagMapping.MapDecoder;




public class XML {
	private static String fileContent = "";
	static String indent = "";
	private static MapDecoder mapDecoder;
	
	public static void convert(String rule, ArrayList<Tag> tags) throws IOException {
		List<String> mapFile =  Files.readAllLines(Paths.get(getCleanPath()+"/res/NRTRDE-0201.asn"));
		mapDecoder  = new MapDecoder(mapFile);
		
		switch(rule) {
			case "BER":
				convertBERtoXML(tags);
		}
	}

	private static void convertBERtoXML(ArrayList<Tag> tags) throws FileNotFoundException, UnsupportedEncodingException {	        
		PrintWriter writer;
			for ( Tag tag : tags){
				PrimitiveOrConstructed((BERTag)tag);
			}
			writer = new PrintWriter("res/xml/DecodedMessage.xml", "UTF-8");
			fileContent = fileContent.replaceAll("\n", System.lineSeparator());
			writer.println(fileContent);
			writer.close();		
	}
	
	
	private static void PrimitiveOrConstructed(BERTag tag) {
		ArrayList<String> mapTranslation = new ArrayList<String>();
		String tagType = "";
		if (tag.getTagCP() == "Primitive" ) {
			if(tag.getTagClass() == "Application"){
				mapTranslation = mapDecoder.map.get("APPLICATION "+tag.getTagType());
				if(mapTranslation != null){
					tagType = ""+mapTranslation.get(0);
				}
				else{
					tagType = ""+tag.getTagType();
				}
			}
			else{
				tagType = ""+tag.getTagType();
			}
			fileContent += indent + "<" + tagType + ">" + tag.getTagData() + "</" + tagType + ">\n";
								
		} 
		else {
			if(tag.getTagClass() == "Application"){
				mapTranslation = mapDecoder.map.get("APPLICATION "+tag.getTagType());
				if(mapTranslation != null){
					tagType = ""+mapTranslation.get(0);
				}
				else{
					tagType = ""+tag.getTagType();
				}
			}
			else{
				tagType = ""+tag.getTagType();
			}
			
		  ArrayList<BERTag> tags = tag.getTagTags();
		  fileContent += indent + "<"+tagType + ">\n" ;
		  indent += "    ";
			for ( Tag tag2 : tags){		
				PrimitiveOrConstructed((BERTag)tag2);
			}	
			indent = indent.substring(0, (indent.length()-4));
			fileContent += indent + "</" + tagType + ">\n";			
		}
	}
	
	public static String getCleanPath() {
		java.io.File file = new java.io.File("");   //Dummy file
	    return file.getAbsolutePath();
	}
}


		
	
	
		

