package storedconvert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;




public class XML {
	
	public static void initFile() throws IOException {
	
	
		String name = "bdf";
		String path = "C:/Users/adam/Documents/GitHub/ASN1-Decoder/res/hej.xml";
		Files.write( Paths.get(path), name.getBytes(), StandardOpenOption.CREATE);
		
		
	}
}
