package rules;

import java.util.ArrayList;

public class BERDecoder extends Decoder{
	private byte[] bytes;
	private int byteCurrently;

	public BERDecoder(byte[] bytes) {
		this.bytes = bytes;
		this.byteCurrently = 0;
		
		decodeTag();
		
	}
	
	private void decodeTag(){
		ArrayList<String> tag = new ArrayList<String>();
		ArrayList<Byte> dataBytes = new ArrayList<Byte>();
		System.out.print("Byte: " + this.byteCurrently +", "+ this.bytes[this.byteCurrently]+", ");
		System.out.println(this.bytes[this.byteCurrently+1]);
		tag = readTag();
		System.out.print("Class: " + tag.get(0));
		System.out.print(", P/C: " + tag.get(1));
		System.out.print(", Tag: " + tag.get(2));
		int length = readLength();
		System.out.print(", Lenght: " + length);
		System.out.println("");
		
		if(tag.get(1) == "Primitive"){
			for (int i = this.byteCurrently; i> (this.byteCurrently+length); ){
				dataBytes.add(bytes[i]);
			}
			
		}
		else{
			int endByte = this.byteCurrently+length;
			while ( this.byteCurrently <= endByte){
				this.byteCurrently++;
				decodeTag();
			}
		}
	}
	
	private ArrayList<String> readTag(){
		ArrayList<String> tag = new ArrayList<String>();
		switch ( this.bytes[this.byteCurrently] >> 6 ) {
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
		
		if ( ((this.bytes[this.byteCurrently] >> 5) & 1 ) == 1 ){
			tag.add("Constructed");
		}
		else{
			tag.add("Primitive");
		}
		
		if (  (this.bytes[this.byteCurrently]&0x1f ) > 30 ){
			tag.add(readLongType());
		}
		else{
			String s = "" + (this.bytes[this.byteCurrently]&0x1f);
			tag.add(s);
			this.byteCurrently++;
		}
		
		return tag;
	}
	
	private String readLongType(){
		this.byteCurrently++;
		String type = "" + (this.bytes[this.byteCurrently]&0x7F);
		if((this.bytes[this.byteCurrently]>>7 & 1) == 1){
			type += readLongType();
		}
		return "" + (this.bytes[this.byteCurrently]&0x07F);
	}
	
	
	
	private int readLength(){
		if ( (this.bytes[this.byteCurrently] >> 7 & 1) == 1 ){
			if((this.bytes[this.byteCurrently]&0x07F) != 0){
				int lengthOctets = this.bytes[this.byteCurrently]&0x07F;
				System.out.println(" Lengthy: "+ lengthOctets);
				String length = "";
				for(int i = 1; i < lengthOctets+1; i++){
					length += this.bytes[this.byteCurrently + i]&0x07F;
				}
				return Integer.parseInt(length);
			}
			return 0;
			
		}
		else{
			this.byteCurrently++;
			return (int)this.bytes[this.byteCurrently]&0x0FF;
		}
	}
	
	private String readIndefinateLength(){
		this.byteCurrently++;
		String type = "" + (this.bytes[this.byteCurrently]&0x7F);
		if((this.bytes[this.byteCurrently]>>7 & 1) == 1){
			type += readLongType();
		}
		return "" + (this.bytes[this.byteCurrently]&0x7F);
	}

}