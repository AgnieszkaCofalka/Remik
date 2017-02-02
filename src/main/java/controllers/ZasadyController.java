package controllers;

import javafx.fxml.FXML;

public class ZasadyController {
	
	private MainController mainController;		// referencja do g��wnego kontrolera

	@FXML
	public void powrotDoMenu(){				// powr�t do g��wnego menu
		mainController.loadMenuScreen();
	}
	
	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}
	
}
