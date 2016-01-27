package core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import rules.BERDecoder;
import rules.BERTag;
import rules.Decoder;
import rules.Tag;

public class Main {

	public static void main(String[] args) throws IOException {

	
		// NRTRDE-0201.asn
		// NRTEST1SWETR0386
		// CDDNKTDSWETR90904.xml.asn
		// test
		// CDHNDMESWETR03713
		String s = "308006092A864886F70D010702A0803080020101310B300906052B0E03021A0500308006092A864886F70D0107010000A080308201EF30820158A00302010202102FA176B36EE9F049F444B40099661945300D06092A864886F70D0101050500300C310A300806035504030C0161301E170D3038313031353135303334315A170D3039313031353135303334315A300C310A300806035504030C016130819F300D06092A864886F70D010101050003818D00308189028181008953097086EE6147C5F4D5FFAF1B498A3D11EC5518E964DC52126B2614F743883F64CA51377ABB530DFD20464A48BD67CD27E7B29AEC685C5D10825E605C056E4AB8EEA460FA27E55AA62C498B02D7247A249838A12ECDF37C6011CF4F0EDEA9CEE687C1CB4A51C6AE62B2EFDB000723A01C99D6C23F834880BA8B42D5414E6F0203010001A3523050300E0603551D0F0101FF0404030204F0301D0603551D0E0416041442065D007887ED38677CBF9FB36FF78F82A8F8AD301F0603551D2304183016801442065D007887ED38677CBF9FB36FF78F82A8F8AD300D06092A864886F70D0101050500038181004D06EED02269A709C6F3F77E1123FB23FD543C4BD289AB45100468C5F059A05FFC6824D53FEEFBCFC20C0763B86342885E9CB68A0A73CBBC2A99D926C1AA9E9190E27E8BD8A8D3B5150387E8628DAD1E251045762ACF41ED8701E6D01859E63A4E8C9FB2A241F048B0ECD78A6FDB16A68149B7F4E9B2D340DC05A2E48EFA3C0E000031820125308201210201013020300C310A300806035504030C016102102FA176B36EE9F049F444B40099661945300906052B0E03021A0500A05D301806092A864886F70D010903310B06092A864886F70D010701301C06092A864886F70D010905310F170D3038313031353135303334335A302306092A864886F70D010904311604140000000000000000000000000000000000000000300D06092A864886F70D0101010500048180741ED28723067F9955759B6FC0A96760B1CC52A256B819C593B690C070268372671FA39C80877617EC5F83AB705E6F218540648473E51C6A166B9910B4DF67F2BCF7368AF3BDC33FD57BFDFB411A97B3584A7D876F40B40CC8F65CD2181F60BA08B207926CB57A0CD4BF3B289993D3931E671835A6FBC5870A359BF40EA5CAB3000000000000";
		FileOutputStream out = new FileOutputStream ("hej");
		
		byte[] bytes = readBinaryFile(getCleanPath()+"/res/NRTEST1SWETR0386");

		Decoder decoder = decodeMessage("BER", bytes);
		ArrayList<Tag> tags = decoder.getTags();
		convertTags("BER", tags, "XML");

	}
	
	private static void convertTags(String rule, ArrayList<Tag> tags, String format) {
		switch(format){
		case "XML":
			storedconvert.XML.convert(rule, tags);
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
