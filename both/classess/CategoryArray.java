package both.classess;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CategoryArray {
	
	private HashMap<Integer, String> list;
	
	
	public CategoryArray(){
		this( new HashMap<Integer, String>() );
	}
	public CategoryArray( HashMap<Integer, String> list ){
		this.list = list;
	}
	
	public int size(){
		return list.size();
	}
	
	public Map<Integer, String> getList(){
		return list;
	}
	
	public boolean addCategory( int index, String string ){
		
		if( list.containsKey( index ) ) return false;
		
		list.put( index, string );
		return true;
	}
	
	public boolean addCategoryList( HashMap<Integer, String> list ){
		if( list.isEmpty() ) return false;
		
		list.entrySet().forEach( entry -> {
			addCategory( entry.getKey(), entry.getValue() );
		});
		
		return true;
	}
	
	
	//
	
	public String getCategory( int index ){
		if( list.isEmpty() ) return "empty";
		if( index <= 0 ) return "x<=0";
		if( ! list.containsKey( index ) ) return "nav indexa";
		
		return list.get( index );
		/*for( Entry< Integer, String > entry : list.entrySet() ){
			if( entry.getKey() == index ){
				return entry.getValue();
			}
		}
		
		return "else";*/
	}
	public int getCategory( String string ){
		if( list.isEmpty() ) return 0;
		if( string == null ) return 0;
		if( ! list.containsValue( string ) ) return 0;
		
		for( Entry< Integer, String > entry : list.entrySet() ){
			if( entry.getValue().equalsIgnoreCase( string ) ){
				return entry.getKey();
			}
		}

		return 0;
	}
	
	public void clear(){
		list = new HashMap<Integer, String>();
	}

}
