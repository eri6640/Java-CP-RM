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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
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
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import both.ReadWrite.ReadWriteActions;
import both.classess.ActionData;
import both.classess.CategoryArray;
import both.classess.Ingredient;
import both.classess.Recipe;
import both.classess.RecipesArray;
import both.conf.ConfActions;
import both.conf.ConfLang;

import com.esotericsoftware.kryonet.Client;

public class CoreClient {
	
	public static int ActionID = 0;
	private static JFrame frame;
	private static Client client;
	private static JFileChooser chooser;
	private static File selectedFile;
	
	public static RecipesArray RecipeStorage = new RecipesArray();
	public static CategoryArray CategoryStorage = new CategoryArray();
	
	public static int SelectedRecipeID = 0;

	public static void main( String[] args ){
		
		client = new Client();
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
	    client.getKryo().register( both.classess.CategoryArray.class );
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
		JOptionPane.showMessageDialog( frame, msg, title, JOptionPane.WARNING_MESSAGE );
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
		panel_2.setLayout( new BoxLayout( panel_2, BoxLayout.PAGE_AXIS ));
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
		
		
		loadPanel1Left1( panel1_left1, panel1_right1 );
		
		
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
		JPanel panel2_panel2 = new JPanel();
		panel2_panel2.setLayout( new BoxLayout( panel2_panel2, BoxLayout.PAGE_AXIS ));
		panel2_panel2.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel_2.add( panel2_panel2 );
		
		loadPanel2( panel2_panel, panel2_panel2 );
		

		/*
		 * 
		 * panel 3
		 * 
		 */
		
		JPanel panel3_panel = new JPanel();
		panel_3.add( panel3_panel );
		
		loadPanel3( panel3_panel );
		/*
		 * 
		 * panel 4
		 * 
		 */
		
		JPanel panel4_panel = new JPanel();
		panel_4.add( panel4_panel );
		
		JButton chooserB = new JButton("Export directory");
		chooser = new JFileChooser(); 
		chooserB.addActionListener( new ActionListener(){
	   		@Override
	   		public void actionPerformed( ActionEvent event ) {
	   			JFileChooser chooser = new JFileChooser();
	   			chooser.setCurrentDirectory(new java.io.File("."));
	   			String choosertitle = "Chooser";
	   		    chooser.setDialogTitle(choosertitle);
	   		    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	   		    
	   		    chooser.setAcceptAllFileFilterUsed(false);
	   		    int returnValue = chooser.showOpenDialog(null);
		        if (returnValue == JFileChooser.APPROVE_OPTION) {
		        	selectedFile = chooser.getSelectedFile();
		        }
	   		}
		});
		panel4_panel.add(chooserB);
		
		JButton chooserBB = new JButton("Export");
		chooserBB.addActionListener( new ActionListener(){
	   		@Override
	   		public void actionPerformed( ActionEvent event ) {
	   			if( selectedFile != null ){
	   				showThis( selectedFile.getPath() );
		   			try {
		   				ReadWriteActions.create( CategoryStorage, selectedFile.getPath() + "/saved_data.txt" );
		   			} catch (FileNotFoundException e) {
		   				e.printStackTrace();
		   			} catch (UnsupportedEncodingException e) {
		   				e.printStackTrace();
		   			}
	   			}
	   			else showWarning( "Warning", "Nav izveleta dir" );
	   		}
		});
		panel4_panel.add(chooserBB);
		
		
		///
		JPanel panel4_panel2 = new JPanel();
		panel_4.add( panel4_panel2 );
		
		JButton chooserBA = new JButton("Import file");
		chooser = new JFileChooser(); 
		chooserBA.addActionListener( new ActionListener(){
	   		@Override
	   		public void actionPerformed( ActionEvent event ) {
	   			JFileChooser chooser = new JFileChooser();
	   			chooser.setCurrentDirectory(new java.io.File("."));
	   			String choosertitle = "Chooser";
	   		    chooser.setDialogTitle(choosertitle);
	   		    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	   		    
	   		    chooser.setAcceptAllFileFilterUsed(false);
	   		    int returnValue = chooser.showOpenDialog(null);
		        if (returnValue == JFileChooser.APPROVE_OPTION) {
		        	selectedFile = chooser.getSelectedFile();
		        }
	   		}
		});
		panel4_panel2.add(chooserBA);
		
		JButton chooserBBA = new JButton("Import");
		chooserBBA.addActionListener( new ActionListener(){
	   		@Override
	   		public void actionPerformed( ActionEvent event ) {
	   			if( selectedFile != null ){
	   				showThis( selectedFile.getPath() );
	   				try {
						client.sendTCP( ReadWriteActions.read( selectedFile ) );
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	   			}
	   			else showWarning( "Warning", "Nav izvelets fails" );
	   		}
		});
		panel4_panel2.add(chooserBBA);
	
	
		
		
		
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
				
				if( ConfLang.Pane_Recepes.equals( sourceTabbedPane.getTitleAt(index) ) ){
					loadPanel1Left1( panel1_left1, panel1_right1 );
					loadPanel1Right1( panel1_right1 );
				}
				if( ConfLang.Pane_RecepeEdit.equals( sourceTabbedPane.getTitleAt(index) ) ){
					loadPanel2( panel2_panel, panel2_panel2 );
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
			
			recepi_text.append( "Recepte: " + recipe.getName() + "\n\n" );
			recepi_text.append( "Kategorija: " + CategoryStorage.getCategory( recipe.getCID() ) + "\n\n" );
			recepi_text.append( "Apraksts: " + recipe.getRecipe() );
			
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
	public void loadPanel1Left1( JPanel panel1_left1, JPanel panel1_right1 ){
		
		panel1_left1.removeAll();
		
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
					tableRecipes[ tmp ][ 1 ] = CategoryStorage.getCategory( recipe.getCID() ) + "";
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
			   			if( recipeTable.getSelectedRows().length == 1 ){
				   			int selectedRows[] = recipeTable.getSelectedRows();
				   			//for ( int row_id : selectedRows ){
				   			String selectedId = (String) recipeTable.getModel().getValueAt( selectedRows[0], 0 );
				   			SelectedRecipeID = Integer.parseInt( selectedId.split( "-" )[0] );
				   				
				   			loadPanel1Right1( panel1_right1 );
				   				
					   		showThis( selectedId );
				   			//}			   				
			   			}
			   			else{
			   				showWarning( "Warning", "Atlasishana bija neveiksmiga!\nVar atlasit tikai vienu recepti." );
			   			}
			   		    
			   		}
			   	});
	}

	public void loadPanel2( JPanel panel2_panel, JPanel panel2_panel2 ){
		JTextField field_name;
		JComboBox field_category;
		JTextArea field_recipe;
		//ingr
		JTextField field_ing;
		JTextField field_ing_val;
		JTextField field_ing_mes;
		
		panel2_panel.removeAll();
		panel2_panel2.removeAll();
		
		if( RecipeStorage.getRecipe( SelectedRecipeID ) == null ){
			//title
			titlePanel = new JPanel();
			titlePanel.setLayout( new FlowLayout( FlowLayout.LEFT ) );
					
			label = new JLabel( "Receptes laboshana:" );
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
		    
		    String[] category_list = CategoryStorage.getList().values().toArray(new String[0]);
		    
		    panel2_panel.add( field_category = new JComboBox<String>( category_list ), gbContainer);
		    
		    int tmp_selected = 0;
		    for( String string : category_list ){
		    	if( string.equalsIgnoreCase( CategoryStorage.getCategory( recipe.getCID() ) ) ){
		    		break;
		    	}
		    	else tmp_selected++;
		    }
		    
		    field_category.setSelectedIndex( tmp_selected );
		    
		    panel2_panel.add( field_recipe = new JTextArea( 1, 40 ), gbContainer);
		    field_recipe.setFocusAccelerator('c');
		    
		    field_recipe.setFont( new Font( "Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 12 ) );
		    field_recipe.setLineWrap( true );
		    field_recipe.setWrapStyleWord(true);
		    field_recipe.setMargin(new Insets(10,10,10,10));
			
		    field_name.setText( recipe.getName() );
		    field_recipe.append( recipe.getRecipe() );
		    
		    gbContainer.weightx = 0.0;
		    gbContainer.fill = GridBagConstraints.NONE;
		    
		    
		    
		    
		    /**
		     * 
		     * ingredient tab
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
			
		    JTable recipeTable3 = new JTable( tableIngredients, columnNames2 ) {
		        private static final long serialVersionUID = 1L;
				
		        public boolean isCellEditable(int row, int column) {                
		                return false;               
		        };
		    };
		    
		    if( ! recipe.getIngredients().isEmpty() ){
				//title
		    	JPanel panelIngredtitle = new JPanel();
			    titlePanel = new JPanel();
				titlePanel.setLayout( new FlowLayout( FlowLayout.LEFT ) );
				
				label = new JLabel( "Sastavdaljas:" );
				titlePanel.add( label );
				panelIngredtitle.add( titlePanel );
			    panel2_panel2.add( panelIngredtitle );
		    	
		    	//
				
				
				/**
				 * 
				 * table
				 * 
				 */
				
				
				// table
				
				JScrollPane recipeScrollPanel3 = new JScrollPane();
				
				recipeTable3.setColumnSelectionAllowed( false );
				recipeTable3.setShowHorizontalLines( true );
				recipeTable3.setShowVerticalLines( false );
		
				
				recipeTable3.setSelectionForeground( Color.white );
				recipeTable3.setSelectionBackground( Color.red );
				recipeScrollPanel3 = new JScrollPane();
				recipeScrollPanel3 = recipeTable3.createScrollPaneForTable( recipeTable3 );
								
				tableDimension = new Dimension( (int)panel2_panel.getSize().getWidth(), 125 );
								
				recipeTable3.setPreferredScrollableViewportSize( tableDimension );
				recipeTable3.setFillsViewportHeight( true );
				// table end
				
				panel2_panel2.add( recipeScrollPanel3 );
				
				
			}
		    
		    //add ingredient
		    
		    JPanel panelAddIngredtitle = new JPanel();
		    titlePanel = new JPanel();
			titlePanel.setLayout( new FlowLayout( FlowLayout.LEFT ) );
			
			label = new JLabel( "Sastavdalju pievienoshana:" );
			titlePanel.add( label );
			panelAddIngredtitle.add( titlePanel );
		    panel2_panel2.add( panelAddIngredtitle );
		    
		    JPanel panelAddIngred = new JPanel();
		    panel2_panel2.add( panelAddIngred );
		    panelAddIngred.setLayout(new GridBagLayout());
		    GridBagConstraints gbContainer2 = new GridBagConstraints();
	
		    gbContainer2.gridx = 0;
		    gbContainer2.gridy = GridBagConstraints.RELATIVE;
		    gbContainer2.gridwidth = 1;
		    gbContainer2.gridheight = 1;
		    gbContainer2.insets = new Insets(3, 20, 3, 20);
		    gbContainer2.anchor = GridBagConstraints.EAST;
	
		    panelAddIngred.add(label = new JLabel("Nosaukums:", SwingConstants.RIGHT), gbContainer2);
		    label.setDisplayedMnemonic('n');
		    panelAddIngred.add(label = new JLabel("Daudzums:", SwingConstants.RIGHT), gbContainer2);
		    label.setDisplayedMnemonic('h');
		    panelAddIngred.add(label = new JLabel("Mervieniba:", SwingConstants.RIGHT), gbContainer2);
		    label.setDisplayedMnemonic('c');
	
		    gbContainer2.gridx = 1;
		    gbContainer2.gridy = 0;
		    gbContainer2.weightx = 1.0;
		    gbContainer2.fill = GridBagConstraints.HORIZONTAL;
		    gbContainer2.anchor = GridBagConstraints.CENTER;
	
		    panelAddIngred.add( field_ing = new JTextField(35), gbContainer2);
		    field_ing.setFocusAccelerator('n');
		    gbContainer2.gridx = 1;
		    gbContainer2.gridy = GridBagConstraints.RELATIVE;
		    
		    panelAddIngred.add( field_ing_val = new JTextField(35), gbContainer2);
		    field_ing_val.setFocusAccelerator('c');
		    panelAddIngred.add( field_ing_mes = new JTextField(35), gbContainer2);
		    field_ing_mes.setFocusAccelerator('g');
		    
		    gbContainer2.weightx = 0.0;
		    gbContainer2.fill = GridBagConstraints.NONE;
		    
		    
		    
		    
		    /*
		     * 
		     * pogas
		     * 
		     */
		    
		    JPanel buttonPanel = new JPanel();
		    panel2_panel2.add( buttonPanel );
		    /*
		     * SAVE SAVE SAVE
		     */
		    JButton submitButton = new JButton("Save Recipe");
		    submitButton.setMargin( new Insets(2,10,2,10) );

		    buttonPanel.add( submitButton );
		    
		    submitButton.addActionListener( new ActionListener(){
		   		@Override
		   		public void actionPerformed( ActionEvent event ) {
		   			if( field_name.getText().isEmpty() || field_recipe.getText().isEmpty() ){
		   				showThis( "Visiem laukiem jabut aizpilditiem" );
		   				showWarning( "Warning", "Visiem laukiem jabut aizpilditiem!" );
		   			}
		   			else if( CategoryStorage.getCategory( category_list[ field_category.getSelectedIndex() ] ) == 0 ){
		   				showThis( "Problemas ar kategorijas noteikshanu!" );
		   				showWarning( "Warning", "Problemas ar kategorijas noteikshanu!" );
		   			}
		   			else{
		   				
		   				recipe.setName( field_name.getText() );
		   				recipe.setCategory( CategoryStorage.getCategory( category_list[ field_category.getSelectedIndex() ] ) );
		   				recipe.setRecipe( field_recipe.getText() );
		   				
		   				client.sendTCP( new ActionData( getActID(), ConfActions.ActEditRecipeData, recipe ) );
		   				loadPanel2( panel2_panel, panel2_panel2 );
		   			}
		   		}
		   	});
		    /*
		     * ADD ADD
		     */
		    JButton submitButton0 = new JButton("Add Ingredient");
		    submitButton0.setMargin( new Insets(2,10,2,10) );

		    buttonPanel.add( submitButton0 );
		    
		    submitButton0.addActionListener( new ActionListener(){
		   		@Override
		   		public void actionPerformed( ActionEvent event ) {
		   			if( field_ing.getText().isEmpty() || field_ing_val.getText().isEmpty() || field_ing_mes.getText().isEmpty() ){
		   				showThis( "Visiem laukiem jabut aizpilditiem" );
		   				showWarning( "Warning", "Visiem laukiem jabut aizpilditiem!" );
		   			}
		   			else{		   				
		   				Ingredient ingredient = new Ingredient( 0, recipe.getRID(), field_ing.getText(), field_ing_val.getText(),field_ing_mes.getText() );
		   				client.sendTCP( new ActionData( getActID(), ConfActions.ActAddIngredientData, ingredient ) );
		   				loadPanel2( panel2_panel, panel2_panel2 );
		   			}
		   		}
		   	});
		    
		    //

		    /*
		     * RESET RESET RESET
		     */
		    JButton submitButton3 = new JButton("Reset");
		    submitButton.setMargin( new Insets(2,10,2,10) );

		    buttonPanel.add( submitButton3 );
		    
		    submitButton3.addActionListener( new ActionListener(){
		   		@Override
		   		public void actionPerformed( ActionEvent event ) {
		   			loadPanel2( panel2_panel, panel2_panel2 );
		   		}
		   	});
		    
		    //
		    
		    JButton submitButton4 = new JButton("Del Recipe");
		    submitButton.setMargin( new Insets(2,10,2,10) );

		    buttonPanel.add( submitButton4 );
		    
		    submitButton4.addActionListener( new ActionListener(){
		   		@Override
		   		public void actionPerformed( ActionEvent event ) {
		   			client.sendTCP( new ActionData( getActID(), ConfActions.ActDelRecipeData, recipe ) );
		   			loadPanel2( panel2_panel, panel2_panel2 );
		   		}
		   	});
		    
		    //
		    if( ! recipe.getIngredients().isEmpty() ){
			    JButton submitButton5 = new JButton("Del Selected Ingredients");
			    submitButton.setMargin( new Insets(2,10,2,10) );
	
			    buttonPanel.add( submitButton5 );
			    
			    submitButton5.addActionListener( new ActionListener(){
			   		@Override
			   		public void actionPerformed( ActionEvent event ) {
			   			if( recipeTable3.getSelectedRows().length > 0 ){
				   			int selectedRows[] = recipeTable3.getSelectedRows();
				   			ArrayList<Integer> ingredientDelList = new ArrayList<Integer>();
				   			for ( int row_id : selectedRows ){
					   			String selectedIngr = (String) recipeTable3.getModel().getValueAt( row_id, 0 );
					   			
					   			for( Ingredient ingredient : recipe.getIngredients() ){
					   				if( ingredient.getIngredient().equals( selectedIngr ) ){
					   					ingredientDelList.add( ingredient.getIID() );
					   					showThis( ""+ ingredient.getIID());
					   				}
					   			}
					   			
					   				
				   			}
				   			client.sendTCP( new ActionData( getActID(), ConfActions.ActDelIngredientData, recipe, null, ingredientDelList ) );
				   			loadPanel2( panel2_panel, panel2_panel2 );
			   			}
			   			else{
			   				showWarning( "Warning", "Atlasishana bija neveiksmiga!\nVar atlasit tikai vienu recepti." );
			   			}
			   		}
			   	});
		    }
		    
		    
		}
	}
	public void loadPanel3( JPanel panel3_panel){
		JTextField field_name;
		JComboBox field_category;
		JTextArea field_recipe;
		
		panel3_panel.removeAll();
		
		panel3_panel.setLayout(new GridBagLayout());
	    GridBagConstraints gbContainer = new GridBagConstraints();

	    gbContainer.gridx = 0;
	    gbContainer.gridy = GridBagConstraints.RELATIVE;
	    gbContainer.gridwidth = 1;
	    gbContainer.gridheight = 1;
	    gbContainer.insets = new Insets(3, 20, 3, 20);
	    gbContainer.anchor = GridBagConstraints.EAST;

	    panel3_panel.add(label = new JLabel("Nosaukums:", SwingConstants.RIGHT), gbContainer);
	    label.setDisplayedMnemonic('n');
	    panel3_panel.add(label = new JLabel("Kategorija:", SwingConstants.RIGHT), gbContainer);
	    label.setDisplayedMnemonic('h');
	    panel3_panel.add(label = new JLabel("Recepte:", SwingConstants.RIGHT), gbContainer);
	    label.setDisplayedMnemonic('c');

	    gbContainer.gridx = 1;
	    gbContainer.gridy = 0;
	    gbContainer.weightx = 1.0;
	    gbContainer.fill = GridBagConstraints.HORIZONTAL;
	    gbContainer.anchor = GridBagConstraints.CENTER;

	    panel3_panel.add( field_name = new JTextField(35), gbContainer);
	    field_name.setFocusAccelerator('n');
	    gbContainer.gridx = 1;
	    gbContainer.gridy = GridBagConstraints.RELATIVE;
	    
	    String[] category_list = CategoryStorage.getList().values().toArray(new String[0]);
	    
	    panel3_panel.add( field_category = new JComboBox<String>( category_list ), gbContainer);
	   
	    field_category.setSelectedIndex( 0 );
	    
	    panel3_panel.add( field_recipe = new JTextArea( 1, 40 ), gbContainer);
	    field_recipe.setFocusAccelerator('c');
	    
	    field_recipe.setFont( new Font( "Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 12 ) );
	    field_recipe.setLineWrap( true );
	    field_recipe.setWrapStyleWord(true);
	    field_recipe.setMargin(new Insets(10,10,10,10));
	    
	    gbContainer.weightx = 0.0;
	    gbContainer.fill = GridBagConstraints.NONE;

	    
	    
	    
	    
	    /*
	     * 
	     * pogas
	     * 
	     */
	    
	    JPanel buttonPanel = new JPanel();
	    panel3_panel.add( buttonPanel );
	    /*
	     * SAVE SAVE SAVE
	     */
	    JButton submitButton = new JButton("Add Recipe");
	    submitButton.setMargin( new Insets(2,10,2,10) );

	    buttonPanel.add( submitButton );
	    
	    submitButton.addActionListener( new ActionListener(){
	   		@Override
	   		public void actionPerformed( ActionEvent event ) {
	   			if( field_name.getText().isEmpty() || field_recipe.getText().isEmpty() ){
	   				showThis( "Visiem laukiem jabut aizpilditiem" );
	   				showWarning( "Warning", "Visiem laukiem jabut aizpilditiem!" );
	   			}
	   			else if( CategoryStorage.getCategory( category_list[ field_category.getSelectedIndex() ] ) == 0 ){
	   				showThis( "Problemas ar kategorijas noteikshanu!" );
	   				showWarning( "Warning", "Problemas ar kategorijas noteikshanu!" );
	   			}
	   			else{
	   				
	   				Recipe recipe = new Recipe( CategoryStorage.getCategory( category_list[ field_category.getSelectedIndex() ] ), field_name.getText(), field_recipe.getText() );
	   				client.sendTCP( new ActionData( getActID(), ConfActions.ActAddRecipeData, recipe ) );
	   				loadPanel3( panel3_panel );
	   				
	   			}
	   		}
	   	});
	}
}
