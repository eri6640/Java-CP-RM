package server.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import both.classess.Ingredient;
import both.classess.Recipe;
import server.CoreServer;

public class MySQLActions {

	private static String table_category = "cp_categories";
	private static String table_recipe = "cp_recipes";
	private static String table_ingredient = "cp_recipes_ingredients";
	
	public static boolean tryConnection(){
		return createConnection() != null ? true : false;
	}
	
	public static Connection createConnection(){
		return createConnection( "unknown" );
	}
	
	private static Connection createConnection( String string ){
		
		Connection Connection = null;
		try {
			Connection = CoreServer.MySQL.openConnection();
        } catch (Exception err) { 
        	CoreServer.showThis( string + " MySQL Connection error: " + err );
        }
		
		return Connection != null ? Connection : null;
	}
	
	/*
	 * 
	 * 
	 * 
	 */
	
	public static void loadAll() {
		
		Statement ps;
		Connection Connection = createConnection( "loadAll()" );
		
		CoreServer.RecipeStorage.clear();
		
		/*
		 * Recipes
		 */
		
		try {
			ps = (Statement) Connection.createStatement();
		
			String query = "SELECT * FROM `<table>`".replaceAll( "<table>", table_recipe );
			
			ResultSet result = ps.executeQuery( query );
			
			while( result.next() ) {
				
				int recipe_id = result.getInt( "id" );
				int category_id = result.getInt( "category_id" );
				String recipe_name = result.getString( "name" );
				String recipe_recipe = result.getString( "recipe" );
				int recipe_created = result.getInt( "created" );
				
				CoreServer.RecipeStorage.addRecipe( new Recipe( recipe_id, category_id, recipe_name, recipe_recipe, recipe_created ) );
				//CoreServer.showThis( "Recipe: " + recipe_id + " " + category_id + " " + recipe_name );
            }
			

			ps.close();	
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		CoreServer.showThis( "Loaded " + CoreServer.RecipeStorage.size() + " recipes!" );
		
		/*
		 * Ingredients
		 */
		
		int ingredients = 0;
		
		for( Recipe recipe : CoreServer.RecipeStorage.getList().values() ){
			try {
				ps = (Statement) Connection.createStatement();
			
				String query = "SELECT * FROM `<table>` WHERE `<where1>` = <rid>".replaceAll( "<table>", table_ingredient ).replaceAll( "<where1>", "recipe_id" ).replaceAll( "<rid>", ""+recipe.getRID() );
				
				ResultSet result = ps.executeQuery( query );
				
				while( result.next() ) {
					
					ingredients++;
					
					int ingredient_id = result.getInt( "id" );
					int recipe_id = result.getInt( "recipe_id" );
					String ingredient = result.getString( "ingredient" );
					String value = result.getString( "value" );
					String measurement = result.getString( "measurement" );
					
					CoreServer.RecipeStorage.addRecipeIngredient( new Ingredient( ingredient_id, recipe_id, ingredient, value, measurement ) );
	            }
				
				ps.close();
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		CoreServer.showThis( "Loaded " + ingredients + " ingredients!" );
		//CoreServer.showThis( "1 " + CoreServer.RecipeStorage.getRecipe( 1 ).ingredientSize() );
		
		CoreServer.CategoryStorage.clear();
		
		try {
			ps = (Statement) Connection.createStatement();
		
			String query = "SELECT * FROM `<table>`".replaceAll( "<table>", table_category );
			
			ResultSet result = ps.executeQuery( query );
			
			while( result.next() ) {
				
				int id = result.getInt( "id" );
				String category = result.getString( "category" );
				
				CoreServer.CategoryStorage.addCategory( id, category );
				
            }
			

			ps.close();	
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		CoreServer.showThis( "Loaded " + CoreServer.CategoryStorage.size() + " categories!" );
		
		
		//
		try {
			Connection.close();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

	}
	

	public static boolean updateRecipe( Recipe recipe ) {
		
		Connection Connection = createConnection( "updateRecipe()" );
		
		try {			
			String query = "UPDATE `<table>` SET `category_id` = ?, `name` = ?, `recipe` = ? WHERE `id` = ?;".replaceAll( "<table>", table_recipe );
	    	
	    	PreparedStatement preparedStatement = Connection.prepareStatement( query );
	    	
	    	preparedStatement.setInt( 1, recipe.getCID() );
	    	preparedStatement.setString( 2, recipe.getName() );
	    	preparedStatement.setString( 3, recipe.getRecipe() );
	    	preparedStatement.setInt( 4, recipe.getRID() );
	    	
	    	preparedStatement.executeUpdate();
	    	preparedStatement.close();
			
			Connection.close();
		
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		CoreServer.showThis( "bljup" );
		
		
		//
		try {
			Connection.close();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		return true;

	}
	public static int addIngredient( Ingredient ingredient ) {
		
		int index = -1;
		Connection Connection = createConnection( "updateRecipe()" );
		
		try {		
	    	String query = "INSERT INTO `<table>` (`id`, `recipe_id`, `ingredient`, `value`, `measurement`) VALUES (NULL, ?, ?, ?, ?);".replaceAll( "<table>", table_ingredient );
			
	    	PreparedStatement preparedStatement = Connection.prepareStatement( query, Statement.RETURN_GENERATED_KEYS );
	    	
	    	preparedStatement.setInt( 1, ingredient.getRID() );
	    	preparedStatement.setString( 2, ingredient.getIngredient() );
	    	preparedStatement.setString( 3, ingredient.getValue() );
	    	preparedStatement.setString( 4, ingredient.getMeasurement() );
	    	
	    	preparedStatement.executeUpdate();
	    	ResultSet rs = preparedStatement.getGeneratedKeys();
            if( rs.next() ) index = rs.getInt(1);
	    	
	    	preparedStatement.close();
			
			Connection.close();
		
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		
		CoreServer.showThis( "bljup " + index );
		
		
		//
		try {
			Connection.close();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		return index;

	}
	

	
	public static void deleteIngredients( ArrayList<Integer> ingredientDelList ) {
		
		Connection Connection = createConnection( "deleteIngredients()" );

		/*
		 * Ingredients
		 */
		
		for( int index : ingredientDelList ){
			try {
				String query = "DELETE FROM `<table>` WHERE `id` = ?;".replaceAll( "<table>", table_ingredient );
		    	
		    	PreparedStatement preparedStatement = Connection.prepareStatement( query );
		    	
		    	preparedStatement.setInt( 1, index );
		    	
		    	preparedStatement.executeUpdate();
		    	preparedStatement.close();
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		//
		try {
			Connection.close();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

	}
	

}
