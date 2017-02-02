package controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;

public class MenuController {
	
	private MainController mainController;			// referencja do g³ównego ekranu
	
	@FXML
	public void rozpocznijGre(){
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/GraScreen.fxml"));		// wczytanie ekranu gry
		StackPane stackpane = null;	
		try {
			stackpane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		GraController graController = loader.getController();
		graController.setMainController(mainController);			// przekazanie kontrolerowi kolejnego ekranu referencji do g³ównego ekranu
		mainController.setScreen(stackpane);	
	}
	
	@FXML
	public void pokazZasady(){
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/ZasadyScreen.fxml"));		// wczytanie ekranu z zasadami gry
		StackPane stackpane = null;
		try {
			stackpane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ZasadyController zasadyController = loader.getController();
		zasadyController.setMainController(mainController);
		mainController.setScreen(stackpane);
		
	}

	public void setMainController(MainController mainController) {		// pozwala zapisaæ "z zewn¹trz" referencjê do g³ównego kontrolera
		this.mainController = mainController;
	}
}
