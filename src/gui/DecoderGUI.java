package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DecoderGUI {

	public JFrame frame;
	private JTextField txtEnterFilePath;
	public String filePath;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DecoderGUI window = new DecoderGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
		
	/**
	 * Create the application.
	 */
	public DecoderGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("ASN1-Decoder");
		frame.setBounds(100, 100, 709, 234);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
        //Creates the area to display information
		final JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
		textArea.setForeground(Color.BLACK);
		textArea.setBackground(Color.WHITE);
		textArea.setEditable(false);
		textArea.setBounds(28,53,534,112);
		frame.getContentPane().add(textArea);
		
		

		
		
		//Browse button that allows me to chose a file
		JButton btnBrowse = new JButton("Browse...");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DecoderStart of = new DecoderStart();

				
				try{
					of.PickFile();
				}
				catch (Exception e) {
					
					e.printStackTrace();
				}
				filePath = of.path;
				txtEnterFilePath.setText(of.path);
				textArea.setText(of.sb.toString());
				
			}
			});
		
		btnBrowse.setBounds(573, 10, 110, 23);
		frame.getContentPane().add(btnBrowse);
		
		//Close window button
		JButton btnClose = new JButton ("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
	
		});
		btnClose.setBounds(573, 142, 110, 23);
		frame.getContentPane().add(btnClose);
		
		//Shows which file you chose
		txtEnterFilePath = new JTextField();
		txtEnterFilePath.setText("Enter file path...");
		txtEnterFilePath.setBounds(28, 11, 534, 20);
		frame.getContentPane().add(txtEnterFilePath);
		txtEnterFilePath.setColumns(10);

		}
}
	

	
			
			
		
		
	


