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
		
		
		System.out.println("Byte pos: " + this.byteCurrently +", Bytes: "+ (this.bytes[this.byteCurrently]&0x0ff ) +", " +(this.bytes[this.byteCurrently+1]&0x0ff ));
		
		// Read the tag octet
		tag = readTag();
		
		
		System.out.print("Class: " + tag.get(0));
		System.out.print(", P/C: " + tag.get(1));
		System.out.print(", Tag: " + tag.get(2));
		
		
		// Read the length octet/octets
		int length = readLength();
		
		
		System.out.print(", Lenght: " + length);
		System.out.println("");
		
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
				boolean noEndofContent = true;
				while (noEndofContent){
					BERTag nextTag = decodeTag();
					if (nextTag.getTagClass() == "Universal" && nextTag.getTagCP() == "Primitive" && nextTag.getTagType() == 0){
						noEndofContent = false;
					}
					this.tags.add(nextTag);
				}
			}
			else {
				int endByte = this.byteCurrently+length;
				while ( this.byteCurrently < endByte){
					this.tags.add(decodeTag());
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
			tag.add(readLongType());
		}
		else{
			String s = "" + (this.bytes[this.byteCurrently]&0x1f);
			tag.add(s);
			
		}
		this.byteCurrently++;
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
				int lengthOctets = this.bytes[this.byteCurrently]&0x07F;
				String length = "";
				
				// Go though all lenght octets and add them to length
				for(int i = 1; i < lengthOctets+1; i++){
					length += this.bytes[this.byteCurrently + i]&0x0ff;
				}
				// Need to jump ahead same amout of bytes as we just read.
				for(int i = 1; i < lengthOctets+1; i++){
					this.byteCurrently++;
				}
				this.byteCurrently++;
				return Integer.parseInt(length);
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
	
	private String readIndefinateLength(){
		this.byteCurrently++;
		String type = "" + (this.bytes[this.byteCurrently]&0x7F);
		if((this.bytes[this.byteCurrently]>>7 & 1) == 1){
			type += readLongType();
		}
		return "" + (this.bytes[this.byteCurrently]&0x7F);
	}

}