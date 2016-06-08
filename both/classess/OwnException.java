package both.classess;

public class OwnException extends Exception {
	
	String msg;
	
	public OwnException( String msg ){
		this.msg = msg;
	}

	public String getMessage(){
		return msg;
	}
}
