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
import javax.swing.SingleSelectionModel;

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
				translateTag((BERTag)tag, null);
			}
			writer = new PrintWriter("res/xml/DecodedMessage.xml", "UTF-8");
			fileContent = fileContent.replaceAll("\n", System.lineSeparator());
			writer.println(fileContent);
			writer.close();		
	}
	
	
	private static void translateTag(BERTag tag, String parentSequenceInput) {
		ArrayList<String> mapTranslation = new ArrayList<String>();
		String tagType = null;
		String sequence = null;
		String parentSequence = parentSequenceInput;
		boolean sequenceEndOfLine;
		
		if (parentSequence != null){
			mapTranslation = mapDecoder.map.get(parentSequence);
			sequenceEndOfLine = false;
			tagType = mapTranslation.get(0);
			while(!sequenceEndOfLine){
				if(mapTranslation.get(0) == " "){
					sequenceEndOfLine = true;
				}
				mapTranslation.remove(0);				
			}				
		}
		if (tag.getTagCP() == "Primitive" ) {
			if(tag.getTagClass() == "Application"){
				mapTranslation = mapDecoder.map.get("APPLICATION "+tag.getTagType());
				if(mapTranslation != null){
					tagType = ""+mapTranslation.get(0);
				}
				else if(tagType == null){ tagType = ""+tag.getTagType(); }
			}
			else if(tagType == null){ tagType = ""+tag.getTagType(); }
			
			fileContent += indent + "<" + tagType + ">" + interpretData( tag.getTagData(), mapTranslation.get(1) ) + "</" + tagType + ">\n";								
		} 
		else {
			if(tag.getTagClass() == "Application"){
				mapTranslation = mapDecoder.map.get("APPLICATION "+tag.getTagType());
				if(mapTranslation != null){
					if (tagType == null){
						tagType = ""+mapTranslation.get(0);
					}
					if(mapTranslation.get(1).equals("SEQUENCE")){
						sequence = "APPLICATION " + tag.getTagType() + " SEQUENCE";
					}
				}
				else if(tagType == null){ tagType = ""+tag.getTagType(); }
			}
			else if(tagType == null){ tagType = ""+tag.getTagType(); }
			
		  ArrayList<BERTag> tags = tag.getTagTags();
		  fileContent += indent + "<"+tagType + ">\n" ;
		  indent += "    ";
			for ( Tag tag2 : tags){		
				translateTag((BERTag)tag2, sequence);
			}	
			indent = indent.substring(0, (indent.length()-4));
			fileContent += indent + "</" + tagType + ">\n";
		}
	}

	private static String interpretData(ArrayList<Byte> tagData, String string) {
		switch(string){
		case "INTEGER":
			return Integer.toString(byteToInt(tagData));
		case "AsciiString":
			return byteToString(tagData);
		case "NumberString":
			return byteToString(tagData);
		case "BCDString":
			return ""+byteToBCDInt(tagData);
		default:
			if(string.contains("AsciiString") | string.contains("HexString")){
				return byteToString(tagData);
			}
			if(string.contains("INTEGER")){
				return Integer.toString(byteToInt(tagData));
			}
			System.out.println(string);
			return tagData.toString();
		}
	}

	private static long byteToBCDInt(ArrayList<Byte> tagData) {
		int length = tagData.size();
		long binaryShift = 10;
		long result = 0;
		int sign = 1;
		System.out.println("Length: " + length);
		result += ( (tagData.get(length-1) & 0xf0) >> 4 );
		System.out.println((tagData.get(length-1) & 0xf0) >> 4);
		for( int l = length-2; l>=0;){
			result += ( tagData.get(l) & 0x0f ) *binaryShift;
			System.out.println(( tagData.get(l) & 0x0f )*binaryShift);
			System.out.println("Shift: " + binaryShift);
			binaryShift *= 10;
			
			//System.out.println(result);
			//System.out.println(result);
			result += ( (tagData.get(l) & 0xf0) >> 4 )*binaryShift;
			System.out.println((( tagData.get(l) & 0xf0) >> 4)*binaryShift );
			binaryShift *= 10;
			
			l--;
			//System.out.println(result);
			//System.out.println(result);
		}
		
		switch(( tagData.get(length-1) & 0x0f ) ){
		case 0xd:
			sign = -1;
			break;
		case 0xb:
			sign = -1;
			break;
		}
		return result;
	}

	private static String byteToString(ArrayList<Byte> tagData) {
		byte[] data = new byte[tagData.size()];
		int i = 0;
		for (Byte b : tagData){			
			data[i] = b;
			i++;
		}
		return new String(data);
	}

	private static int byteToInt(ArrayList<Byte> tagData) {
		int length = tagData.size();
		int byteShift = 0;
		int result = 0;
		for( int l = length-1; l>=0; l--){
			result = result | ( ( tagData.get(l) & 0xff ) << byteShift );
			byteShift += 8;
		}
		return result;
	}

	public static String getCleanPath() {
		java.io.File file = new java.io.File("");   //Dummy file
	    return file.getAbsolutePath();
	}

}


		
	
	
		

