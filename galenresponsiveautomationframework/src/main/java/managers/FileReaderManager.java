package managers;

import java.io.IOException;

import dataProvider.JsonDataReader;
import dataProvider.TextFileReader;

public class FileReaderManager {

	private static FileReaderManager fileReaderManager = new FileReaderManager();
	private static JsonDataReader jsonDataReader;
	private static TextFileReader textFileReader;


	private FileReaderManager() {
	}

	public static FileReaderManager getInstance() {
		return fileReaderManager;
	}

	public JsonDataReader getJsonReader() throws IOException {
		return (jsonDataReader == null) ? new JsonDataReader() : jsonDataReader;
	}
	
	public TextFileReader getTextFileReader() throws IOException {
		return (textFileReader == null) ? new TextFileReader() : textFileReader;
	}
	

}
