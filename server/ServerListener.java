package server;

import both.classess.ActionData;
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
        			break;
        		}
        		case ConfActions.ActEditRecipeData :{
        			CoreServer.showThis( "ActEditRecipeData pieprasijums" );
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
