package both.ReadWrite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import both.classess.CategoryArray;

public class ReadWriteActions {
	
	public static void create( CategoryArray categories, String fin ) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter( fin, "UTF-8");
		for( String string : categories.getList().values() ){
			writer.println( string );
		}
		writer.close();
	}
	
	public static ArrayList<String> read( File fin ) throws IOException {
		FileInputStream fis = new FileInputStream(fin);
		ArrayList<String> list = new ArrayList<String>();
	 
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	 
		String line = null;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
			list.add( line );
		}
	 
		br.close();
		return list;
	}

}
