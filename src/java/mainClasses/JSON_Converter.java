/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainClasses;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author micha
 */
public class JSON_Converter {
    
    public String getJSONFromAjax(BufferedReader reader) throws IOException{
	StringBuilder buffer = new StringBuilder();
	String line;
	while ((line = reader.readLine()) != null) {
		buffer.append(line);
	}
	String data = buffer.toString();
	return data;
    }

    public Student jsonToStudent(BufferedReader json) {
        Gson gson = new Gson();
        Student msg = gson.fromJson(json, Student.class);
        return msg;
    }

    public Librarian jsonToLibrarian(BufferedReader json) {
        Gson gson = new Gson();
        Librarian msg = gson.fromJson(json, Librarian.class);
        return msg;
    }

    public Book jsonToBook(BufferedReader json) {
        Gson gson = new Gson();
        Book msg = gson.fromJson(json, Book.class);
        return msg;
    }
}
