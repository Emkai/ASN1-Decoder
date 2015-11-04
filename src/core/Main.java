package core;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import rules.BERMessage;
import rules.Message;

public class Main {

	public static void main(String[] args) throws IOException {
		
	}
	
	private static Message decodeMessage(String rule, byte[] bytes){
		switch(rule){
		case "BER":
			return new BERMessage(bytes);
		default:
			return null;
		}
	}
	
	private static byte[] readBinaryFile(String filePath) throws IOException{
		try{
			Path path = Paths.get(filePath);
		    byte[] bytes = Files.readAllBytes(Paths.get("C:/Users/Mattias/Documents/GitHub/arctic-group/ASN1-Decoder/res/NRTEST1SWETR0386"));
		    return bytes;
		}
		catch(Exception e){
			throw(e);
		}
	}
	
	private static byte[] readBinaryFile2(String filePath) throws IOException{
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
	}
	
	

}
