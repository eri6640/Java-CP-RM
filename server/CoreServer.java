package server;

import java.io.IOException;
import server.mysql.MySQL;
import server.mysql.MySQLActions;
import both.classess.CategoryArray;
import both.classess.RecipesArray;

import com.esotericsoftware.kryonet.Server;

public class CoreServer{
	
	private static Server server;
	public static MySQL MySQL;
	
	public static RecipesArray RecipeStorage = new RecipesArray();
	public static CategoryArray CategoryStorage = new CategoryArray();
	
	
	public static void main( String[] args ){
		    
	    String db_localhost = "46.105.57.245";
		String db_port = "3306";
		String db_database = "101_java_cp";
		String db_username = "101_java";
		String db_password = "a5665f8egfs";
		
		MySQL = new MySQL( db_localhost, db_port, db_database, db_username, db_password );
		
		if( MySQLActions.tryConnection() ) showThis( "MySQL connection enstablished." );
		else{
			showThis( "MySQL connection failed." );
			return;
		}
		
		server = new Server();
	    server.start();
	    try {
	    	// TCP port 54555 and UDP port 54777
			server.bind(54555, 54777);
		} catch (IOException e) {
			e.printStackTrace();
			showThis( "Server stopped" );
			return;
		}
	    
	    //server.getKryo().setRegistrationRequired(true);
		server.getKryo().register( both.classess.ActionData.class );
		server.getKryo().register( both.classess.RecipesArray.class );
		server.getKryo().register( both.classess.Recipe.class );
		server.getKryo().register( both.classess.Ingredient.class );
		server.getKryo().register( both.classess.CategoryArray.class );
		server.getKryo().register(java.util.HashMap.class);
		server.getKryo().register(java.util.ArrayList.class);
	    
	    showThis( "Server started" );
	    /*
	     * 
	     * SERVER STARTED
	     * 
	     */
	    
	    
	    server.addListener( new ServerListener() );
	    
	    //ScheduledExecutorService executorService = Executors.newScheduledThreadPool( 1 );
	    //executorService.scheduleAtFixedRate( new DataRunnable(), 1, 3, TimeUnit.SECONDS );
	    
	    MySQLActions.loadAll();
	    
	    
	}
	
	//
	public static void showThis( String string ){
		System.out.println( string );
	}

}
