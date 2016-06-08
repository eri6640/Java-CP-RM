package both.classess;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
	
	private int recipe_id; //receptes id
	private int category_id; // recepshu kategorijas id
	private boolean active;
	private String recipe_name; //receptes nosaukums
	private String recipe_recipe; // pasha recepte
	private int recipe_created; // kad izveidota vai pievienota
	private ArrayList<Ingredient> ingredient_list; // sastavdalju saraksts
	
	public Recipe(){
		active = false;
	}
	
	public Recipe( int recipe_id, int category_id, String recipe_name, String recipe_recipe, int recipe_created ){
		this( recipe_id, category_id, recipe_name, recipe_recipe, recipe_created, new ArrayList<Ingredient>() );
	}
	
	public Recipe( int recipe_id, int category_id, String recipe_name, String recipe_recipe, int recipe_created, ArrayList<Ingredient> ingredient_list ){
		//this.recipe_id = recipe_id > 0 ? recipe_id : -1;
		//this.category_id = category_id > 0 ? category_id : -1;
		
		active = true;
		
		if( recipe_id > 0 ) this.recipe_id = recipe_id;
		else{
			active = false;
			return;
		}
		if( category_id > 0 ) this.category_id = category_id;
		else{
			active = false;
			return;
		}
		
		this.recipe_name = recipe_name;
		this.recipe_recipe = recipe_recipe;
		//this.recipe_created = recipe_created > 0 ? recipe_created : -1;
		if( recipe_created > 0 ) this.recipe_created = recipe_created;
		else{
			active = false;
			return;
		}
		
		if( ingredient_list.isEmpty() ) this.ingredient_list = new ArrayList<Ingredient>();
		else setIngredients( ingredient_list );
		
	}
	
	public void clear(){
		active = false;
		ingredient_list.clear();
	}
	
	/*
	 * 
	 * 
	 */
	public void setName( String recipe_name ){
		this.recipe_name = recipe_name;
	}
	public void setRecipe( String recipe_recipe ){
		this.recipe_recipe = recipe_recipe;
	}
	public void setIngredients( ArrayList<Ingredient> ingredient_list ){
		if( ! ingredient_list.isEmpty() ){
			if( ! this.ingredient_list.isEmpty() ) ingredient_list.clear(); // ja ir kaut kas... iztiram
			
			for( Ingredient ingredient : ingredient_list ){
				addIngredient( ingredient );
			}
		}
	}
	public void addIngredients( ArrayList<Ingredient> ingredient_list ){
		if( ! ingredient_list.isEmpty() ){
			for( Ingredient ingredient : ingredient_list ){
				addIngredient( ingredient );
			}
		}
	}
	
	//
	public boolean addIngredient( Ingredient ingredient ){
		if( ingredient != null ){
			ingredient_list.add( ingredient );
			return true;
		}
		return false;
	}
	
	/*
	 * 
	 * 
	 */
	public boolean isActive(){
		return active;
	}
	public int getRID(){
		return recipe_id;
	}
	public int getCID(){
		return category_id;
	}
	public String getName(){
		return recipe_name;
	}
	public String getRecipe(){
		return recipe_recipe;
	}
	public int getCreated(){
		return recipe_created;
	}
	
	public ArrayList<Ingredient> getIngredients(){
		return ingredient_list;
	}
	
	public int ingredientSize(){
		return ingredient_list.size();
	}

}
