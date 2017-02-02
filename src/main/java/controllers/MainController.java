package controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainController {
	
	@FXML
	private StackPane mainStackPane;
	
	@FXML
	public void initialize(){
		loadMenuScreen();
	}
	
	public void loadMenuScreen(){		// ³adowanie ekranu g³ównego menu
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../fxml/MenuScreen.fxml"));
		VBox vbox = null;
		try {
			vbox = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		MenuController menuController = loader.getController();
		menuController.setMainController(this);		// do wszystkich kolejnych ekranów jest przekazywana referencja do g³ównego,
		setScreen(vbox);							// ¿eby mo¿na by³oby je wczytywaæ
		
	}
	
	public void setScreen(Pane pane){				//wczytywanie ekranu podanego w argumencie do ekranu g³ównego
		mainStackPane.getChildren().clear();
		mainStackPane.getChildren().add(pane);
	}
	
}
