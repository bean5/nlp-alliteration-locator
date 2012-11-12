package alliterationGUI;

import alliterationLocator.InterTextualFinder;

import javax.swing.*;
/*
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.*;
*/

import java.awt.*;
import java.awt.event.*;

/*
 * 
*/
public class AlliterationFinderGui {
	protected static InterTextualFinder finder = new InterTextualFinder();
	
	// Initialize all swing objects.
    private JFrame f = new JFrame("Alliteration Locator Beta"); //create Frame
    private JPanel pnlNorth = new JPanel(); // North quadrant 
    private JPanel pnlCenter = new JPanel(); // Center quadrant
    private JPanel pnlSouth = new JPanel(); // South quadrant
    
	// Buttons some there is something to put in the panels
    private JButton btnRun = new JButton("Run");

    static String corporaBaseDir = "corpora/";
    static String resultsBaseDir = "intertext_results/";

//    private JTextField txtFieldFilePrimary = new JTextField(corporaBaseDir + "");
//	private JTextField txtFieldFileSecondary = new JTextField(corporaBaseDir + "");
    private JTextField txtFieldFilePrimary = new JTextField(corporaBaseDir + "Abinadi.txt");
//    private JTextField txtFieldFileSecondary = new JTextField(corporaBaseDir + "Alma 2.txt");
    
    private JLabel lblFilePrimary = new JLabel("Primary File");
//    private JLabel lblFileSecondary = new JLabel("Secondary File");
    
    private JTextField txtFieldOutFile = new JTextField(resultsBaseDir + "results.txt");
    private JLabel lblFileOut = new JLabel("Output File");
    
    // Menu
    private JMenuBar mb = new JMenuBar(); 						// Menubar
    private JMenu mnuFile = new JMenu("File"); 					// File Entry on Menu bar
    private JMenuItem mnuItemSave = new JMenuItem("Save");
    
    private JMenu mnuHelp = new JMenu("Help"); 					// Help Menu entry
    private JMenuItem mnuItemAbout = new JMenuItem("About"); 	// About Entry
    
    private JMenuItem mnuItemQuit = new JMenuItem("Quit"); 		// Quit sub item

	private JTextField min = new JTextField("2");
	//private JTextField min = new JTextField("4");
	
	private JCheckBox checkUseStopWords = new JCheckBox("Use Stop Words", true);
	
	private JTextArea textArea = new JTextArea(20, 25);
	
    /* Constructor for the GUI */
    public AlliterationFinderGui() {
    	ActionListener clicked = new Clicked();
    	
		// Set menubar
        f.setJMenuBar(mb);
        //f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		//Build Menus
        mnuFile.add(mnuItemSave);
        mnuFile.add(mnuItemQuit);  	// Create Quit line
        
        mnuHelp.add(mnuItemAbout); 	// Create About line
        
        mb.add(mnuFile);       		// Add Menu items to form
        //mb.add(mnuHelp);
        
        txtFieldFilePrimary.setColumns(20);
        txtFieldOutFile.setColumns(20);
        
        // Add Buttons
        pnlNorth.add(lblFilePrimary);
        pnlNorth.add(txtFieldFilePrimary);
        
        pnlSouth.add(lblFileOut);
        pnlSouth.add(txtFieldOutFile);
        
        JPanel mainBox = new JPanel();
        mainBox.setLayout(new BorderLayout());
        mainBox.add(pnlNorth, BorderLayout.NORTH);
        mainBox.add(pnlCenter, BorderLayout.CENTER);
        mainBox.add(pnlSouth, BorderLayout.SOUTH);
        
        JPanel secondMainBox = new JPanel();
        secondMainBox.setLayout(new BorderLayout());
        
        JPanel options = new JPanel();
        //options.add(checkUseStopWords);
        //checkUseStopWords.setEnabled(false);
        
        min.addActionListener(clicked);
        options.add(new JLabel("Minimum Alliteration Size"));
        options.add(min);
        min.setColumns(3);
        
        secondMainBox.add(mainBox, BorderLayout.NORTH);
        secondMainBox.add(options, BorderLayout.CENTER);
        
        // Setup Main Frame
        f.getContentPane().setLayout(new BorderLayout());
		f.getContentPane().add(secondMainBox, BorderLayout.NORTH);
		f.getContentPane().add(btnRun, BorderLayout.EAST);
		
		textArea.setMargin(new Insets(5,5,5,5));
		textArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(textArea);
		f.getContentPane().add(logScrollPane, BorderLayout.CENTER);
		textArea.setText("Output will display here with time.");
		
		f.pack();
        f.setVisible(true);
        
		// Allows the Swing App to be closed
        f.addWindowListener(new ListenCloseWindow());

		//Add listeners
        btnRun.addActionListener(clicked);
        mnuItemSave.addActionListener(clicked);
        mnuItemAbout.addActionListener(clicked);
        mnuItemQuit.addActionListener(new ListenMenuQuit());
    }
	
    public class ListenMenuQuit implements ActionListener{
        public void actionPerformed(ActionEvent e){
            System.exit(0);         
        }
    }

    public class ListenCloseWindow extends WindowAdapter{
        public void windowClosing(WindowEvent e){
            System.exit(0);         
        }
    }
    
    public class Clicked implements ActionListener {
    	private JFileChooser fc;
    	
        public void actionPerformed(ActionEvent e){
        	Object source = e.getSource();
        	System.out.println(e.getActionCommand());
        	if(source == btnRun) {
        		min.selectAll();
        		min.setCaretPosition(min.getDocument().getLength());
        		
        		finder.findAlliterationsGivenParams(
	    			txtFieldFilePrimary.getText().trim(), 
	    			Integer.parseInt(min.getText().trim()), 
	    			checkUseStopWords.isSelected()
	        	);
	        	textArea.setText(finder.toString());	        	
        	} else if(source == mnuItemSave) {
	        	finder.saveTo(txtFieldOutFile.getText().trim());
        	} else if(source == mnuItemAbout) {
        		//System.exit(0);
        		//launch about frame
        	} else if(source == min) {
        		
        	}
        }
        
        public void mouseClicked(MouseEvent e) {}
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
    }

    // Display Frame
    public void launchFrame(){
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack(); //Adjusts panel to components for display
        f.setVisible(true);
    }
    
    public static void main(String[] args) {
		AlliterationFinderGui gui = new AlliterationFinderGui();
        gui.launchFrame();
	}
}
