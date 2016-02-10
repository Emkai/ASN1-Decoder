package core;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import rules.BERDecoder;
import rules.Decoder;
import rules.Tag;

public class Main {

	public static void main(String[] args) throws IOException {
		// NRTRDE-0201.asn
		// NRTEST1SWETR0386
		// CDDNKTDSWETR90904.xml.asn
		// test
		// CDHNDMESWETR03713
		
		// "/res/NRTEST1SWETR0386"
		// "/res/NRTRDE-0201.asn"
		System.out.println(args[0]);
		String mapFileName = "";
		
		
		
		byte[] bytes = readBinaryFile(getCleanPath()+ "/" + args[0]);
		
		for(int i = 0; i<(args.length-1);i++ ){
			switch(args[i]){
			case "-d":
				mapFileName = args[i+1];
				break;
			}
		}

		Decoder decoder = decodeMessage("BER", bytes);
		ArrayList<Tag> tags = decoder.getTags();
		convertTags("BER", tags, "XML", mapFileName);
	}
	
	private static void convertTags(String rule, ArrayList<Tag> tags, String format, String mapFileName) throws IOException {
		switch(format){
		case "XML":
			storedconvert.XML.convert(rule, tags, mapFileName);
			break;
			
		default:
			break;
		}
		
	}

	public static String getCleanPath() {
		java.io.File file = new java.io.File("");   //Dummy file
	    return file.getAbsolutePath();
	}
	
	private static Decoder decodeMessage(String rule, byte[] bytes){
		switch(rule){
		case "BER":
			BERDecoder berDecoder = new BERDecoder(bytes);
			return berDecoder;
		default:
			return null;
		}
	}
	
	private static byte[] readBinaryFile(String filePath) throws IOException{
		try{
			Path path = Paths.get(filePath);
		    byte[] bytes = Files.readAllBytes(path);
		    return bytes;
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	/*private static byte[] readBinaryFile2(String filePath) throws IOException{
		try{
			byte[] buffer = new byte[4096];
		    ByteArrayOutputStream ous = new ByteArrayOutputStream();
		    @SuppressWarnings("resource")
			FileInputStream ios = new FileInputStream(filePath);
		    int read = 0;
		    while ((read = ios.read(buffer)) != -1) {
		        ous.write(buffer, 0, read);
		    }
		    return ous.toByteArray();
		}
		catch(Exception e){
			throw(e);
		}
	}*/
	
	

}
