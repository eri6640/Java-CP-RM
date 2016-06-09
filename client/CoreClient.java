package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import server.ServerListener;
import both.classess.ActionData;
import both.classess.Ingredient;
import both.classess.Recipe;
import both.classess.RecipesArray;
import both.conf.ConfActions;
import both.conf.ConfLang;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class CoreClient {
	
	public static int ActionID = 0;
	private static JFrame frame;
	public static RecipesArray RecipeStorage = new RecipesArray();
	public static int SelectedRecipeID = 1;

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
	
	public static void showWarning( String title, String msg ){
		JOptionPane.showMessageDialog( frame, title, msg, JOptionPane.WARNING_MESSAGE );
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
		tabbedPane.addTab( ConfLang.Pane_Recepes, null, panel_1, null );
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab( ConfLang.Pane_RecepeEdit, null, panel_2, null );
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab( ConfLang.Pane_RecepesAdd, null, panel_3, null );
		
		JPanel panel_4 = new JPanel();
		tabbedPane.addTab( ConfLang.Pane_ExImport, null, panel_4, null );
		
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
			tableRecipes[ tmp ][ 0 ] = recipe.getRID() + "-" + recipe.getName();
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
		
		JButton selectRecipeButton = new JButton("Select");
		selectRecipeButton.setVerticalAlignment(SwingConstants.BOTTOM);
		panel1_left1.add( selectRecipeButton );
		
		selectRecipeButton.addActionListener( new ActionListener(){
	   		@Override
	   		public void actionPerformed( ActionEvent event ) {
	   			int selectedRows[] = recipeTable.getSelectedRows();
	   			for ( int row_id : selectedRows ){
	   				String selectedId = (String) recipeTable.getModel().getValueAt( row_id, 0 );
	   				SelectedRecipeID = Integer.parseInt( selectedId.split( "-" )[0] );
	   				
	   				loadPanel1Right1( panel1_right1 );
	   				
		   		    showThis( selectedId );
	   			}
	   		    
	   		}
	   	});
		
		
		/*
		 * 
		 * panel1_right1
		 * 
		 */
		
		loadPanel1Right1( panel1_right1 );

		/*
		 * 
		 * panel 2
		 * 
		 */
		JPanel panel2_panel = new JPanel();		
		panel_2.add( panel2_panel );
		
		loadPanel2( panel2_panel );
		

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
		
		tabbedPane.addChangeListener( new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent changeEvent) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
				int index = sourceTabbedPane.getSelectedIndex();
				
				if( ConfLang.Pane_RecepeEdit.equals( sourceTabbedPane.getTitleAt(index) ) ){
					loadPanel2( panel2_panel );
				}
				
			}
		});
		
		
		frame.pack();
		frame.setMinimumSize( frame.getSize() );
		frame.setVisible(true);
		
	}// initializeGUI end
	
	
	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	public void loadPanel1Right1( JPanel panel1_right1 ){

		panel1_right1.removeAll();
		
		if( RecipeStorage.getRecipe( SelectedRecipeID ) == null ){
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
			recepi_text.setEditable( false );
			recepi_text.setMargin(new Insets(10,10,10,10));
			
			recepi_text.append( "Nav izveleta neviena recepte vai ari netika atrasta izveleta recepte!" );
			
		}
		else{
			
			Recipe recipe = RecipeStorage.getRecipe( SelectedRecipeID );
		
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
			recepi_text.setEditable( false );
			recepi_text.setMargin(new Insets(10,10,10,10));
			
			recepi_text.append( "Recepte: Pankukas\n\n" );
			recepi_text.append( "Kategorija: Pankukas\n\n" );
			recepi_text.append( "Apraksts: The code shown in bold illustrates how theselection is created. The caret is first set to the end of the complete word, then moved back to a position after the last character typed. The moveCaretPosition method not only moves the caret to a new position but also selects the text between the two positions. The completion task is implemented with the following code:" );
			
			if( ! recipe.getIngredients().isEmpty() ){
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
				
				
				String[][] tableIngredients = new String[ recipe.getIngredients().size() ][3];
				int tmp = 0;
				for( Ingredient ingredient : recipe.getIngredients() ){
					tableIngredients[ tmp ][ 0 ] = ingredient.getIngredient();
					tableIngredients[ tmp ][ 1 ] = ingredient.getValue() + "";
					tableIngredients[ tmp ][ 2 ] = ingredient.getMeasurement();
					tmp++;
				}
				
				// table
				JTable recipeTable2 = new JTable( tableIngredients, columnNames2 ) {
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
			}
		}//end else
	}

	public void loadPanel2( JPanel panel2_panel ){
		JTextField field_name;
		JTextField field_category;
		JTextArea field_recipe;
		
		panel2_panel.removeAll();
		
		if( RecipeStorage.getRecipe( SelectedRecipeID ) == null ){
			//title
			titlePanel = new JPanel();
			titlePanel.setLayout( new FlowLayout( FlowLayout.LEFT ) );
					
			label = new JLabel( "Izveleta recepte:" );
			titlePanel.add( label );
			panel2_panel.add( titlePanel );
			//title end
			
			JTextArea recepi_text = new JTextArea( 1, 40 );
			panel2_panel.add( recepi_text );
			
			recepi_text.setFont( new Font( "Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 12 ) );
			recepi_text.setLineWrap( true );
			recepi_text.setWrapStyleWord(true);
			recepi_text.setEditable( false );
			recepi_text.setMargin(new Insets(10,10,10,10));
			
			recepi_text.append( "Nav izveleta neviena recepte vai ari netika atrasta izveleta recepte!" );
			
		}
		else{
			
			Recipe recipe = RecipeStorage.getRecipe( SelectedRecipeID );
		
			panel2_panel.setLayout(new GridBagLayout());
		    GridBagConstraints gbContainer = new GridBagConstraints();
	
		    gbContainer.gridx = 0;
		    gbContainer.gridy = GridBagConstraints.RELATIVE;
		    gbContainer.gridwidth = 1;
		    gbContainer.gridheight = 1;
		    gbContainer.insets = new Insets(3, 20, 3, 20);
		    gbContainer.anchor = GridBagConstraints.EAST;
	
		    panel2_panel.add(label = new JLabel("Nosaukums:", SwingConstants.RIGHT), gbContainer);
		    label.setDisplayedMnemonic('n');
		    panel2_panel.add(label = new JLabel("Kategorija:", SwingConstants.RIGHT), gbContainer);
		    label.setDisplayedMnemonic('h');
		    panel2_panel.add(label = new JLabel("Recepte:", SwingConstants.RIGHT), gbContainer);
		    label.setDisplayedMnemonic('c');
	
		    gbContainer.gridx = 1;
		    gbContainer.gridy = 0;
		    gbContainer.weightx = 1.0;
		    gbContainer.fill = GridBagConstraints.HORIZONTAL;
		    gbContainer.anchor = GridBagConstraints.CENTER;
	
		    panel2_panel.add( field_name = new JTextField(35), gbContainer);
		    field_name.setFocusAccelerator('n');
		    gbContainer.gridx = 1;
		    gbContainer.gridy = GridBagConstraints.RELATIVE;
		    panel2_panel.add( field_category = new JTextField(35), gbContainer);
		    field_category.setFocusAccelerator('h');
		    panel2_panel.add( field_recipe = new JTextArea( 1, 40 ), gbContainer);
		    field_recipe.setFocusAccelerator('c');
		    
		    field_recipe.setFont( new Font( "Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 12 ) );
		    field_recipe.setLineWrap( true );
		    field_recipe.setWrapStyleWord(true);
		    field_recipe.setMargin(new Insets(10,10,10,10));
			
		    field_name.setText( recipe.getRecipe() );
		    field_category.setText( recipe.getRecipe() );
		    field_recipe.append( recipe.getRecipe() );
		    
		    gbContainer.weightx = 0.0;
		    gbContainer.fill = GridBagConstraints.NONE;
		    
		    JButton submitButton = new JButton("Submit");
		    submitButton.setMargin( new Insets(2,10,2,10) );
		    
		    
		    //set values
		    field_name.setText( "Value" );
		    
		}
	}
}
