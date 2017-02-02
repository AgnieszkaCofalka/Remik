package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application{

	public static void main(String[] args) {		// uruchamia aplikacj�
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {		// �aduje g��wne okno, do kt�rego wczytuj� si� wszystkie ekrany
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../fxml/MainScreen.fxml"));
		StackPane stackpane = loader.load();
		Scene scene = new Scene(stackpane, 1400, 820);
		primaryStage.setMaximized(true);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Remik");
		primaryStage.show();
	}

}
