package tagMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapDecoder {
	public HashMap<String, ArrayList<String>> map;
	
	public MapDecoder(List<String> mapFile){
		this.map = new HashMap<String, ArrayList<String>>();
		
		this.drawMap(mapFile);
	}
	
	private void drawMap(List<String> mapFile) {
		String[] splitLine;
		String label;
		String tagNr;
		String type;
		ArrayList<String> mapValue = new ArrayList<String>();
		int lineNumber = 0;
		for(String line : mapFile){
			if(line.contains("::=")){
				if(line.contains("[APPLICATION ")){
					splitLine = line.split(" ::=");
					label = splitLine[0];
					splitLine = splitLine[1].split("APPLICATION ");
					splitLine = splitLine[1].split("] ");
					tagNr = splitLine[0];
					splitLine = splitLine[1].split(" --");
					type = splitLine[0];
					
					mapValue = new ArrayList<String>();
					mapValue.add(label);
					mapValue.add(type);
					this.map.put(("APPLICATION "+tagNr), mapValue);
					
					if (type.equals("SEQUENCE")){
						readSequence(lineNumber+1, mapFile, ("APPLICATION "+tagNr));
					}
					
					System.out.println("Label: " + label + ", Tag Nr: " + tagNr + ", Type: " + type);
				}
			}
			lineNumber++;
		}		
	}

	private void readSequence(int lineNumber, List<String> mapFile, String tagTypeNumber) {
		boolean endOfSequence = false;
		String line = "";
		String[] splitLine;
		ArrayList<String> sequence = new ArrayList<String>();
		int lineNr = lineNumber+1;
		while(!endOfSequence){
			line = mapFile.get(lineNr);
			if(line.equals("}")){
				endOfSequence = true;
				break;
			}
			splitLine = line.trim().replaceAll(" +", " ").split(" ");
			for(String l : splitLine){
				sequence.add(l);
				System.out.println(l);
			}
			sequence.add(" ");
			lineNr++;
		}
		this.map.put(tagTypeNumber+" SEQUENCE", sequence);
		
		
	}
	

}
