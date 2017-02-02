package controllers;

import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import main.Karta;
import main.Partia;

public class GraController {
	

	@FXML
	private HBox twojeKarty;		// zawiera karty gracza (przyciski)
	
	@FXML
	private Label komunikaty;		// wyœwietla komunikaty dla gracza
	
	@FXML
	private HBox przyciskiSterowania;		// tu pojawiaj¹ siê przyciski do sterowania przebiegiem gry
	
	@FXML
	private FlowPane sekwensyNaStole;			// w œrodku wczytuj¹ siê grafiki kart w wy³o¿onych sekwensach wraz z odpowiednimi przyciskami
	
	@FXML
	private FlowPane grupyNaStole;				// jak wy¿ej, dla grup kart
	
	private MainController mainController;		// referencja do g³ównego kontrolera
	private Partia partia;						// klasa reprezentuj¹ca pojedyncz¹ partiê
	private ArrayList<Button> kartyPrzyciski;			// listy przycisków, pozwalaj¹ na ³atwy dostêp do nich (np. przy zmianie akcji)
	private ArrayList<VBox> sekwensyNaStolePrzyciski;
	private ArrayList<VBox> grupyNaStolePrzyciski;
	
	public Partia getPartia() {
		return partia;
	}
	
	public GraController(){			
		partia = new Partia();
		sekwensyNaStolePrzyciski = new ArrayList<VBox>();
		grupyNaStolePrzyciski = new ArrayList<VBox>();
		kartyPrzyciski = new ArrayList<Button>();
	}
	
	public void initialize(){			// po inicjalizacji automatycznie rozpoczyna partiê
		partia.rozpocznijPartie();
		wczytajKartyGracza();
		graczWyrzucaKarte();
	}
	

	private void graczPobieraKarte() {
		wczytajKartyNaStole();			// pokazuj¹ siê ju¿ wy³o¿ono karty (ale jeszcze bez przyciskow)
		wczytajKartyGracza();			// pokazuj¹ siê karty nale¿¹ce do gracza (grafika)
		komunikaty.setText("Karta wyrzucona przez przeciwnika: "+partia.getWyrzucona()+"\nWez kartê z talii lub wyrzucon¹ przez przeciwnika");
		Button zTalii = new Button("Karta z talii");				// stworzenie przycisków sterowania i ustawienie dla nich akcji
		Button odPrzeciwnika = new Button("Karta od przeciwnika");
		EventHandler<ActionEvent> h1 = new EventHandler<ActionEvent>(){		

			public void handle(ActionEvent event) {
				pobranoKarteZTalii();
			}
		};
		EventHandler<ActionEvent> h2 = new EventHandler<ActionEvent>(){		

			public void handle(ActionEvent event) {
				pobranoWyrzucona();
			}
		};
		zTalii.setOnAction(h1);
		odPrzeciwnika.setOnAction(h2);
		przyciskiSterowania.getChildren().add(zTalii);	    //dodanie przycisków strowania do odpowiedniego panelu
		przyciskiSterowania.getChildren().add(odPrzeciwnika);
	}

	private void wczytajKartyNaStole() {
		// tworzy HBoxy z obrazkami kart, bez przycisków akcji
		sekwensyNaStole.getChildren().clear();
		grupyNaStole.getChildren().clear();
		sekwensyNaStolePrzyciski.clear();
		grupyNaStolePrzyciski.clear();
		for(int i = 0; i < partia.getKartyNaStole().getLiczbaSekwensow(); i++){	// wczytuje grafiki kart wy³o¿onych na stó³, na razie bez przycisków
			HBox h = new HBox();
			VBox v = new VBox();					// tutaj bêd¹ siê potem pojawia³y przyciski akcji do danego sekwensu
			v.setAlignment(Pos.CENTER);
			v.setSpacing(4.0);
			v.setPadding(new Insets(0, 4, 0, 4));
			h.getChildren().add(v);
			sekwensyNaStolePrzyciski.add(v);
			h.setSpacing(2.0);
			for (int j = 0; j < partia.getKartyNaStole().liczbaKartWSekwensieNr(i); j++){
				Image imageDecline = new Image(getClass().getResourceAsStream("../Images/"+partia.getKartyNaStole().zobaczKarteZSekwensuNr(j, i).getNrWTalii()+".png"));
				ImageView iv = new ImageView(imageDecline);
				iv.setFitHeight(96);
				iv.setFitWidth(64);
				h.getChildren().add(iv);
			}
			sekwensyNaStole.getChildren().add(h);
		}
		for(int i = 0; i < partia.getKartyNaStole().getLiczbaGrup(); i++){		// jak wy¿ej, ale dla grup kart
			HBox h = new HBox();
			VBox v = new VBox();
			v.setAlignment(Pos.CENTER);
			v.setSpacing(4.0);
			v.setPadding(new Insets(0, 4, 0, 4));
			h.getChildren().add(v);
			grupyNaStolePrzyciski.add(v);
			h.setSpacing(2.0);
			for (int j = 0; j < partia.getKartyNaStole().liczbaKartWGrupieNr(i); j++){
				Image imageDecline = new Image(getClass().getResourceAsStream("../Images/"+partia.getKartyNaStole().zobaczKarteZGrupyNr(j, i).getNrWTalii()+".png"));
				ImageView iv = new ImageView(imageDecline);
				iv.setFitHeight(96);
				iv.setFitWidth(64);
				h.getChildren().add(iv);
			}
			grupyNaStole.getChildren().add(h);
		}
	
	}
	
	private void aktywujDokladanie(){			// jeœli mo¿liwe jest dok³adanie kart, to tworzy odpowiednie przyciski akcji
		if (partia.czyMoznaDokladac()){		// czyMoznaDokladac - zwraca boola, czy mo¿na w danym momencie dok³adaæ karty
			for(int i = 0; i < sekwensyNaStole.getChildren().size(); i++){		// dla sekwensow
				final int I = i;
				Button dolozKarte = new Button("Do³ó¿ kartê");
				EventHandler<ActionEvent> h1 = new EventHandler<ActionEvent>(){		
				
					public void handle(ActionEvent event) {
						dokladanieKartyDoSekwensuNr(I);
					}
				};
				dolozKarte.setOnAction(h1);
				sekwensyNaStolePrzyciski.get(i).getChildren().add(dolozKarte);
				if(partia.czyJestJokerWSekwensieNr(i)){
				Button wezJokera = new Button("Wez jokera");
					EventHandler<ActionEvent> h2 = new EventHandler<ActionEvent>(){		

						public void handle(ActionEvent event) {
							wezJokeraZSekwensuNr(I);
						}
					};
				
					wezJokera.setOnAction(h2);
					sekwensyNaStolePrzyciski.get(i).getChildren().add(wezJokera);
				}
			}
			for(int i = 0; i < grupyNaStole.getChildren().size(); i++){		// dla grup kart

				final int I = i;
				if(partia.czyMoznaDokladacDoGrupyNr(i)){
					Button dolozKarte = new Button("Do³ó¿ kartê");
					EventHandler<ActionEvent> h1 = new EventHandler<ActionEvent>(){		

						public void handle(ActionEvent event) {
							dokladanieKartyDoGrupyNr(I);
						}
					};
					dolozKarte.setOnAction(h1);
					grupyNaStolePrzyciski.get(i).getChildren().add(dolozKarte);
				}
				if(partia.czyJestJokerWGrupieNr(i)){
					Button wezJokera = new Button("Wez jokera");
				
					EventHandler<ActionEvent> h2 = new EventHandler<ActionEvent>(){		

						public void handle(ActionEvent event) {
							wezJokeraZGrupyNr(I);
						}
					};
				wezJokera.setOnAction(h2);
				grupyNaStolePrzyciski.get(i).getChildren().add(wezJokera);
				}
			}
		}
	}
	
	private void dezaktywujDokladanie(){				// usuwa przyciski pozwalaj¹ce na dok³adanie kart
		for (int i = 0; i < sekwensyNaStolePrzyciski.size(); i++)
			sekwensyNaStolePrzyciski.get(i).getChildren().clear();
		for (int i = 0; i < grupyNaStolePrzyciski.size(); i++)
			grupyNaStolePrzyciski.get(i).getChildren().clear();
	}

	private void pobranoWyrzucona() {		
		partia.wezWyrzucona();				
		wczytajKartyGracza();			// wczytuje odpowiednie elementy po pobraniu karty od poprzedniego gracza
		aktywujDokladanie();
		wykladanieKart();
	}
	
	private void dokladanieKartyDoSekwensuNr(int id){
		komunikaty.setText("Wybierz kartê, któr¹ chcesz do³o¿yæ");
		przyciskiSterowania.getChildren().clear();
		dezaktywujDokladanie();		// usuwa przyciski znajduj¹ce siê przy wszystkich sekwensach i grupach
		final int ID = id;
		for (int i = 0; i < kartyPrzyciski.size(); i++){
			final int I = i;
			EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>(){		
				
				public void handle(ActionEvent event) {
					if (partia.czyJokerNaMiejscuOId(I)){
						dokladanieJokeraDoSekwensu(ID, I);
					}
					else{
						boolean czyDolozono = partia.sprobujDolozycDoSekwensuNr(ID, I); // sprawdza, czy dan¹ kartê mo¿na do³o¿yæ, jeœli tak to karta zostaje do³o¿ona
						if (czyDolozono){
							komunikaty.setText("Karta zosta³a do³o¿ona");		// informuje, czy kartê uda³o siê do³o¿yæ, czy nie
							wczytajKartyNaStole();
							aktywujDokladanie();
							glowneAkcje();
						}
						else{
							komunikaty.setText("Nieodpowiednia karta");
						}
					}

				}
			};
			kartyPrzyciski.get(i).setOnAction(handler);
		}
		Button zrezygnuj = new Button("Zrezygnuj z dok³adania kart");
		EventHandler<ActionEvent> h1 = new EventHandler<ActionEvent>(){		

			public void handle(ActionEvent event) {				// rezygnacja z dok³adania karty, wyœwielaj¹ siê g³ówne opcje (wyk³adanie i wyrucenie karty)
					komunikaty.setText("Wy³ó¿ karty lub wyrzuæ kartê");
					glowneAkcje();
					aktywujDokladanie();
				}
		};
		zrezygnuj.setOnAction(h1);
		przyciskiSterowania.getChildren().add(zrezygnuj);
		
	}
	
	private void dokladanieJokeraDoSekwensu(int nrSekwensu, int nrKarty){
		Button naPoczatku = new Button("Do³ó¿ na pocz¹tku sekwensu");		// jeœli dok³adany jest Joker, to gracz mo¿e zdecydowaæ, czy dok³ada go na pocz¹tku czy na koñcu sekwensu
		Button naKoncu = new Button("Do³ó¿ na koñcu sekwensu");
		Button zrezygnuj = new Button("Zrezygnuj z dok³adania kart");			// tworzenie odpowiednych przycisków sterowania
		komunikaty.setText("Dok³adanie Jokera");
		for(int i = 0; i < kartyPrzyciski.size(); i++)
			kartyPrzyciski.get(i).setOnAction(null);
		final int NRSEKW = nrSekwensu;
		final int NRKARTY = nrKarty;
		EventHandler<ActionEvent> h1 = new EventHandler<ActionEvent>(){		

			public void handle(ActionEvent event) {
				boolean czyDolozono = partia.dolozJokeraNaPoczatkuSekwensuNr(NRSEKW, NRKARTY);		// dok³ada Jokera, a jeœli siê nie uda³o wyœwietla komunikat
				if (czyDolozono){
					komunikaty.setText("Karta zosta³a do³o¿ona");
					wczytajKartyNaStole();
					aktywujDokladanie();
					glowneAkcje();
				}
				else{
					komunikaty.setText("Nie mo¿na do³o¿yæ Jokera przed dwójk¹\nani przed innym Jokerem");
				}
			}
		};
		EventHandler<ActionEvent> h2 = new EventHandler<ActionEvent>(){		

			public void handle(ActionEvent event) {
				boolean czyDolozono = partia.dolozJokeraNaKoncuSekwensuNr(NRSEKW, NRKARTY);		// jak wy¿ej, ale Joker jest dok³adany na koñcu sekwensu
				if (czyDolozono){
					komunikaty.setText("Karta zosta³a do³o¿ona");
					wczytajKartyNaStole();
					aktywujDokladanie();
					glowneAkcje();
				}
				else{
					komunikaty.setText("Nie mo¿na do³o¿yæ Jokera po asie\nani po innym Jokerze");
				}
			}
		};
		
		EventHandler<ActionEvent> h3 = new EventHandler<ActionEvent>(){		

			public void handle(ActionEvent event) {		// rezygnacja z dok³adania kart
					komunikaty.setText("Wy³ó¿ karty lub wyrzuæ kartê");
					glowneAkcje();
					aktywujDokladanie();
				}
		};
		naPoczatku.setOnAction(h1);		// przypisanie akcji
		naKoncu.setOnAction(h2);
		zrezygnuj.setOnAction(h3);
		przyciskiSterowania.getChildren().clear();
		przyciskiSterowania.getChildren().add(naPoczatku);		// dodanie przycisków na ekran
		przyciskiSterowania.getChildren().add(naKoncu);
		przyciskiSterowania.getChildren().add(zrezygnuj);
		
	}
	
	private void dokladanieKartyDoGrupyNr(int id){		// po klikniêciu w 'do³ó¿ kartê' przy danej grupie kkart
		komunikaty.setText("Wybierz kartê, któr¹ chcesz do³o¿yæ");
		przyciskiSterowania.getChildren().clear();
		dezaktywujDokladanie();
		final int ID = id;
		for (int i = 0; i < kartyPrzyciski.size(); i++){
			final int I = i;
			EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>(){		

				public void handle(ActionEvent event) {
					boolean czyDolozono = partia.sprobujDolozycDoGrupyNr(ID, I);
					if (czyDolozono){
						komunikaty.setText("Karta zosta³a do³o¿ona");
						wczytajKartyNaStole();
						aktywujDokladanie();
						glowneAkcje();
					}
					else{
						komunikaty.setText("Nieodpowiednia karta");
					}
				}
			};
			kartyPrzyciski.get(i).setOnAction(handler);
		}
		Button zrezygnuj = new Button("Zrezygnuj z dok³adania kart");
		EventHandler<ActionEvent> h1 = new EventHandler<ActionEvent>(){	

			public void handle(ActionEvent event) {
					komunikaty.setText("Wy³ó¿ karty lub wyrzuæ kartê");
					glowneAkcje();
					aktywujDokladanie();
				}
		};
		zrezygnuj.setOnAction(h1);
		przyciskiSterowania.getChildren().add(zrezygnuj);
	}
	
	private void wezJokeraZGrupyNr(int id){
		komunikaty.setText("Wybierz kartê, któr¹ chcesz zamieniæ na Jokera");
		przyciskiSterowania.getChildren().clear();
		dezaktywujDokladanie();
		final int ID = id;
		for (int i = 0; i < kartyPrzyciski.size(); i++){
			final int I = i;
			EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>(){		
				
				public void handle(ActionEvent event) {
					Karta joker = partia.sprobujWziacJokeraZGrupyNr(ID, I);
					if (joker != null){
						komunikaty.setText("Zamieniono karty, otrzymujesz Jokera");
						wczytajKartyNaStole();
						aktywujDokladanie();
						glowneAkcje();
					}
					else{
						komunikaty.setText("Nieodpowiednia karta");
					}
				}
			};
			kartyPrzyciski.get(i).setOnAction(handler);
		}
		Button zrezygnuj = new Button("Zrezygnuj z zamiany kart");
		EventHandler<ActionEvent> h1 = new EventHandler<ActionEvent>(){		

			public void handle(ActionEvent event) {
					komunikaty.setText("Wy³ó¿ karty lub wyrzuæ kartê");
					glowneAkcje();
					aktywujDokladanie();
				}
		};
		zrezygnuj.setOnAction(h1);
		przyciskiSterowania.getChildren().add(zrezygnuj);
	}
	
	private void wezJokeraZSekwensuNr(int id){
		komunikaty.setText("Wybierz kartê, któr¹ chcesz zamieniæ na Jokera");
		przyciskiSterowania.getChildren().clear();
		dezaktywujDokladanie();
		final int ID = id;
		for (int i = 0; i < kartyPrzyciski.size(); i++){
			final int I = i;
			EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>(){		
				
				public void handle(ActionEvent event) {
					Karta joker = partia.sprobujWziacJokeraZSekwensuNr(ID, I);
					if (joker != null){
						komunikaty.setText("Zamieniono karty, otrzymujesz Jokera");
						wczytajKartyNaStole();
						aktywujDokladanie();
						glowneAkcje();
					}
					else{
						komunikaty.setText("Nieodpowiednia karta");
					}
				}
			};
			kartyPrzyciski.get(i).setOnAction(handler);
		}
		Button zrezygnuj = new Button("Zrezygnuj z zamiany kart");
		EventHandler<ActionEvent> h1 = new EventHandler<ActionEvent>(){		

			public void handle(ActionEvent event) {
					komunikaty.setText("Wy³ó¿ karty lub wyrzuæ kartê");
					glowneAkcje();
					aktywujDokladanie();
				}
		};
		zrezygnuj.setOnAction(h1);
		przyciskiSterowania.getChildren().add(zrezygnuj);
	}
	
	private void pobranoKarteZTalii() {		// wczytuje odpowiednie pola, jeœi gracz wybra³ pobranie karty z talii
		przyciskiSterowania.getChildren().clear();
		Karta nowaKarta = partia.wezZTalii();
		komunikaty.setText("Pobrano kartê z talii: "+nowaKarta);
		aktywujDokladanie();
		glowneAkcje();
	}
	
	private void glowneAkcje(){		// aktywuje przyciski dla wyk³adania kart i wyrzucania karty (+przyciski dok³adania kart, jeœli mo¿na dok³adaæ)
		wczytajKartyGracza();
		przyciskiSterowania.getChildren().clear();
		for(int i = 0; i < kartyPrzyciski.size(); i++)
			kartyPrzyciski.get(i).setOnAction(null);
		Button wyloz = new Button("Wy³ó¿ karty");
		Button wyrzuc = new Button ("Wyrzuæ kartê");
		EventHandler<ActionEvent> h1 = new EventHandler<ActionEvent>(){		

			public void handle(ActionEvent event) {
				partia.poczatekWykladania();
				wykladanieKart();
			}
		};
		EventHandler<ActionEvent> h2 = new EventHandler<ActionEvent>(){		

			public void handle(ActionEvent event) {
				graczWyrzucaKarte();
			}
		};
		wyloz.setOnAction(h1);
		wyrzuc.setOnAction(h2);
		przyciskiSterowania.getChildren().add(wyloz);
		przyciskiSterowania.getChildren().add(wyrzuc);
	}
	
	private void wykladanieKart(){			//tworzy przyciski pozwalaj¹ce na wy³ozenie kart
		wczytajKartyGracza();
		przyciskiSterowania.getChildren().clear();		// punkty za wyk³adane karty
		
		Button sekwens = new Button("Dodaj sekwens");
		Button grupa = new Button("Dodaj grupê kart");
		Button zakoncz = new Button("Zatwierdz wy³o¿one karty");
		Button zrezygnuj = new Button("Zrezygnuj z wyk³adania kart");
		EventHandler<ActionEvent> h1 = new EventHandler<ActionEvent>(){		

			public void handle(ActionEvent event) {
				pierwszaKartaSekwensu();
			}
		};
		EventHandler<ActionEvent> h2 = new EventHandler<ActionEvent>(){		

			public void handle(ActionEvent event) {
				pierwszaKartaGrupy();
			}
		};
		EventHandler<ActionEvent> h3 = new EventHandler<ActionEvent>(){		

			public void handle(ActionEvent event) {
				koniecWykladania();
			}
		};
		EventHandler<ActionEvent> h4 = new EventHandler<ActionEvent>(){		

			public void handle(ActionEvent event) {
				rezygnacjaZWykladania();
			}
		};
		
		sekwens.setOnAction(h1);
		grupa.setOnAction(h2);
		zakoncz.setOnAction(h3);
		zrezygnuj.setOnAction(h4);
		
		przyciskiSterowania.getChildren().add(sekwens);
		przyciskiSterowania.getChildren().add(grupa);
		przyciskiSterowania.getChildren().add(zakoncz);	
		przyciskiSterowania.getChildren().add(zrezygnuj);	
		komunikaty.setText("Punkty za wyk³adane karty: " + partia.getPunktyWylozenia());
	}
	
	
	
	protected void rezygnacjaZWykladania() {
		partia.oddajKartyGraczowi();
		wczytajKartyGracza();
		if(partia.maWyrzucona()){	// jeœli gracz wzi¹³ kartê od poprzedniego gracza i jej nie wy³o¿y³, to musi j¹ oddaæ i wzi¹æ kartê z talii
			komunikaty.setText("Nie wy³o¿ono karty pobranej od przeciwnika,\nkarta zostaje zwrócona, otrzymujesz kartê z talii");
			partia.oddajWyrzucona();
			Button ok = new Button("Ok");
			EventHandler<ActionEvent> h1 = new EventHandler<ActionEvent>(){		
				public void handle(ActionEvent event) {
					pobranoKarteZTalii();
				}
			};
			ok.setOnAction(h1);
			przyciskiSterowania.getChildren().clear();
			przyciskiSterowania.getChildren().add(ok);
		}
		else{
			komunikaty.setText("Wy³ó¿ karty lub wyrzuæ kartê");
			glowneAkcje();
		}
		
	}

	

	private void koniecWykladania() {		
		String czyPoprawnieWylozono = partia.sprawdzCzyPoprawnieWylozono();
		if (czyPoprawnieWylozono != null){
			komunikaty.setText(czyPoprawnieWylozono);
			Button ok = new Button("Ok");
			EventHandler<ActionEvent> h1 = new EventHandler<ActionEvent>(){		
				public void handle(ActionEvent event) {
					wykladanieKart();
				}
			};
			ok.setOnAction(h1);
			przyciskiSterowania.getChildren().clear();
			przyciskiSterowania.getChildren().add(ok);
		}
		else{
			wczytajKartyNaStole();
			glowneAkcje();
			aktywujDokladanie();
			komunikaty.setText("Karty zosta³y wy³o¿one na stó³");
		}
	}
	
	
	protected void pierwszaKartaGrupy() { 		// rozpoczêcie tworzenia grupy kart
		przyciskiSterowania.getChildren().clear();
		partia.nowaGrupa();
		dodajKartyDoGrupy();
		
		Button zatwierdzGrupe = new Button("Zatwierdz grupê kart");
		EventHandler<ActionEvent> handler1 = new EventHandler<ActionEvent>(){		// przerzuciæ do osobnej metody
			
			public void handle(ActionEvent event) {
				partia.zatwierdzGrupe();
				wykladanieKart();
			}
		};
		zatwierdzGrupe.setOnAction(handler1);
		przyciskiSterowania.getChildren().add(zatwierdzGrupe);
		zatwierdzGrupe.setVisible(false);
		
		Button zrezygnujZGrupy = new Button("Zrezygnuj z tworzenia grupy kart");
		EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>(){		// przerzuciæ do osobnej metody
			
			public void handle(ActionEvent event) {
				partia.oddajGraczowiGrupe();
				wczytajKartyGracza();
				wykladanieKart();
			}
		};
		zrezygnujZGrupy.setOnAction(handler);
		przyciskiSterowania.getChildren().add(zrezygnujZGrupy);
	}
	
	private void dodajKartyDoGrupy(){			// dodawanie kolejnych kart do tworzonej w³aœnie grupy kart
		if (partia.czyMoznaZatwierdzicGrupe()){
			przyciskiSterowania.getChildren().get(0).setVisible(true);		// jeœli liczba kart w grupie >= 3, to aktywuje siê przycisk zatwierdzenia grupy
		}
			
		for(int i = 0; i < kartyPrzyciski.size(); i++){
			final int I = i;
			EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>(){	
			
				public void handle(ActionEvent event) {
					
					boolean czyMoznaDodac = partia.czyMoznaDodacDoWykladanejGrupy(I);
					if(czyMoznaDodac){
						wczytajKartyGracza();
						dodajKartyDoGrupy();
					}
				}
			};
			kartyPrzyciski.get(i).setOnAction(handler);
		}
	}
	
	protected void pierwszaKartaSekwensu(){			// rozpoczêcie tworzenia nowego sekwensu
		przyciskiSterowania.getChildren().clear();
		partia.nowySekwens();
		dodajKartyDoSekwensu();
		
		Button zatwierdzSekwens = new Button("Zatwierdz sekwens");
		EventHandler<ActionEvent> handler1 = new EventHandler<ActionEvent>(){		// przerzuciæ do osobnej metody
			
			public void handle(ActionEvent event) {
				partia.zatwierdzSekwens();
				wykladanieKart();
			}
		};
		zatwierdzSekwens.setOnAction(handler1);
		przyciskiSterowania.getChildren().add(zatwierdzSekwens);
		zatwierdzSekwens.setVisible(false);
		
		Button zrezygnujZSekwensu = new Button("Zrezygnuj z tworzenia sekwensu");
		EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>(){		// przerzuciæ do osobnej metody
			
			public void handle(ActionEvent event) {
				partia.oddajGraczowiSekwens();
				wczytajKartyGracza();
				wykladanieKart();
			}
		};
		zrezygnujZSekwensu.setOnAction(handler);
		przyciskiSterowania.getChildren().add(zrezygnujZSekwensu);
	}
	
	public void dodajKartyDoSekwensu(){		// dodawanie kolejnych kart do tworzonego sekwensu
		if (partia.czyMoznaZatwierdzicSekwens()){
			przyciskiSterowania.getChildren().get(0).setVisible(true);
		}
			
		for(int i = 0; i < kartyPrzyciski.size(); i++){
			final int I = i;
			EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>(){	
			
				public void handle(ActionEvent event) {
					boolean czyMoznaDodac = partia.czyMoznaDodacDoWykladanegoSekwensu(I);
					if(czyMoznaDodac){
						wczytajKartyGracza();
						dodajKartyDoSekwensu();
					}			
				}
			};
			kartyPrzyciski.get(i).setOnAction(handler);
		}
			
	}
	
	private void graczWyrzucaKarte(){		// wczytuje potrzebne akcje do wyrzucenia karty
		komunikaty.setText("Wybierz kartê do wyrzucenia");
		przyciskiSterowania.getChildren().clear();
		dezaktywujDokladanie();
		wczytajKartyGracza();
		System.out.println(partia.getAutomatycznyGracz());
		for (int i = 0; i < kartyPrzyciski.size(); i++){
			final int I = i;
			EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>(){		

				public void handle(ActionEvent event) {
					int wygrany = partia.koniecKolejki(I);			// aktywuje dzia³ania na koniec kolejki (ruch komputerowego gracza itp)
					if(wygrany >= 0)
						loadWynikiScreen(wygrany);			// jeœli ktoœ wygra³, to wczytuje siê ekran z wynikami
					else
						graczPobieraKarte();			// jeœli nikt nie wygra³, to kolejka zaczyna siê od pocz¹tku
				}
			};
			kartyPrzyciski.get(i).setOnAction(handler);
		}
	}
	
	private void wczytajKartyGracza(){		// wczytanie przycisków z kartami gracza, na razie bez akcji
		partia.sortujKartyGracza();
		twojeKarty.getChildren().clear();
		kartyPrzyciski.clear();
		for (int i = 0; i < partia.liczbaKartGracza(); i++){
			Button button = new Button();
			kartyPrzyciski.add(button);
			Image imageDecline = new Image(getClass().getResourceAsStream("../Images/"+partia.getGracz().zobaczKarteID(i).getNrWTalii()+".png"));
			kartyPrzyciski.get(i).setGraphic(new ImageView(imageDecline));
			kartyPrzyciski.get(i).setPadding(Insets.EMPTY);
			kartyPrzyciski.get(i).setStyle("-fx-background-color: transparent;");
			twojeKarty.getChildren().add(kartyPrzyciski.get(i));
		}
	}
	
	
	private void loadWynikiScreen(int wygrany){			// wczytanie ekranu z wynikami gry
		FXMLLoader loader2 = new FXMLLoader(getClass().getResource("../fxml/WynikiScreen.fxml"));
		StackPane stackpane = null;
		try {
			stackpane = loader2.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		WynikiController wynikiController = loader2.getController();
		wynikiController.setWygrany(wygrany);
		wynikiController.setWyniki(partia.getWynikiGry());
		wynikiController.wyswietlWyniki();
		wynikiController.setMainController(mainController);
		mainController.setScreen(stackpane);
	}
	
	

	public void setMainController(MainController mainController) {		// zapisanie referencji do g³ównego ekranu
		this.mainController = mainController;
	}
	
}
