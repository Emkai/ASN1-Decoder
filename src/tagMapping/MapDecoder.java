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
					
					System.out.println("Label: " + label + ", Tag Nr: " + tagNr + ", Type: " + type);
				}
				//System.out.println(line);
			}
		}		
	}
	

}
