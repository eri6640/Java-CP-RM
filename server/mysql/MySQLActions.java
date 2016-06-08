package server.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import both.classess.Ingredient;
import both.classess.Recipe;
import server.CoreServer;

public class MySQLActions {
	
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
					int value = result.getInt( "value" );
					String measurement = result.getString( "measurement" );
					
					CoreServer.RecipeStorage.addRecipeIngredient( new Ingredient( ingredient_id, recipe_id, ingredient, value, measurement ) );
	            }
				
				ps.close();
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		try {
			Connection.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		CoreServer.showThis( "Loaded " + ingredients + " ingredients!" );
		//CoreServer.showThis( "1 " + CoreServer.RecipeStorage.getRecipe( 1 ).ingredientSize() );
		
		
		//
		try {
			Connection.close();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

	}
	

}
