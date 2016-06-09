package server;

import java.util.ArrayList;

import server.mysql.MySQLActions;
import both.classess.ActionData;
import both.classess.Ingredient;
import both.classess.Recipe;
import both.conf.ConfActions;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ServerListener extends Listener {
	
	@Override
	public void received (Connection connection, Object object) {

		if( object instanceof ArrayList){
			ArrayList<String> categ = (ArrayList<String>)object;
			MySQLActions.addCategories(categ);
			connection.sendTCP( CoreServer.CategoryStorage );
		}
		else if( object instanceof ActionData ) {
        	ActionData data = (ActionData)object;

        	switch( data.getAction() ){
        		case ConfActions.ActGetData :{
        			CoreServer.showThis( "ActGetData pieprasijums" );
        			data.setSuccess();
        			connection.sendTCP( CoreServer.RecipeStorage );
        			connection.sendTCP( CoreServer.CategoryStorage );
        			connection.sendTCP( data );
        			break;
        		}
        		case ConfActions.ActAddRecipeData :{
        			CoreServer.showThis( "ActAddRecipeData pieprasijums" );
        			Recipe recipe = data.getRecipe();
        			int index = 0;
        			if( ( index = MySQLActions.addRecipe( recipe ) ) > 0 ){
        				recipe.setID( index );
        				
        				CoreServer.RecipeStorage.addRecipe( recipe );

            			data.setSuccess();
            			connection.sendTCP( CoreServer.RecipeStorage );
            			connection.sendTCP( CoreServer.CategoryStorage );
            			connection.sendTCP( data );
        			}
        			else connection.sendTCP( data );
        			break;
        		}
        		case ConfActions.ActAddIngredientData :{
        			CoreServer.showThis( "ActAddIngredientData pieprasijums" );
        			Ingredient ingredient = data.getIngredient();
        			int index = 0;
        			if( ( index = MySQLActions.addIngredient( ingredient ) ) > 0 ){
        				ingredient.setId( index );
        				CoreServer.RecipeStorage.getRecipe( ingredient.getRID() ).addIngredient( ingredient );

            			data.setSuccess();
            			connection.sendTCP( CoreServer.RecipeStorage );
            			connection.sendTCP( data );
        			}
        			break;
        		}
        		case ConfActions.ActEditRecipeData :{
        			CoreServer.showThis( "ActEditRecipeData pieprasijums" );
        			Recipe recipe = data.getRecipe();
        			if( CoreServer.RecipeStorage.replace( recipe ) && MySQLActions.updateRecipe( recipe ) ){

            			data.setSuccess();
            			connection.sendTCP( CoreServer.RecipeStorage );
            			connection.sendTCP( data );
        			}
        			break;
        		}
        		case ConfActions.ActDelRecipeData :{
        			CoreServer.showThis( "ActDelRecipeData pieprasijums" );
        			MySQLActions.deleteRecipe( data.getRecipe().getRID() );
        			CoreServer.RecipeStorage.delRecipe( data.getRecipe() );

        			MySQLActions.loadAll();
        			
        			data.setSuccess();
        			connection.sendTCP( CoreServer.RecipeStorage );
        			connection.sendTCP( data );
        			break;
        		}
        		case ConfActions.ActDelIngredientData :{
        			CoreServer.showThis( "ActDelIngredientData pieprasijums" );
        			ArrayList<Integer> ingredientDelList = data.getIngredients();
        			
        			MySQLActions.deleteIngredients( ingredientDelList );
        			
        			MySQLActions.loadAll();

        			
        			data.setSuccess();
        			connection.sendTCP( CoreServer.RecipeStorage );
        			connection.sendTCP( data );
        			
        			break;
        		}
        		
        	}
        }
     }

}
