package client;

import server.CoreServer;
import both.classess.ActionData;
import both.classess.RecipesArray;
import both.conf.ConfActions;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ClientListener extends Listener {
	
	@Override
	public void received (Connection connection, Object object) {
		if( object instanceof RecipesArray ){
			CoreClient.RecipeStorage = (RecipesArray)object;
			CoreServer.showThis( "Received RecipeStorage" );
		}
		else if( object instanceof ActionData ){
        	ActionData data = (ActionData)object;
        	
        	switch( data.getAction() ){
	    		case ConfActions.ActGetData :{
	    			CoreServer.showThis( "ActGetData atbilde" );
	    			if( data.isSuccess() ) CoreServer.showThis( "success" );
	    			else CoreServer.showThis( "! success" );
	    			break;
	    		}
	    		case ConfActions.ActAddRecipeData :{
	    			CoreServer.showThis( "ActAddRecipeData atbilde" );
	    			break;
	    		}
	    		case ConfActions.ActAddIngredientData :{
	    			CoreServer.showThis( "ActAddIngredientData atbilde" );
	    			break;
	    		}
	    		case ConfActions.ActEditRecipeData :{
	    			CoreServer.showThis( "ActEditRecipeData atbilde" );
	    			break;
	    		}
	    		case ConfActions.ActDelRecipeData :{
	    			CoreServer.showThis( "ActDelRecipeData atbilde" );
	    			break;
	    		}
	    		case ConfActions.ActDelIngredientData :{
	    			CoreServer.showThis( "ActDelIngredientData atbilde" );
	    			break;
	    		}
    		
        	}
        }
     }

}
