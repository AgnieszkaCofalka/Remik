package controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;

public class MenuController {
	
	private MainController mainController;			// referencja do g��wnego ekranu
	
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
		graController.setMainController(mainController);			// przekazanie kontrolerowi kolejnego ekranu referencji do g��wnego ekranu
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

	public void setMainController(MainController mainController) {		// pozwala zapisa� "z zewn�trz" referencj� do g��wnego kontrolera
		this.mainController = mainController;
	}
}
