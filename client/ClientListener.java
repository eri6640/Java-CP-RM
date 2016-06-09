package client;

import server.CoreServer;
import both.classess.ActionData;
import both.classess.CategoryArray;
import both.classess.RecipesArray;
import both.conf.ConfActions;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ClientListener extends Listener {
	
	@Override
	public void received (Connection connection, Object object) {
		if( object instanceof RecipesArray ){
			CoreClient.RecipeStorage = (RecipesArray)object;
			CoreClient.showThis( "Received RecipeStorage" );
		}
		else if( object instanceof CategoryArray ){
			CoreClient.CategoryStorage = (CategoryArray)object;
			CoreClient.showThis( "Received CategoryStorage" );
		}
		else if( object instanceof ActionData ){
        	ActionData data = (ActionData)object;
        	
        	switch( data.getAction() ){
	    		case ConfActions.ActGetData :{
	    			CoreClient.showThis( "ActGetData atbilde" );
	    			if( data.isSuccess() ) CoreClient.showThis( "success" );
	    			else CoreClient.showThis( "! success" );
	    			CoreClient.showWarning( "title", "asdfdsaf" );
	    			break;
	    		}
	    		case ConfActions.ActAddRecipeData :{
	    			CoreClient.showThis( "ActAddRecipeData atbilde" );
	    			break;
	    		}
	    		case ConfActions.ActAddIngredientData :{
	    			CoreClient.showThis( "ActAddIngredientData atbilde" );
	    			break;
	    		}
	    		case ConfActions.ActEditRecipeData :{
	    			CoreClient.showThis( "ActEditRecipeData atbilde" );
	    			break;
	    		}
	    		case ConfActions.ActDelRecipeData :{
	    			CoreClient.showThis( "ActDelRecipeData atbilde" );
	    			break;
	    		}
	    		case ConfActions.ActDelIngredientData :{
	    			CoreClient.showThis( "ActDelIngredientData atbilde" );
	    			break;
	    		}
    		
        	}
        }
     }

}
