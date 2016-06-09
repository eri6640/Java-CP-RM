package both.classess;

import java.util.ArrayList;

public class ActionData {
	
	private final int action_id;
	private int action;
	private boolean success;
	private Recipe recipe;
	private Ingredient ingredient;
	private ArrayList<Integer> changed_ingredients;
	
	public ActionData( ){
		this( 0, 0, null, null, null ); // kartigi neapskatits gadijums //nepiecieshams kryo...
	}
	
	public ActionData( int action_id, int action ){
		this( action_id, action, null, null, null );
	}
	public ActionData( int action_id, int action, Recipe recipe ){
		this( action_id, action, recipe, null, null );
	}
	public ActionData( int action_id, int action, Ingredient ingredient ){
		this( action_id, action, null, ingredient, null );
	}
	public ActionData( int action_id, int action, ArrayList<Integer> changed_ingredients ){
		this( action_id, action, null, null, changed_ingredients );
	}
	public ActionData( int action_id, int action, Recipe recipe, Ingredient ingredient, ArrayList<Integer> changed_ingredients ){
		this.action_id = action_id;
		this.action = action;
		this.recipe = recipe;
		this.ingredient = ingredient;
		this.changed_ingredients = changed_ingredients;
	}
	

	/*
	 * 
	 * 
	 */
	public void setSuccess(){
		success = true;
	}
	public void clear(){
		recipe.clear();
		changed_ingredients.clear();
	}
	
	
	/*
	 * 
	 * 
	 */
	public boolean isSuccess(){
		return success;
	}
	public int getID(){
		return action_id;
	}
	public int getAction(){
		return action;
	}
	
	public Recipe getRecipe(){
		return recipe;
	}
	public Ingredient getIngredient(){
		return ingredient;
	}
	public ArrayList<Integer> getIngredients(){
		return changed_ingredients;
	}

}
