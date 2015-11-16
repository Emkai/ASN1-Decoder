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
		System.out.println("Byte pos: " + this.byteCurrently +", Bytes: "+ (this.bytes[this.byteCurrently]&0x0ff ) +", " +(this.bytes[this.byteCurrently+1]&0x0ff ));
		tag = readTag();
		System.out.print("Class: " + tag.get(0));
		System.out.print(", P/C: " + tag.get(1));
		System.out.print(", Tag: " + tag.get(2));
		int length = readLength();
		System.out.print(", Lenght: " + length);
		System.out.println("");
		
		if(tag.get(1) == "Primitive"){
			int endbyte = this.byteCurrently+length;
			for (int i = this.byteCurrently; i < endbyte;i++ ){
				dataBytes.add(bytes[i]);
					this.byteCurrently++;
				
			}
			
		}
		else{
			int endByte = this.byteCurrently+length;
			while ( this.byteCurrently <= endByte){
				decodeTag();
			}
		}
	}
	
	private ArrayList<String> readTag(){
		ArrayList<String> tag = new ArrayList<String>();
		switch ( (this.bytes[this.byteCurrently] >> 7) & 0x2 ) {
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
			System.out.print("Type-type: Long, ");
			tag.add(readLongType());
		}
		else{
			System.out.print("Type-type: Short, ");
			String s = "" + (this.bytes[this.byteCurrently]&0x1f);
			tag.add(s);
			
		}
		this.byteCurrently++;
		System.out.println("\nAfter read tag at: "+this.byteCurrently);
		return tag;
	}
	
	private String readLongType(){
		this.byteCurrently++;
		String type = "" + (this.bytes[this.byteCurrently]&0x7F);
		if((this.bytes[this.byteCurrently]>>7 & 1) == 1){
			
			type += readLongType();
			return type;
		}
		
		return "" + (this.bytes[this.byteCurrently]&0x07F);
	}
	
	
	
	private int readLength(){
		// If the last bit is set to 1, this is a long length
		if ( ((this.bytes[this.byteCurrently] >> 7) & 1) == 1 ){
			// If 10000000 is the length octet then indefinite form is used, here we check if it is not indefinite
			if((this.bytes[this.byteCurrently]&0x07F) != 0){
				System.out.print(", Length-type: Long");
				int lengthOctets = this.bytes[this.byteCurrently]&0x07F;
				String length = "";
				for(int i = 1; i < lengthOctets+1; i++){
					length += this.bytes[this.byteCurrently + i]&0x0ff;
				}
				for(int i = 1; i < lengthOctets+1; i++){
					this.byteCurrently++;
				}
				this.byteCurrently++;
				return Integer.parseInt(length);
			}
			// Not implemetet yet, indefinite
			else{
				System.out.print(", Length-type: Indefinite");
				return 0;
			}
		}
		// else short length
		else{
			System.out.print(", Length-type: Short");
			
			int length = (int)this.bytes[this.byteCurrently]&0x0FF;
			this.byteCurrently++;
			return length;
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