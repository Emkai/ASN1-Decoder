package rules;

import java.util.ArrayList;

public class BERDecoder extends Decoder{
	private byte[] bytes;

	public BERDecoder(byte[] bytes) {
		this.bytes = bytes;
		ArrayList<String> tag = new ArrayList<String>();
		
		tag = readTag(0);
		System.out.println(tag.get(0));
		System.out.println(tag.get(1));
		System.out.println(tag.get(2));
		
		int length = readLength(1);
		System.out.println(length);
		
	}
	
	private ArrayList<String> readTag(int byteIndex){
		ArrayList<String> tag = new ArrayList<String>();
		switch ( this.bytes[byteIndex] >> 6 ) {
			case 0:
				tag.add("Universal");
				break;
			case 1:
				tag.add("Application");
				break;
			case 2:
				tag.add("Context-specific");
				break;
			case 3:
				tag.add("Private");
				break;
		}
		
		if ( ((this.bytes[byteIndex] >> 5) & 1 ) == 1 ){
			tag.add("Constructed");
		}
		else{
			tag.add("Primitive");
		}
		
		if (  (this.bytes[byteIndex]&0x1f ) > 30 ){
			tag.add("Long Size type");
		}
		else{
			String s = "" + (this.bytes[byteIndex]&0x1f);
			tag.add(s);
		}
		
		return tag;
	}
	
	private int readLength(int byteIndex){
		if ( this.bytes[byteIndex] == 0x80 ){
			System.out.println("Long length");
			return 0;
		}
		else{
			return (int)this.bytes[byteIndex]&0x0FF;
		}
	}

}