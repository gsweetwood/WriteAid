//  Author:  Garret Sweetwood
//  Date:    6/1/2018
//  Scope:	This app was developed to help writers or students with two main functions.
//  		First, after the user copies in the text of their article, WriteAid will count
//			every iteration of every word.  This serves to point out words that are potentially 
//			used too much by displaying an ordered list.  Second, selecting a word from the
//			list will automatically return a thesaurus search of that word divided into word
//			forms.
//  		Thesaurus API service provided by words.bighugelabs.com

package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
		
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("WordSwap.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("WriteAid");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		
		launch(args);
			}
	
}
