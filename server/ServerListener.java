package server;

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

        if( object instanceof ActionData ) {
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
        			break;
        		}
        		case ConfActions.ActDelIngredientData :{
        			CoreServer.showThis( "ActDelIngredientData pieprasijums" );
        			break;
        		}
        		
        	}
        }
     }

}
