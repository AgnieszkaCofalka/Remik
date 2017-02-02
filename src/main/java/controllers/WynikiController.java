package controllers;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class WynikiController {
	
	@FXML
	private Label czyWygral;		// wyœwietli tekst wygra³eœ/ przegra³eœ
	
	@FXML
	private Label wynikiGry;			// wyœwietli dok³adne wyniki gry

	private MainController mainController;		// referencja do kontrolera g³ównego ekranu
	
	private int wygrany;			// 0 - u¿ytkownik, 1 - gracz komputerowy; przekazywane z gry
	private int[] wyniki;
	
	public void wyswietlWyniki(){			// wypisuje informacje o wynikach w podanych miejscach
		if (wygrany == 0)
			czyWygral.setText("Wygra³eœ partiê");
		else
			czyWygral.setText("Przegra³eœ partiê");
		wynikiGry.setText("Twój wynik: "+wyniki[0]+"pkt \nWynik przeciwnika: "+wyniki[1]+" pkt");
	}
	
	public void nowaGra(){
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/GraScreen.fxml"));	// wczytanie ekrany gry
		StackPane stackpane = null;						// jeœli gracz wybra³ opcjê kolejnej partii
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
		Platform.exit();		// wy³¹czenia okna aplikacji, jeœli gracz wybra³ koniec gry
	}
	

	public void setMainController(MainController mainController) {	//pozwala zapisaæ referencjê do g³ównego kontrolera
		this.mainController = mainController;
	}

	public void setWygrany(int wygrany) {		// pozwala przekazaæ tutaj wyniki
		this.wygrany = wygrany;
	}

	public void setWyniki(int[] wyniki) {
		this.wyniki = wyniki;
	}
	
	
}
