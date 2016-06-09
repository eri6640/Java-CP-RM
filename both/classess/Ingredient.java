package both.classess;

public class Ingredient {
	
	private int ingred_id;
	private int recipe_id;
	private String ingred_ingredient; // sastavdalja
	private String ingred_value; // daudzums
	private String ingred_measurement; // mervieniba
	
	public Ingredient(){ //neapskatits gadijums //kryo ...
		ingred_id = 0;
		recipe_id = 0;
	}
	
	public Ingredient( int ingred_id, int recipe_id, String ingred_ingredient, String ingred_value, String ingred_measurement ){
		this.ingred_id = ingred_id > 0 ? ingred_id : -1;
		this.recipe_id = recipe_id > 0 ? recipe_id : -1;

		this.ingred_ingredient = ingred_ingredient;
		
		this.ingred_value = ingred_value;
		
		this.ingred_measurement = ingred_measurement;
	}
	

	/*
	 * 
	 * 
	 * 
	 */
	public void setId( int ingred_id ) {
		this.ingred_id = ingred_id;
	}
	public void setIngredient( String ingred_ingredient ) {
		this.ingred_ingredient = ingred_ingredient;
	}

	public void setValue( String ingred_value ) {
		this.ingred_value = ingred_value;
	}

	public void setMeasurement( String ingred_measurement ) {
		this.ingred_measurement = ingred_measurement;
	}
	
	
	/*
	 * 
	 * 
	 * 
	 */
	public int getIID() {
		return ingred_id;
	}

	public int getRID() {
		return recipe_id;
	}
	
	public String getIngredient() {
		return ingred_ingredient;
	}

	public String getValue() {
		return ingred_value;
	}

	public String getMeasurement() {
		return ingred_measurement;
	}
	
	

}
