package dataProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.management.RuntimeErrorException;

import com.james.api.context.TestObject;

public class TextFileReader {

	private TestObject testObject;
	private String filePath;
	private String fileContent;
	
	public TextFileReader() throws IOException {
		this.testObject = new TestObject();
		fileContent = getFileContentText();
	}

	public final String getFileContentText() throws IOException {
		filePath = "src" + File.separator + "main" + File.separator + "resources" + File.separator
				+ "testDataResources" + File.separator + "content.txt";
		
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    fileContent = sb.toString();
		} finally {
		    br.close();
		}
		return fileContent;
	}
}
