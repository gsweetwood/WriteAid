package application;

import org.json.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class WordSwapController implements Initializable {

	private static final String USER_AGENT = "Mozilla/5.0";;
	private Hashtable<String, Integer> wordCountHTable;
	private String[] wordArray;
	private String inputFromUser;
	private String delimiters = "\\W+";
	private String response;

	@FXML
	private TextArea textArea_Input;

	@FXML
	private Button loadInput_button;

	@FXML
	private TableView<Word> tableView;

	@FXML
	private TableColumn<Word, String> wordColumn;
	
	@FXML
	private TableColumn<Word, Integer> countColumn;

	@FXML
	private Button browse_button;
	
	@FXML
    private TextArea synList_txtArea;

	public void setInputFromUser() {
		this.inputFromUser = this.textArea_Input.getText();
	}

	public String getInputFromUser() {
		return inputFromUser;
	}

	public void setWordArray(String[] wordArray) {
		this.wordArray = getArrayOfWords(inputFromUser);
	}

	public String[] getArrayOfWords(String input) {
		String[] array = input.split(delimiters);
		return array;
	}

	// For development purposes only
	public void displayWordArray() {
		for (String x : wordArray) {
			if (!x.equals(" ")) {
				System.out.println(x);
			}

		}
	}

	@FXML
	void loadInput(ActionEvent event) {
		this.setInputFromUser();
		this.setWordArray(this.getArrayOfWords(this.getInputFromUser()));
		//this.displayWordArray();
		this.setWordCountTable();
		this.populateTable();
	}

	public void setWordCountTable() {
		this.wordCountHTable = new Hashtable<String, Integer>();
		for (String word : wordArray) {
			if (!this.wordCountHTable.containsKey(word.toUpperCase()) && !word.isEmpty() && !word.equals(" ")
					&& word != null && !word.equals("\\n")) {
				this.wordCountHTable.put(word.toUpperCase(), 1);
			} else if (this.wordCountHTable.containsKey(word.toUpperCase()) && !word.isEmpty() && !word.equals(" ")
					&& word != null && !word.equals("\\n")) {
				int currentCount = this.wordCountHTable.get(word.toUpperCase());
				this.wordCountHTable.replace(word.toUpperCase(), currentCount + 1);
			}
		}
		System.out.println(this.wordCountHTable);

	}

	// Create Word objects and fill the table
	public void populateTable() {
		System.out.println("start");

		countColumn.setCellValueFactory(new PropertyValueFactory<Word, Integer>("count"));
		wordColumn.setCellValueFactory(new PropertyValueFactory<>("wordName"));
		ObservableList<Word> wordsList = FXCollections.observableArrayList();
		Set<String> keys = wordCountHTable.keySet();
		for (String key : keys) {
			wordsList.add(new Word(key, wordCountHTable.get(key)));
		}
		
		tableView.setItems(wordsList);

	}

	public void sendGetToAPI(String word) throws Exception {

		System.out.println("sendGet started");

		String url = "http://words.bighugelabs.com/api/2/d291d0ab9725a613d85cda80e049d9fb/" + word + "/json";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// Request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		System.out.println(response.toString());
		this.response = response.toString();
		
		this.parse(this.response);
	}

	@FXML
	void sendGet(ActionEvent event) throws Exception {
		System.out.println("say something");
		

	}

	@FXML
	void selectItem(MouseEvent event) throws Exception {

		Word selectedWord = tableView.getSelectionModel().getSelectedItem();
		System.out.println(selectedWord.getWordName());
		sendGetToAPI(selectedWord.getWordName());
		

	}
	
	public void parse(String myJSONString) throws JSONException  {
		StringBuilder finalResult = new StringBuilder();
		JSONObject object = new JSONObject(myJSONString);
		String[] keys = JSONObject.getNames(object);
		System.out.println(object);

		for (String key : keys)
		{
		    JSONObject value = new JSONObject(object.getJSONObject(key).toString());
		    System.out.println("1 " + key);
		    finalResult.append(key + "\n");
		    System.out.println(value);
		    // Determine type of value and do something with it...
		    String[] nestedKeys = JSONObject.getNames(value);
		    for (String innerKey : nestedKeys) {
		    	finalResult.append("\t" + innerKey + "\n");
		    	JSONArray pumpkins = new JSONArray(value.getJSONArray(innerKey).toString());
		    	String[] tempArr = toStringArray(pumpkins);
		    	
	    	
		    	System.out.println(tempArr);
		    	for(int i = 0; i < tempArr.length;i++) {
		    		finalResult.append("\t\t" + tempArr[i] + "\n");
		    	}
		    }
		    
		}
		
		this.synList_txtArea.setText(finalResult.toString());
	}
	
	//from https://stackoverflow.com/questions/15871309/convert-jsonarray-to-string-array
	public static String[] toStringArray(JSONArray array) {
	    if(array==null)
	        return null;

	    String[] arr=new String[array.length()];
	    for(int i=0; i<arr.length; i++) {
	        arr[i]=array.optString(i);
	    }
	    return arr;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

}
