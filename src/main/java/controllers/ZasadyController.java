package controllers;

import javafx.fxml.FXML;

public class ZasadyController {
	
	private MainController mainController;		// referencja do g³ównego kontrolera

	@FXML
	public void powrotDoMenu(){				// powrót do g³ównego menu
		mainController.loadMenuScreen();
	}
	
	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}
	
}
