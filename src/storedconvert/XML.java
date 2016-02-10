package storedconvert;



import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import rules.BERTag;
import rules.Tag;
import tagMapping.MapDecoder;




public class XML {
	private static String fileContent = "";
	static String indent = "";
	private static MapDecoder mapDecoder;
	
	public static void convert(String rule, ArrayList<Tag> tags, String mapFileName) throws IOException {
		List<String> mapFile = null;
		if(mapFileName != ""){
			 mapFile =  Files.readAllLines(Paths.get(getCleanPath()+"/"+mapFileName));
			 mapDecoder  = new MapDecoder(mapFile);
		}else{
			mapDecoder  = new MapDecoder();
		}
		
		
		
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
			System.out.println("NOW            I              WANT        TO          WRITE");
			//System.out.println(getCleanPath()+"/res/xml/DecodedMessage.xml");
			writer = new PrintWriter(getCleanPath()+"/DecodedMessage.xml", "UTF-8");
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
			if(mapTranslation.size() != 0){
				tagType = mapTranslation.get(0);
				while(!sequenceEndOfLine){
					if(mapTranslation.get(0) == " "){
						sequenceEndOfLine = true;
					}
					mapTranslation.remove(0);				
				}	
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
			System.out.println(tagType);
			System.out.println(tag.getTagData());
			System.out.println("Size:" + mapTranslation);
			if(mapTranslation != null){
				fileContent += indent + "<" + tagType + ">" + interpretData( tag.getTagData(), mapTranslation.get(1) ) + "</" + tagType + ">\n";
			}
			else{
				fileContent += indent + "<" + tagType + ">" + tag.getTagData() + "</" + tagType + ">\n";
			}
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
		case "AddressStringDigits":
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
		long binaryShift = 1;
		long result = 0;
		int sign = 1;
		System.out.println("Length: " + length);
		//result += ( (tagData.get(length-1) & 0xf0) >> 4 );
		System.out.println((tagData.get(length-1) & 0xf0) >> 4);
		for( int l = length-1; l>=0;){
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
		default:
			sign = 1;
			break;
		}
		return result * sign;
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


		
	
	
		

