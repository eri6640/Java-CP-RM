package both.classess;

import java.util.HashMap;
import java.util.Map;

public class RecipesArray {
	
	private HashMap<Integer, Recipe> list;
	
	
	public RecipesArray(){
		this( new HashMap<Integer, Recipe>() );
	}
	public RecipesArray( HashMap<Integer, Recipe> list ){
		this.list = list;
	}
	
	public int size(){
		return list.size();
	}
	
	public Map<Integer, Recipe> getList(){
		return list;
	}
	
	public boolean addRecipe( Recipe recipe ){
		int recipe_id = recipe.getRID();
		
		if( list.containsKey( recipe_id ) ) return false;
		
		list.put( recipe_id, recipe );
		return true;
	}
	
	public boolean delRecipe( Recipe recipe ){
		int recipe_id = recipe.getRID();
		
		if( list.containsKey( recipe_id ) ) return false;
		
		list.remove( recipe_id, recipe );// nestrada...
		return true;
	}
	
	public boolean addRecipeList( HashMap<Integer, Recipe> list ){
		if( list.isEmpty() ) return false;
		
		list.entrySet().forEach( entry -> {
			addRecipe( entry.getValue() );
		});
		
		return true;
	}
	
	public boolean addRecipeIngredient( Ingredient ingredient ){
		Recipe recipe = getRecipe( ingredient.getRID() );
		if( recipe == null ) return false;
		return recipe.addIngredient( ingredient );
	}
	
	public boolean replace( Recipe recipe ){
		
		if( getRecipe( recipe.getRID() ) == null ) return false;
		list.remove( recipe.getRID() );
		
		return addRecipe( recipe );		
	}
	
	
	//
	
	public Recipe getRecipe( int index ){
		if( list.isEmpty() ) return null;
		if( index <= 0 ) return null;
		if( ! list.containsKey( index ) ) return null;
		
		return list.get( index );
	}
	
	public void clear(){
		list = new HashMap<Integer, Recipe>();
	}

}
