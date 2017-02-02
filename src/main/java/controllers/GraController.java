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
	private Label komunikaty;		// wy�wietla komunikaty dla gracza
	
	@FXML
	private HBox przyciskiSterowania;		// tu pojawiaj� si� przyciski do sterowania przebiegiem gry
	
	@FXML
	private FlowPane sekwensyNaStole;			// w �rodku wczytuj� si� grafiki kart w wy�o�onych sekwensach wraz z odpowiednimi przyciskami
	
	@FXML
	private FlowPane grupyNaStole;				// jak wy�ej, dla grup kart
	
	private MainController mainController;		// referencja do g��wnego kontrolera
	private Partia partia;						// klasa reprezentuj�ca pojedyncz� parti�
	private ArrayList<Button> kartyPrzyciski;			// listy przycisk�w, pozwalaj� na �atwy dost�p do nich (np. przy zmianie akcji)
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
	
	public void initialize(){			// po inicjalizacji automatycznie rozpoczyna parti�
		partia.rozpocznijPartie();
		wczytajKartyGracza();
		graczWyrzucaKarte();
	}
	

	private void graczPobieraKarte() {
		wczytajKartyNaStole();			// pokazuj� si� ju� wy�o�ono karty (ale jeszcze bez przyciskow)
		wczytajKartyGracza();			// pokazuj� si� karty nale��ce do gracza (grafika)
		komunikaty.setText("Karta wyrzucona przez przeciwnika: "+partia.getWyrzucona()+"\nWez kart� z talii lub wyrzucon� przez przeciwnika");
		Button zTalii = new Button("Karta z talii");				// stworzenie przycisk�w sterowania i ustawienie dla nich akcji
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
		przyciskiSterowania.getChildren().add(zTalii);	    //dodanie przycisk�w strowania do odpowiedniego panelu
		przyciskiSterowania.getChildren().add(odPrzeciwnika);
	}

	private void wczytajKartyNaStole() {
		// tworzy HBoxy z obrazkami kart, bez przycisk�w akcji
		sekwensyNaStole.getChildren().clear();
		grupyNaStole.getChildren().clear();
		sekwensyNaStolePrzyciski.clear();
		grupyNaStolePrzyciski.clear();
		for(int i = 0; i < partia.getKartyNaStole().getLiczbaSekwensow(); i++){	// wczytuje grafiki kart wy�o�onych na st�, na razie bez przycisk�w
			HBox h = new HBox();
			VBox v = new VBox();					// tutaj b�d� si� potem pojawia�y przyciski akcji do danego sekwensu
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
		for(int i = 0; i < partia.getKartyNaStole().getLiczbaGrup(); i++){		// jak wy�ej, ale dla grup kart
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
	
	private void aktywujDokladanie(){			// je�li mo�liwe jest dok�adanie kart, to tworzy odpowiednie przyciski akcji
		if (partia.czyMoznaDokladac()){		// czyMoznaDokladac - zwraca boola, czy mo�na w danym momencie dok�ada� karty
			for(int i = 0; i < sekwensyNaStole.getChildren().size(); i++){		// dla sekwensow
				final int I = i;
				Button dolozKarte = new Button("Do�� kart�");
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
					Button dolozKarte = new Button("Do�� kart�");
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
	
	private void dezaktywujDokladanie(){				// usuwa przyciski pozwalaj�ce na dok�adanie kart
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
		komunikaty.setText("Wybierz kart�, kt�r� chcesz do�o�y�");
		przyciskiSterowania.getChildren().clear();
		dezaktywujDokladanie();		// usuwa przyciski znajduj�ce si� przy wszystkich sekwensach i grupach
		final int ID = id;
		for (int i = 0; i < kartyPrzyciski.size(); i++){
			final int I = i;
			EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>(){		
				
				public void handle(ActionEvent event) {
					if (partia.czyJokerNaMiejscuOId(I)){
						dokladanieJokeraDoSekwensu(ID, I);
					}
					else{
						boolean czyDolozono = partia.sprobujDolozycDoSekwensuNr(ID, I); // sprawdza, czy dan� kart� mo�na do�o�y�, je�li tak to karta zostaje do�o�ona
						if (czyDolozono){
							komunikaty.setText("Karta zosta�a do�o�ona");		// informuje, czy kart� uda�o si� do�o�y�, czy nie
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
		Button zrezygnuj = new Button("Zrezygnuj z dok�adania kart");
		EventHandler<ActionEvent> h1 = new EventHandler<ActionEvent>(){		

			public void handle(ActionEvent event) {				// rezygnacja z dok�adania karty, wy�wielaj� si� g��wne opcje (wyk�adanie i wyrucenie karty)
					komunikaty.setText("Wy�� karty lub wyrzu� kart�");
					glowneAkcje();
					aktywujDokladanie();
				}
		};
		zrezygnuj.setOnAction(h1);
		przyciskiSterowania.getChildren().add(zrezygnuj);
		
	}
	
	private void dokladanieJokeraDoSekwensu(int nrSekwensu, int nrKarty){
		Button naPoczatku = new Button("Do�� na pocz�tku sekwensu");		// je�li dok�adany jest Joker, to gracz mo�e zdecydowa�, czy dok�ada go na pocz�tku czy na ko�cu sekwensu
		Button naKoncu = new Button("Do�� na ko�cu sekwensu");
		Button zrezygnuj = new Button("Zrezygnuj z dok�adania kart");			// tworzenie odpowiednych przycisk�w sterowania
		komunikaty.setText("Dok�adanie Jokera");
		for(int i = 0; i < kartyPrzyciski.size(); i++)
			kartyPrzyciski.get(i).setOnAction(null);
		final int NRSEKW = nrSekwensu;
		final int NRKARTY = nrKarty;
		EventHandler<ActionEvent> h1 = new EventHandler<ActionEvent>(){		

			public void handle(ActionEvent event) {
				boolean czyDolozono = partia.dolozJokeraNaPoczatkuSekwensuNr(NRSEKW, NRKARTY);		// dok�ada Jokera, a je�li si� nie uda�o wy�wietla komunikat
				if (czyDolozono){
					komunikaty.setText("Karta zosta�a do�o�ona");
					wczytajKartyNaStole();
					aktywujDokladanie();
					glowneAkcje();
				}
				else{
					komunikaty.setText("Nie mo�na do�o�y� Jokera przed dw�jk�\nani przed innym Jokerem");
				}
			}
		};
		EventHandler<ActionEvent> h2 = new EventHandler<ActionEvent>(){		

			public void handle(ActionEvent event) {
				boolean czyDolozono = partia.dolozJokeraNaKoncuSekwensuNr(NRSEKW, NRKARTY);		// jak wy�ej, ale Joker jest dok�adany na ko�cu sekwensu
				if (czyDolozono){
					komunikaty.setText("Karta zosta�a do�o�ona");
					wczytajKartyNaStole();
					aktywujDokladanie();
					glowneAkcje();
				}
				else{
					komunikaty.setText("Nie mo�na do�o�y� Jokera po asie\nani po innym Jokerze");
				}
			}
		};
		
		EventHandler<ActionEvent> h3 = new EventHandler<ActionEvent>(){		

			public void handle(ActionEvent event) {		// rezygnacja z dok�adania kart
					komunikaty.setText("Wy�� karty lub wyrzu� kart�");
					glowneAkcje();
					aktywujDokladanie();
				}
		};
		naPoczatku.setOnAction(h1);		// przypisanie akcji
		naKoncu.setOnAction(h2);
		zrezygnuj.setOnAction(h3);
		przyciskiSterowania.getChildren().clear();
		przyciskiSterowania.getChildren().add(naPoczatku);		// dodanie przycisk�w na ekran
		przyciskiSterowania.getChildren().add(naKoncu);
		przyciskiSterowania.getChildren().add(zrezygnuj);
		
	}
	
	private void dokladanieKartyDoGrupyNr(int id){		// po klikni�ciu w 'do�� kart�' przy danej grupie kkart
		komunikaty.setText("Wybierz kart�, kt�r� chcesz do�o�y�");
		przyciskiSterowania.getChildren().clear();
		dezaktywujDokladanie();
		final int ID = id;
		for (int i = 0; i < kartyPrzyciski.size(); i++){
			final int I = i;
			EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>(){		

				public void handle(ActionEvent event) {
					boolean czyDolozono = partia.sprobujDolozycDoGrupyNr(ID, I);
					if (czyDolozono){
						komunikaty.setText("Karta zosta�a do�o�ona");
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
		Button zrezygnuj = new Button("Zrezygnuj z dok�adania kart");
		EventHandler<ActionEvent> h1 = new EventHandler<ActionEvent>(){	

			public void handle(ActionEvent event) {
					komunikaty.setText("Wy�� karty lub wyrzu� kart�");
					glowneAkcje();
					aktywujDokladanie();
				}
		};
		zrezygnuj.setOnAction(h1);
		przyciskiSterowania.getChildren().add(zrezygnuj);
	}
	
	private void wezJokeraZGrupyNr(int id){
		komunikaty.setText("Wybierz kart�, kt�r� chcesz zamieni� na Jokera");
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
					komunikaty.setText("Wy�� karty lub wyrzu� kart�");
					glowneAkcje();
					aktywujDokladanie();
				}
		};
		zrezygnuj.setOnAction(h1);
		przyciskiSterowania.getChildren().add(zrezygnuj);
	}
	
	private void wezJokeraZSekwensuNr(int id){
		komunikaty.setText("Wybierz kart�, kt�r� chcesz zamieni� na Jokera");
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
					komunikaty.setText("Wy�� karty lub wyrzu� kart�");
					glowneAkcje();
					aktywujDokladanie();
				}
		};
		zrezygnuj.setOnAction(h1);
		przyciskiSterowania.getChildren().add(zrezygnuj);
	}
	
	private void pobranoKarteZTalii() {		// wczytuje odpowiednie pola, je�i gracz wybra� pobranie karty z talii
		przyciskiSterowania.getChildren().clear();
		Karta nowaKarta = partia.wezZTalii();
		komunikaty.setText("Pobrano kart� z talii: "+nowaKarta);
		aktywujDokladanie();
		glowneAkcje();
	}
	
	private void glowneAkcje(){		// aktywuje przyciski dla wyk�adania kart i wyrzucania karty (+przyciski dok�adania kart, je�li mo�na dok�ada�)
		wczytajKartyGracza();
		przyciskiSterowania.getChildren().clear();
		for(int i = 0; i < kartyPrzyciski.size(); i++)
			kartyPrzyciski.get(i).setOnAction(null);
		Button wyloz = new Button("Wy�� karty");
		Button wyrzuc = new Button ("Wyrzu� kart�");
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
	
	private void wykladanieKart(){			//tworzy przyciski pozwalaj�ce na wy�ozenie kart
		wczytajKartyGracza();
		przyciskiSterowania.getChildren().clear();		// punkty za wyk�adane karty
		
		Button sekwens = new Button("Dodaj sekwens");
		Button grupa = new Button("Dodaj grup� kart");
		Button zakoncz = new Button("Zatwierdz wy�o�one karty");
		Button zrezygnuj = new Button("Zrezygnuj z wyk�adania kart");
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
		komunikaty.setText("Punkty za wyk�adane karty: " + partia.getPunktyWylozenia());
	}
	
	
	
	protected void rezygnacjaZWykladania() {
		partia.oddajKartyGraczowi();
		wczytajKartyGracza();
		if(partia.maWyrzucona()){	// je�li gracz wzi�� kart� od poprzedniego gracza i jej nie wy�o�y�, to musi j� odda� i wzi�� kart� z talii
			komunikaty.setText("Nie wy�o�ono karty pobranej od przeciwnika,\nkarta zostaje zwr�cona, otrzymujesz kart� z talii");
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
			komunikaty.setText("Wy�� karty lub wyrzu� kart�");
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
			komunikaty.setText("Karty zosta�y wy�o�one na st�");
		}
	}
	
	
	protected void pierwszaKartaGrupy() { 		// rozpocz�cie tworzenia grupy kart
		przyciskiSterowania.getChildren().clear();
		partia.nowaGrupa();
		dodajKartyDoGrupy();
		
		Button zatwierdzGrupe = new Button("Zatwierdz grup� kart");
		EventHandler<ActionEvent> handler1 = new EventHandler<ActionEvent>(){		// przerzuci� do osobnej metody
			
			public void handle(ActionEvent event) {
				partia.zatwierdzGrupe();
				wykladanieKart();
			}
		};
		zatwierdzGrupe.setOnAction(handler1);
		przyciskiSterowania.getChildren().add(zatwierdzGrupe);
		zatwierdzGrupe.setVisible(false);
		
		Button zrezygnujZGrupy = new Button("Zrezygnuj z tworzenia grupy kart");
		EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>(){		// przerzuci� do osobnej metody
			
			public void handle(ActionEvent event) {
				partia.oddajGraczowiGrupe();
				wczytajKartyGracza();
				wykladanieKart();
			}
		};
		zrezygnujZGrupy.setOnAction(handler);
		przyciskiSterowania.getChildren().add(zrezygnujZGrupy);
	}
	
	private void dodajKartyDoGrupy(){			// dodawanie kolejnych kart do tworzonej w�a�nie grupy kart
		if (partia.czyMoznaZatwierdzicGrupe()){
			przyciskiSterowania.getChildren().get(0).setVisible(true);		// je�li liczba kart w grupie >= 3, to aktywuje si� przycisk zatwierdzenia grupy
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
	
	protected void pierwszaKartaSekwensu(){			// rozpocz�cie tworzenia nowego sekwensu
		przyciskiSterowania.getChildren().clear();
		partia.nowySekwens();
		dodajKartyDoSekwensu();
		
		Button zatwierdzSekwens = new Button("Zatwierdz sekwens");
		EventHandler<ActionEvent> handler1 = new EventHandler<ActionEvent>(){		// przerzuci� do osobnej metody
			
			public void handle(ActionEvent event) {
				partia.zatwierdzSekwens();
				wykladanieKart();
			}
		};
		zatwierdzSekwens.setOnAction(handler1);
		przyciskiSterowania.getChildren().add(zatwierdzSekwens);
		zatwierdzSekwens.setVisible(false);
		
		Button zrezygnujZSekwensu = new Button("Zrezygnuj z tworzenia sekwensu");
		EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>(){		// przerzuci� do osobnej metody
			
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
		komunikaty.setText("Wybierz kart� do wyrzucenia");
		przyciskiSterowania.getChildren().clear();
		dezaktywujDokladanie();
		wczytajKartyGracza();
		System.out.println(partia.getAutomatycznyGracz());
		for (int i = 0; i < kartyPrzyciski.size(); i++){
			final int I = i;
			EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>(){		

				public void handle(ActionEvent event) {
					int wygrany = partia.koniecKolejki(I);			// aktywuje dzia�ania na koniec kolejki (ruch komputerowego gracza itp)
					if(wygrany >= 0)
						loadWynikiScreen(wygrany);			// je�li kto� wygra�, to wczytuje si� ekran z wynikami
					else
						graczPobieraKarte();			// je�li nikt nie wygra�, to kolejka zaczyna si� od pocz�tku
				}
			};
			kartyPrzyciski.get(i).setOnAction(handler);
		}
	}
	
	private void wczytajKartyGracza(){		// wczytanie przycisk�w z kartami gracza, na razie bez akcji
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
	
	

	public void setMainController(MainController mainController) {		// zapisanie referencji do g��wnego ekranu
		this.mainController = mainController;
	}
	
}
