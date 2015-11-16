package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import java.awt.event.ActionListener;
import java.awt.List;
import java.awt.Choice;

public class DecoderGUI {

	private JFrame frame;
	private JTextField txtEnterFilePath;
	private JButton btnOk;
	

	/**
	 * @wbp.nonvisual location=188,179
	 */
	


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
		frame.setBounds(300, 100, 430, 180);
		frame.setTitle("ASN1-Decoder");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
	
	
	
		
		JButton btnBrowse = new JButton("Browse...");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				DecoderGUIBrowse Browse = new DecoderGUIBrowse();
				Browse.NewScreen();
			}
		});
		btnBrowse.setBounds(309, 30, 89, 23);
		frame.getContentPane().add(btnBrowse);
	
	
	
		
		txtEnterFilePath = new JTextField();
		txtEnterFilePath.setText("Enter file path...");
		txtEnterFilePath.setBounds(10, 30, 289, 23);
		frame.getContentPane().add(txtEnterFilePath);
		txtEnterFilePath.setColumns(10);
		
		btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnOk.setBounds(309, 107, 89, 23);
		frame.getContentPane().add(btnOk);
		
	}
	}

------------------------------------



package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JFileChooser;

public class DecoderGUIBrowse {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void NewScreen() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DecoderGUIBrowse window = new DecoderGUIBrowse();
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
	public DecoderGUIBrowse() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(400, 100, 616, 455);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setBounds(10, 11, 582, 397);
		frame.getContentPane().add(fileChooser);
	}
}