package rules;

import java.util.ArrayList;

public class BERDecoder extends Decoder{
	private byte[] bytes;
	private int byteCurrently;
	

	public BERDecoder(byte[] bytes) {
		this.bytes = bytes;
		this.byteCurrently = 0;
		int endByte = bytes.length;
		while ( this.byteCurrently < endByte){
			tags.add(decodeTag());
		}
	}
	

	private BERTag decodeTag(){
		// Stores tag information such as, Class, P/C and tag type
		ArrayList<String> tag = new ArrayList<String>();
		// For storing the data if it is a primitive tag
		ArrayList<Byte> dataBytes = new ArrayList<Byte>();
		
		
		System.out.println("TAG::    Byte pos: " + this.byteCurrently +", Bytes value at position ["+this.byteCurrently+", "+(this.byteCurrently+1)+"]: => ["+ (this.bytes[this.byteCurrently]&0x0ff ) +", " +(this.bytes[this.byteCurrently+1]&0x0ff )+"]");
		
		// Read the tag octet
		tag = readTag();
		
		System.out.print("         Class: " + tag.get(0));
		System.out.print(", P/C: " + tag.get(1));
		System.out.print(", Tag: " + tag.get(2));
		
		// Read the length octet/octets
		int length = readLength();
		
		
		System.out.print(", Lenght: " + length);
		System.out.println("\n");
		
		BERTag theTag = new BERTag(tag.get(0), tag.get(1), Integer.parseInt(tag.get(2)), length);
		// If it is primitive we store the next octets in the dataBytes
		if(tag.get(1) == "Primitive"){
			int endbyte = this.byteCurrently+length;
			for (int i = this.byteCurrently; i < endbyte;i++ ){
				dataBytes.add(bytes[i]);
					this.byteCurrently++;
				
			}
			theTag.setTagData(dataBytes);
		}
		
		// If it is constructed we loop through and read all the tags in the constructed tag.
		else{
			// if length is -1 then we have indefinite length
			if ( length == -1 ){
				boolean endofContent = false;
				while (!endofContent){
					BERTag nextTag = decodeTag();
					if (nextTag.getTagClass() == "Universal" && nextTag.getTagCP() == "Primitive" && nextTag.getTagType() == 0){
						endofContent = true;
					} 
					theTag.addTag(nextTag);
				}
			}
			else {
				int endByte = this.byteCurrently+length;
				while ( this.byteCurrently < endByte){

					if (endByte > bytes.length){
						System.out.println("\nERROR::  TO LONG, end of tag is at byte " + endByte + " and is greater that total amount of bytes " + bytes.length + "\n");
					}
					if (this.byteCurrently >= bytes.length){
						System.out.println("ERROR:: Trying to read byte that is beyond end of file");
						return theTag;
					}

					theTag.addTag(decodeTag());
				}
			}
		}
		
		return theTag;
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
			tag.add(""+readLongType());
		}
		else{
			String s = "" + (this.bytes[this.byteCurrently]&0x1f);
			tag.add(s);
			
		}
		this.byteCurrently++;
		return tag;
	}
	
	private int readLongType(){
		this.byteCurrently++;
		int type = (0<<7) + (this.bytes[this.byteCurrently]&0x7F);
		if((this.bytes[this.byteCurrently]>>7 & 1) == 1){
			type = (type<<7) +  readLongType();
			return type;
		}
		return (this.bytes[this.byteCurrently]&0x07F);
	}
	
	private int readLength(){
		
		// If the last bit is set to 1, this is a long length
		if ( ((this.bytes[this.byteCurrently] >> 7) & 1) == 1 ){
			
			// If 10000000 is the length octet then indefinite form is used, here we check if it is not indefinite
			if((this.bytes[this.byteCurrently]&0x07F) != 0){
				int lengthOctets = this.bytes[this.byteCurrently]&0x07F;
				int length = 0;
				
				// Go though all lenght octets and add them to length
				
				for(int i = 1; i < lengthOctets+1; i++){
					length = (length << 8) + (this.bytes[this.byteCurrently + i]&0x0ff);					
				}
				// Need to jump ahead same amout of bytes as we just read.
				this.byteCurrently+=lengthOctets;
				
				this.byteCurrently++;
				return length;
			}
			// Not implemetet yet, indefinite
			else{
				return -1;
			}
		}
		// else short length
		else{
			int length = (int)this.bytes[this.byteCurrently]&0x0FF;
			this.byteCurrently++;
			return length;
		}
	}
	
	private int readIndefinateLength(){
		this.byteCurrently++;
		int type = (0<<7) + (this.bytes[this.byteCurrently]&0x7F);
		if((this.bytes[this.byteCurrently]>>7 & 1) == 1){
			type = (type<<7) + readLongType();
		}
		return (this.bytes[this.byteCurrently]&0x7F);
	}

}