package controllers;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class WynikiController {
	
	@FXML
	private Label czyWygral;		// wy�wietli tekst wygra�e�/ przegra�e�
	
	@FXML
	private Label wynikiGry;			// wy�wietli dok�adne wyniki gry

	private MainController mainController;		// referencja do kontrolera g��wnego ekranu
	
	private int wygrany;			// 0 - u�ytkownik, 1 - gracz komputerowy; przekazywane z gry
	private int[] wyniki;
	
	public void wyswietlWyniki(){			// wypisuje informacje o wynikach w podanych miejscach
		if (wygrany == 0)
			czyWygral.setText("Wygra�e� parti�");
		else
			czyWygral.setText("Przegra�e� parti�");
		wynikiGry.setText("Tw�j wynik: "+wyniki[0]+"pkt \nWynik przeciwnika: "+wyniki[1]+" pkt");
	}
	
	public void nowaGra(){
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/GraScreen.fxml"));	// wczytanie ekrany gry
		StackPane stackpane = null;						// je�li gracz wybra� opcj� kolejnej partii
		try {
			stackpane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		GraController graController = loader.getController();
		graController.getPartia().setWynikiGry(wyniki);
		graController.setMainController(mainController);
		mainController.setScreen(stackpane);	
	}
	
	public void koniecGry(){
		Platform.exit();		// wy��czenia okna aplikacji, je�li gracz wybra� koniec gry
	}
	

	public void setMainController(MainController mainController) {	//pozwala zapisa� referencj� do g��wnego kontrolera
		this.mainController = mainController;
	}

	public void setWygrany(int wygrany) {		// pozwala przekaza� tutaj wyniki
		this.wygrany = wygrany;
	}

	public void setWyniki(int[] wyniki) {
		this.wyniki = wyniki;
	}
	
	
}
