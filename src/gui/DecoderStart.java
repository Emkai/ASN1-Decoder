package gui;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Scanner;

import javax.swing.JFileChooser;

public class DecoderStart {

	
	protected static String path = null;
	JFileChooser fileChooser = new JFileChooser();
	StringBuilder sb = new StringBuilder();
	
	public void PickFile() throws Exception {
		fileChooser.setAcceptAllFileFilterUsed(false);
		if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
		
		
		
			java.io.File file = fileChooser.getSelectedFile();
			path = file.getPath();
			Scanner	input = new Scanner(file);
			
			while(input.hasNext()) {
				sb.append(input.nextLine());
				sb.append("\n");
				
			}
			input.close();
		}
			else {
				sb.append("No file was selected");
			}
		}
	}

