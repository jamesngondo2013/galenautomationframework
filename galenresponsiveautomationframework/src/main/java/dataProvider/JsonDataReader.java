package dataProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.james.api.context.TestObject;

import managers.FileReaderManager;
import testdatatypes.CustomerJson;

public class JsonDataReader {

	private String customerFilePath;
	private List<CustomerJson> customerList;
	private TestObject testObject;
	
	public JsonDataReader() throws IOException{
		this.testObject = new TestObject();
		customerList = getCustomerData();
	}
	
	private List<CustomerJson> getCustomerData() {
		Gson gson = new Gson();
		BufferedReader bufferReader = null;
		if (testObject.getServiceApi().getEnvironment().equals("INT")) {
			
			customerFilePath = "src" + File.separator + "main" + File.separator + "resources" + File.separator
					+ "testDataResources" + File.separator + testObject.getServiceApi().getEnvironment().toLowerCase()+ "_customer_data.json";
			
			return getTestData(gson, bufferReader);
		}
		else {
			customerFilePath = "src" + File.separator + "main" + File.separator + "resources" + File.separator
					+ "testDataResources" + File.separator + testObject.getServiceApi().getEnvironment().toLowerCase()+ "_customer_data.json";
			return getTestData(gson, bufferReader);
		}
		
	}

	private List<CustomerJson> getTestData(Gson gson, BufferedReader bufferReader) {
		try {
			bufferReader = new BufferedReader(new FileReader(customerFilePath));
			CustomerJson[] customers = gson.fromJson(bufferReader, CustomerJson[].class);
			return Arrays.asList(customers);
		}catch(FileNotFoundException e) {
			throw new RuntimeException("Json file not found at path : " + customerFilePath);
		}finally {
			try { if(bufferReader != null) bufferReader.close();}
			catch (IOException ignore) {}
		}
	}
		
	public final CustomerJson getCustomerByPPSN(String ppsEnvironment){
			 return customerList.stream().filter(x -> x.ppsEnvironment.equalsIgnoreCase(ppsEnvironment)).findAny().get();
	}
	
}
