package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import server.ServerListener;
import both.classess.ActionData;
import both.classess.Recipe;
import both.classess.RecipesArray;
import both.conf.ConfActions;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class CoreClient {
	
	public static int ActionID = 0;
	private JFrame frame;
	public static RecipesArray RecipeStorage = new RecipesArray();

	public static void main( String[] args ){
		
		Client client = new Client();
		new Thread( client ).start();
	    try {
			client.connect(5000, "localhost", 54555, 54777);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	    
	    //client.getKryo().setRegistrationRequired(true);
	    client.getKryo().register( both.classess.ActionData.class );
	    client.getKryo().register( both.classess.RecipesArray.class );
	    client.getKryo().register( both.classess.Recipe.class );
	    client.getKryo().register( both.classess.Ingredient.class );
	    client.getKryo().register(java.util.HashMap.class);
	    client.getKryo().register(java.util.ArrayList.class);

	    client.sendTCP( new ActionData( getActID(), ConfActions.ActGetData ) );
	    
	    client.addListener( new ClientListener() );
	   	
	    //
	    EventQueue.invokeLater( new Runnable() {
			public void run() {
				try {
					CoreClient window = new CoreClient();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} );
	}
	
	//
	public static void showThis( String string ){
		System.out.println( string );
	}
	public static int getActID(){
		ActionID++;
		return ActionID;
	}
	
	public CoreClient(){
		initializeGUI();
	}
	
	private JPanel titlePanel;
	private JLabel label;
	Dimension tableDimension;
	
	private void initializeGUI(){
		
		frame = new JFrame();
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		JTabbedPane tabbedPane = new JTabbedPane( JTabbedPane.TOP );
		frame.getContentPane().add( tabbedPane, BorderLayout.NORTH );
		
		JPanel panel_1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		tabbedPane.addTab("Receptes", null, panel_1, null);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Receptes izveide", null, panel_2, null);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Export / Import", null, panel_3, null);
		
		/*
		 * 
		 * panel 1
		 * 
		 */
		
		JPanel panel1_left = new JPanel();
		JPanel panel1_right = new JPanel(); //new GridLayout( 1, 1 )
		
		JPanel panel1_left1 = new JPanel();
		panel1_left1.setLayout( new BoxLayout( panel1_left1, BoxLayout.PAGE_AXIS ));
		panel1_left1.setBorder(new EmptyBorder(10, 10, 10, 10));
		//JPanel panel_left2 = new JPanel();
		//panel_left2.setLayout(new BoxLayout(panel_left2, BoxLayout.PAGE_AXIS));
		JPanel panel1_right1 = new JPanel();
		panel1_right1.setLayout( new BoxLayout( panel1_right1, BoxLayout.PAGE_AXIS ));
		panel1_right1.setBorder(new EmptyBorder(10, 10, 10, 10));
		//JPanel panel_right2 = new JPanel();

		panel1_left.add( panel1_left1 );
		//panel_left.add( panel_left2 );
		panel1_right.add( panel1_right1 );
		//panel_right.add( panel_right2 );
		
		panel_1.setLayout( new BorderLayout() );
		panel_1.add( panel1_left, BorderLayout.WEST );
		panel_1.add( panel1_right );
		
		
		//title
		titlePanel = new JPanel();
		titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		label = new JLabel( "Receptes:" );
		titlePanel.add( label );
		panel1_left1.add( titlePanel );
		//title end
		
		/**
		 * 
		 * table
		 * 
		 */
		
		Object columnNames[] = { "Recepte", "Kategorija" };
		
		String[][] dataValues = new String[100][2];

		for( int iY = 0; iY < 100; iY++ ){
			for( int iX = 0; iX < 2; iX++ ){
				dataValues[iY][iX] = "" + iX + "," + iY;
			}
		}
		
		String[][] tableRecipes = new String[ RecipeStorage.size() ][2];
		int tmp = 0;
		for( Recipe recipe : RecipeStorage.getList().values() ){
			tableRecipes[ tmp ][ 0 ] = recipe.getName();
			tableRecipes[ tmp ][ 1 ] = recipe.getCID() + "";
			tmp++;
		}
		
		// table
		JTable recipeTable = new JTable( tableRecipes, columnNames ) {
	        private static final long serialVersionUID = 1L;

	        public boolean isCellEditable(int row, int column) {                
	                return false;               
	        };
	    };
		JScrollPane recipeScrollPanel = new JScrollPane();
		
		recipeTable.setColumnSelectionAllowed( false );
		recipeTable.setShowHorizontalLines( true );
		recipeTable.setShowVerticalLines( false );

		
		recipeTable.setSelectionForeground( Color.white );
		recipeTable.setSelectionBackground( Color.red );
		recipeScrollPanel = new JScrollPane();
		recipeScrollPanel = recipeTable.createScrollPaneForTable( recipeTable );
						
		tableDimension = new Dimension( 250, 350 );
						
		recipeTable.setPreferredScrollableViewportSize( tableDimension );
		recipeTable.setFillsViewportHeight( true );
		// table end
		
		panel1_left1.add( recipeScrollPanel );
		
		
		// right side
		
		//title
		titlePanel = new JPanel();
		titlePanel.setLayout( new FlowLayout( FlowLayout.LEFT ) );
				
		label = new JLabel( "Izveleta recepte:" );
		titlePanel.add( label );
		panel1_right1.add( titlePanel );
		//title end
		
		JTextArea recepi_text = new JTextArea( 1, 40 );
		panel1_right1.add( recepi_text );
		
		recepi_text.setFont( new Font( "Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 12 ) );
		recepi_text.setLineWrap( true );
		recepi_text.setWrapStyleWord(true);
		recepi_text.setMargin(new Insets(10,10,10,10));
		
		recepi_text.append( "Recepte: Pankukas\n\n" );
		recepi_text.append( "Kategorija: Pankukas\n\n" );
		recepi_text.append( "Apraksts: The code shown in bold illustrates how theselection is created. The caret is first set to the end of the complete word, then moved back to a position after the last character typed. The moveCaretPosition method not only moves the caret to a new position but also selects the text between the two positions. The completion task is implemented with the following code:" );
		
		//title
		titlePanel = new JPanel();
		titlePanel.setLayout( new FlowLayout( FlowLayout.LEFT ) );
		
		label = new JLabel( "Sastavdaljas:" );
		titlePanel.add( label );
		panel1_right1.add( titlePanel );
		
		
		/**
		 * 
		 * table
		 * 
		 */
		
		Object columnNames2[] = { "Sastavdalja", "Dadzums", "Mervieniba" };
		
		String[][] dataValues2 = new String[100][3];

		for( int iY = 0; iY < 100; iY++ ){
			for( int iX = 0; iX < 3; iX++ ){
				dataValues2[iY][iX] = "" + iX + "," + iY;
			}
		}
		
		// table
		JTable recipeTable2 = new JTable( dataValues2, columnNames2 ) {
	        private static final long serialVersionUID = 1L;

	        public boolean isCellEditable(int row, int column) {                
	                return false;               
	        };
	    };
		JScrollPane recipeScrollPanel2 = new JScrollPane();
		
		recipeTable2.setColumnSelectionAllowed( false );
		recipeTable2.setShowHorizontalLines( true );
		recipeTable2.setShowVerticalLines( false );

		
		recipeTable2.setSelectionForeground( Color.white );
		recipeTable2.setSelectionBackground( Color.red );
		recipeScrollPanel2 = new JScrollPane();
		recipeScrollPanel2 = recipeTable2.createScrollPaneForTable( recipeTable2 );
						
		tableDimension = new Dimension( 250, 125 );
						
		recipeTable2.setPreferredScrollableViewportSize( tableDimension );
		recipeTable2.setFillsViewportHeight( true );
		// table end
		
		panel1_right1.add( recipeScrollPanel2 );
		

		/*
		 * 
		 * panel 2
		 * 
		 */
		
		JPanel panel2_left = new JPanel();
		panel2_left.setLayout( new BoxLayout( panel2_left, BoxLayout.PAGE_AXIS ));
		
		
		panel_2.add( panel2_left );
		
		//title
		titlePanel = new JPanel();
		titlePanel.setLayout( new FlowLayout( FlowLayout.LEFT ) );
						
		label = new JLabel( "Involved Doctors:" );
		titlePanel.add( label );
		panel2_left.add( titlePanel );
		//title end
				
		// table
		JTable doctorTable = new JTable( dataValues, columnNames );
		JScrollPane doctorScrollPanel = new JScrollPane();
					
		doctorTable.setColumnSelectionAllowed( false );
		doctorTable.setShowHorizontalLines( true );
		doctorTable.setShowVerticalLines( false );
								 
		doctorTable.setSelectionForeground( Color.white );
		doctorTable.setSelectionBackground( Color.red );
		doctorScrollPanel = new JScrollPane();
		doctorScrollPanel = doctorTable.createScrollPaneForTable( doctorTable );
												
		tableDimension = new Dimension( 500, 150 );
									
		doctorTable.setPreferredScrollableViewportSize( tableDimension );
		doctorTable.setFillsViewportHeight( true );
		// table end
								
		panel2_left.add( doctorScrollPanel );
		

		/*
		 * 
		 * panel 3
		 * 
		 */
		
		
		
		
		
		/*
		 * 
		 * footer
		 * 
		 */
		frame.pack();
		frame.setMinimumSize( frame.getSize() );
		frame.setVisible(true);
		
	}// initializeGUI end

	
}
