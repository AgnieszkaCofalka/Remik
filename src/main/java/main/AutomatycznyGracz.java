package main;

import java.util.ArrayList;
import java.util.LinkedList;

public class AutomatycznyGracz extends Gracz{	// reprezentuje graj¹cy z u¿ytkownikiem komputer
	
	private Partia partia;
	private ArrayList<Sekwens> sekwensyDoWylozenia;		// listy tworzone przy wyk³adaniu kart, po wy³o¿eniu kart kopiowane na stó³
	private ArrayList<GrupaKart> grupyDoWylozenia;
	private LinkedList<Karta> dwojkiDoSekwensow;		// na parzystych indeksach jest pocz¹tek nowej dwójki
	private LinkedList<Karta> dwojkiDoGrup;
	
	public void setPartia(Partia partia) {		// zapisanie referencji do partii gry
		this.partia = partia;
	}


	public AutomatycznyGracz(ArrayList<Karta> karty){		// konstruktor
		for(Karta karta : karty){
			Karta nowaKarta = new Karta(karta);
			this.karty.add(nowaKarta);
		}
		this.sortujKarty();
		dwojkiDoSekwensow = new LinkedList<Karta>();
		dwojkiDoGrup = new LinkedList<Karta>();
		sekwensyDoWylozenia = new ArrayList<Sekwens>();
		grupyDoWylozenia = new ArrayList<GrupaKart>();
	}
	
	
	public Karta wykonajRuch(){					// schemat postêpowania w ka¿dym ruchu
		karty.add(partia.kartaDlaAutomatycznegoGracza());
		this.sortujKarty();
		System.out.println(this);
		dwojkiDoSekwensow.clear();
		dwojkiDoGrup.clear();
		Karta doWyrzucenia = null;
		if (this.liczbaKart() >= 12)
			doWyrzucenia = przedPierwszymWylozeniem();
		else
			doWyrzucenia = poPierwszymWylozeniu();
		return doWyrzucenia;
	}
	
	

	private Karta przedPierwszymWylozeniem() {		//schemat akcji przed pierwszym wy³o¿eniem kart
		szukajSekwensow();
		szukajGrup();
		szukajDwojekSekwensy();
		szukajDwojekGrupy();
		dolaczJokera();
		boolean czyWylozono = sprawdzCzyMoznaWylozycPierwszyRaz();			// od razu wyk³ada, jeœli siê da
		if(czyWylozono){
			sprobujDolozyc();
		}
		Karta doWyrzucenia =  szukajKartyDoWyrzucenia();
		System.out.println(this);
		if (!czyWylozono)
			zwrocWykladaneKarty();		// jeœli nie wy³o¿y³ kart, do karty z sekwensow/GrupDoWylozenia wracaj¹ do niego
		
		return doWyrzucenia;
	}
	
	private void sprobujDolozyc() {		// sprawdza, czy mo¿e wzi¹æ Jokera ze sto³u lub do³o¿yæ kartê na stó³
		for(int i = 0; i < partia.getKartyNaStole().getLiczbaGrup(); i++){
			for(int j = 0; j < liczbaKart();j++){
				Karta joker = partia.getKartyNaStole().wezJokeraZGrupyNr(zobaczKarteID(j), i);
				if(joker != null){
					zagrajKartaId(j);
					dodajKarte(joker);
				}
			}
			for(int j = 0; j < liczbaKart();j++){
				boolean czyDolozono = partia.getKartyNaStole().dolozKarteDoGrupyNr(zobaczKarteID(j), i);
				if(czyDolozono)
					zagrajKartaId(j);
			}
		}
		for(int i = 0; i < partia.getKartyNaStole().getLiczbaSekwensow(); i++){
			for(int j = 0; j < liczbaKart();j++){
				Karta joker = partia.getKartyNaStole().wezJokeraZSekwensuNr(zobaczKarteID(j), i);
				if(joker != null){
					zagrajKartaId(j);
					dodajKarte(joker);
				}
			}
			for(int j = 0; j < liczbaKart();j++){
				boolean czyDolozono = partia.getKartyNaStole().dolozKarteDoSekwensuNr(zobaczKarteID(j), i);
				if(czyDolozono)
					zagrajKartaId(j);
			}
		}
		
	}


	private void sprobujWylozycKolejnyRaz() {		// wyk³ada wszystkie karty, jakie mo¿e
		for(int i = 0; i < sekwensyDoWylozenia.size(); i++)
			partia.getKartyNaStole().dodajSekwens(sekwensyDoWylozenia.get(i));
		for(int i = 0; i < grupyDoWylozenia.size(); i++)
			partia.getKartyNaStole().dodajGrupeKart(grupyDoWylozenia.get(i));
		sekwensyDoWylozenia.clear();
		grupyDoWylozenia.clear();
		
	}


	private void zwrocWykladaneKarty() {		// karty z sekwensów i grup, które nie zosta³y wy³o¿one, wracaj¹ do komputerowego gracza
		for(int i = 0; i < liczbaWykladanychSekwensow(); i++){
			for(int j = 0; j < sekwensyDoWylozenia.get(i).liczbaKart(); j++)
				System.out.println(sekwensyDoWylozenia.get(i).zobaczKarte(j));
			System.out.println("\n");
		}
		for(int i = 0; i < liczbaWykladanychGrup(); i++){
			for(int j = 0; j < grupyDoWylozenia.get(i).liczbaKart(); j++)
				System.out.println(grupyDoWylozenia.get(i).zobaczKarte(j));
			System.out.println("\n");
		}
		for (int i = 0; i < sekwensyDoWylozenia.size(); i++){
			int liczbaKart = sekwensyDoWylozenia.get(i).liczbaKart();
			for(int j = 0; j < liczbaKart; j++)
				this.dodajKarte(sekwensyDoWylozenia.get(i).usunKarte(0));
		}
		for (int i = 0; i < grupyDoWylozenia.size(); i++){
			int liczbaKart = grupyDoWylozenia.get(i).liczbaKart();
			for(int j = 0; j < liczbaKart; j++)
				this.dodajKarte(grupyDoWylozenia.get(i).usunKarte(0));
		}
		sekwensyDoWylozenia.clear();
		grupyDoWylozenia.clear();
		this.sortujKarty();
	}


	private boolean sprawdzCzyMoznaWylozycPierwszyRaz() {		// sprawdza, czy wyk³adane karty spe³niaj¹ warunki, jeœli karty s¹ wyk³adane 1. raz
		boolean czyWylozono = false;							// jeœli tak, to od razu je wyk³ada
		if(policzPunktyZaKarty() >= 51 && sekwensyDoWylozenia.size() > 0){
			for(int i = 0; i < sekwensyDoWylozenia.size(); i++)
				partia.getKartyNaStole().dodajSekwens(sekwensyDoWylozenia.get(i));
			for(int i = 0; i < grupyDoWylozenia.size(); i++)
				partia.getKartyNaStole().dodajGrupeKart(grupyDoWylozenia.get(i));
			sekwensyDoWylozenia.clear();
			grupyDoWylozenia.clear();
			czyWylozono = true;
		}
		return czyWylozono;
	}
	
	private int policzPunktyZaKarty(){		// zlicza punkty za wszystkie karty, które da siê wy³o¿yæ
		int punkty = 0;
		for (int i = 0; i < sekwensyDoWylozenia.size(); i++)
			punkty += sekwensyDoWylozenia.get(i).punktyZaSekwens();
		for (int i = 0; i < grupyDoWylozenia.size(); i++)
			punkty += grupyDoWylozenia.get(i).punktyZaGrupe();
		return punkty;
	}


	private void dolaczJokera() {				// do³¹cza Jokera do jednej z dwójek kart
		if (this.czyMaKarteKolor(Kolor.Joker)){
			if(sekwensyDoWylozenia.size() == 0){
				int indeks = najwyzszaDwojkaSekwens();
				if (indeks >= 0){
					sekwensZJokerem(indeks);
					return;
				}
			}
			int indeksS = najwyzszaDwojkaSekwens();
			int indeksG = najwyzszaDwojkaGrupa();
			int maxS = 0;
			if (indeksS >= 0)
				maxS = dwojkiDoSekwensow.get(indeksS).getLiczbaPunktow() + dwojkiDoSekwensow.get(indeksS + 1).getLiczbaPunktow();
			int maxG = 0;
			if (indeksG >= 0)
				maxG = dwojkiDoGrup.get(indeksG).getLiczbaPunktow() + dwojkiDoGrup.get(indeksG + 1).getLiczbaPunktow();
			if (maxS > maxG){
				sekwensZJokerem(indeksS);
				if (zobaczKarteID(liczbaKart() - 1).getKolor().equals(Kolor.Joker))
					dolaczJokera();
			}
			else if(indeksG >= 0){
				grupaZJokerem(indeksG);
				if (zobaczKarteID(liczbaKart() - 1).getKolor().equals(Kolor.Joker))
					dolaczJokera();
			}
			else{
				dwojkiDoSekwensow.add(zobaczKarteID(liczbaKart() - 1)); // ¿eby nie wyrzuciæ Jokera
				if (zobaczKarteID(liczbaKart() - 2).getKolor().equals(Kolor.Joker))
					dwojkiDoSekwensow.add(zobaczKarteID(liczbaKart() - 2));
			}
		}
	}
	
	private void grupaZJokerem(int indeks){		// tworzy z dwójki i Jokera grupê kart
		GrupaKart grupa = new GrupaKart();
		grupa.dodajKarte(dwojkiDoGrup.get(indeks));
		grupa.dodajKarte(dwojkiDoGrup.get(indeks + 1));
		grupa.dodajKarte(zagrajKartaId(liczbaKart() - 1));
		this.zagrajKarta(dwojkiDoGrup.get(indeks).getNrWTalii());
		this.zagrajKarta(dwojkiDoGrup.get(indeks + 1).getNrWTalii());
		dwojkiDoGrup.remove(indeks + 1);
		dwojkiDoGrup.remove(indeks);
		grupyDoWylozenia.add(grupa);
	}
	
	private void sekwensZJokerem(int indeks){			// jak wy¿ej, ale sekwens
	
			Sekwens sekwens = new Sekwens();		// ni¿ej sprawdzana jest kolejnoœæ
			if (dwojkiDoSekwensow.get(indeks).getNumer() + 1 == dwojkiDoSekwensow.get(indeks + 1).getNumer()){
				if (dwojkiDoSekwensow.get(indeks + 1).getNumer() == 14){
					sekwens.dodajKarte(zagrajKartaId(liczbaKart() - 1));
					sekwens.dodajKarte(dwojkiDoSekwensow.get(indeks));
					sekwens.dodajKarte(dwojkiDoSekwensow.get(indeks + 1));
					
				}
				else{
					sekwens.dodajKarte(dwojkiDoSekwensow.get(indeks));
					sekwens.dodajKarte(dwojkiDoSekwensow.get(indeks + 1));
					sekwens.dodajKarte(zagrajKartaId(liczbaKart() - 1));
				}
			}
			else{
				sekwens.dodajKarte(dwojkiDoSekwensow.get(indeks));
				sekwens.dodajKarte(zagrajKartaId(liczbaKart() - 1));
				sekwens.dodajKarte(dwojkiDoSekwensow.get(indeks + 1));
			}
			this.zagrajKarta(dwojkiDoSekwensow.get(indeks).getNrWTalii());
			this.zagrajKarta(dwojkiDoSekwensow.get(indeks + 1).getNrWTalii());
			dwojkiDoSekwensow.remove(indeks + 1);
			dwojkiDoSekwensow.remove(indeks);
			sekwensyDoWylozenia.add(sekwens);
	}
	
	private int najwyzszaDwojkaSekwens(){		// znajduje indeks dla dwójki sekwensowej o najwy¿szej wartoœci
		int indeks = -1;
		int maxWartosc = 0;
		for(int i = 0; i < dwojkiDoSekwensow.size() - 1; i += 2){
			int wartosc = dwojkiDoSekwensow.get(i).getLiczbaPunktow();
			wartosc += dwojkiDoSekwensow.get(i + 1).getLiczbaPunktow();
			if (wartosc > maxWartosc){
				maxWartosc = wartosc;
				indeks = i;
			}
		}
		return indeks;
	}
	
	private int najnizszaDwojkaSekwens(){ // jak wy¿ej, ale wartoœæ najni¿sza
		int indeks = -1;
		int minWartosc = 200;
		for(int i = 0; i < dwojkiDoSekwensow.size() - 1; i += 2){
			int wartosc = dwojkiDoSekwensow.get(i).getLiczbaPunktow();
			wartosc += dwojkiDoSekwensow.get(i + 1).getLiczbaPunktow();
			if (wartosc < minWartosc){
				minWartosc = wartosc;
				indeks = i;
			}
		}
		return indeks;
	}
	
	private int najwyzszaDwojkaGrupa(){		// znajduje dwójkê dla grup o max., ni¿ej min. wartoœci
		int indeks = -1;
		int maxWartosc = 0;
		for(int i = 0; i < dwojkiDoGrup.size() - 1; i += 2){
			int wartosc = dwojkiDoGrup.get(i).getLiczbaPunktow();
			wartosc += dwojkiDoGrup.get(i + 1).getLiczbaPunktow();
			if (wartosc > maxWartosc){
				maxWartosc = wartosc;
				indeks = i;
			}
		}
		return indeks;
	}
	
	private int najnizszaDwojkaGrupa(){
		int indeks = -1;
		int minWartosc = 200;
		for(int i = 0; i < dwojkiDoGrup.size() - 1; i += 2){
			int wartosc = dwojkiDoGrup.get(i).getLiczbaPunktow();
			wartosc += dwojkiDoGrup.get(i + 1).getLiczbaPunktow();
			if (wartosc < minWartosc){
				minWartosc = wartosc;
				indeks = i;
			}
		}
		return indeks;
	}

	private Karta szukajKartyDoWyrzucenia() {			// wybiera kartê, któr¹ nale¿y wyrzuciæ
		for(int i = 0; i < liczbaKart(); i++){
			if (!czyJestWDwojkach(zobaczKarteID(i))){
				System.out.println("Wyrzucana: "+zobaczKarteID(i));
					return zagrajKartaId(i);
			}
		}
		int indeksDwojkiS = najnizszaDwojkaSekwens();
		int indeksDwojkiG = najnizszaDwojkaGrupa();
		int minDwojkiS = 0;
		if (indeksDwojkiS > -1)
			minDwojkiS = dwojkiDoSekwensow.get(indeksDwojkiS).getLiczbaPunktow() + dwojkiDoSekwensow.get(indeksDwojkiS + 1).getLiczbaPunktow();
		int minDwojkiG = 0;
		if (indeksDwojkiS > -1)
			minDwojkiG = dwojkiDoGrup.get(indeksDwojkiG).getLiczbaPunktow() + dwojkiDoGrup.get(indeksDwojkiG + 1).getLiczbaPunktow();

		System.out.println(indeksDwojkiS+", "+minDwojkiS+"\n"+indeksDwojkiG+", "+minDwojkiG);
		if(minDwojkiG > 0 && minDwojkiG <= minDwojkiS){
			Karta doWyrzucenia = new Karta(dwojkiDoGrup.get(indeksDwojkiG));
			zagrajKarta(dwojkiDoGrup.get(indeksDwojkiG).getNrWTalii());
			dwojkiDoGrup.remove(indeksDwojkiG + 1);
			dwojkiDoGrup.remove(indeksDwojkiG);
			return doWyrzucenia;
		}
		else if (minDwojkiS > 0){
			Karta doWyrzucenia = new Karta(dwojkiDoSekwensow.get(indeksDwojkiS));
			zagrajKarta(dwojkiDoSekwensow.get(indeksDwojkiS).getNrWTalii());
			dwojkiDoSekwensow.remove(indeksDwojkiS + 1);
			dwojkiDoSekwensow.remove(indeksDwojkiS);
			return doWyrzucenia;
		}
		else{
			if (najslabszaGrupa() > -1){
				for(int i = 0; i < grupyDoWylozenia.get(najslabszaGrupa()).liczbaKart(); i++){
					this.dodajKarte(grupyDoWylozenia.get(najslabszaGrupa()).zobaczKarte(i));
				}
				grupyDoWylozenia.remove(najslabszaGrupa());
				for(int i = 0; i < liczbaKart(); i++){
					if (!czyJestWDwojkach(zobaczKarteID(i))){
						System.out.println("Wyrzucana: "+zobaczKarteID(i));
						Karta doWyrzucenia = zagrajKartaId(i);
						return doWyrzucenia;
					}
				}
			}
			if (najslabszySekwens() > -1){
				for(int i = 0; i < sekwensyDoWylozenia.get(najslabszySekwens()).liczbaKart(); i++){
					this.dodajKarte(sekwensyDoWylozenia.get(najslabszySekwens()).zobaczKarte(i));
				}
				sekwensyDoWylozenia.remove(najslabszySekwens());
				for(int i = 0; i < liczbaKart(); i++){
					if (!czyJestWDwojkach(zobaczKarteID(i))){
						System.out.println("Wyrzucana: "+zobaczKarteID(i));
						Karta doWyrzucenia = zagrajKartaId(i);
						return doWyrzucenia;
					}
				}
			}
			
		}
		
		
		return zagrajKartaId(0);
	}
	
	private int najslabszaGrupa(){		// znajduje grupê kart o najmniejszej wartoœci
		int id = -1;
		int minWartosc = 500;
		for(int i = 0; i < grupyDoWylozenia.size(); i++){
			if(grupyDoWylozenia.get(i).punktyZaGrupe() < minWartosc){
				minWartosc = grupyDoWylozenia.get(i).punktyZaGrupe();
				id = i;
			}
		}
		return id;
	}
	
	private int najslabszySekwens(){		// znajduje sekwens o najni¿szej wartoœci
		int id = -1;
		int minWartosc = 500;
		for(int i = 0; i < sekwensyDoWylozenia.size(); i++){
			if(sekwensyDoWylozenia.get(i).punktyZaSekwens() < minWartosc){
				minWartosc = sekwensyDoWylozenia.get(i).punktyZaSekwens();
				id = i;
			}
		}
		return id;
	}
	
	private boolean czyJestWDwojkach(Karta k){			// sprawdza, czy dana karta zawiera siê w listach dwójek kart
		for(int i = 0; i < dwojkiDoSekwensow.size(); i++){
			if (dwojkiDoSekwensow.get(i).equals(k))
				return true;
		}
		for(int i = 0; i < dwojkiDoGrup.size(); i++){
			if (dwojkiDoGrup.get(i).equals(k))
				return true;
		}
		return false;
	}


	private void szukajDwojekGrupy() {				// znajduje dwójki kart nadaj¹ce siê na grupy i dok³ada je do listy
		for(int i = 0; i < this.liczbaKart(); i++){
			int licznik = 0;
			for (int j = karty.get(i).getNrWTalii(); j <= 52; j += 13)
				if (this.czyMaKarteNrWTalii(j))
					licznik++;
			if(licznik == 2){
				for (int j = karty.get(i).getNrWTalii(); j <= 52; j += 13)
					if (this.czyMaKarteNrWTalii(j)){
						Karta k = new Karta(j);
						dwojkiDoGrup.add(k);
					}
			}
		}
	}


	private void szukajDwojekSekwensy() {		// jak wy¿ej, ale na sekwensy
		System.out.println("Dwojki sekwensow: ");
		for(int i = 0; i < liczbaKart() - 2; i++){
			if ((karty.get(i).getNumer() + 1 == karty.get(i+1).getNumer() || (karty.get(i).getNumer() + 2 == karty.get(i+1).getNumer())&& karty.get(i).getKolorN() == karty.get(i+1).getKolorN() && !(karty.get(i+1).getKolor().equals(Kolor.Joker)))){
				dwojkiDoSekwensow.add(karty.get(i));
				dwojkiDoSekwensow.add(karty.get(i + 1));
			}
		}
	}


	private void szukajGrup() {		// szuka grup kart, dodaje je do listy (i usuwa odpowiednie karty z rêki gracza komp.)
		for(int i = 0; i < this.liczbaKart() && (karty.get(i).getKolor().equals(Kolor.Trefl) || karty.get(i).getKolor().equals(Kolor.Karo)); i++){
			int licznik = 0;
			for (int j = karty.get(i).getNrWTalii() % 13; j <= 52; j += 13)
				if (this.czyMaKarteNrWTalii(j))
					licznik++;
			if(licznik >= 3){
				GrupaKart grupa = new GrupaKart();
				for (int j = karty.get(i).getNrWTalii() % 13; j <= 52; j += 13)
					if (this.czyMaKarteNrWTalii(j)){
						grupa.dodajKarte(zagrajKarta(j));
					}
				grupyDoWylozenia.add(grupa);
			}
		}
	}
	
	private GrupaKart szukajGrupyDlaKarty(Karta karta){			// szuka grupy kart zawieraj¹cej konkretn¹ kartê
		GrupaKart grupa = null;
		int licznik = 0;
		for (int j = karta.getNrWTalii() % 13; j <= 52; j += 13)
			if (this.czyMaKarteNrWTalii(j))
				licznik++;
		if(licznik >= 3){
			grupa = new GrupaKart();
			for (int j = karta.getNrWTalii() % 13; j <= 52; j += 13)
				if (this.czyMaKarteNrWTalii(j)){
					grupa.dodajKarte(zagrajKarta(j));
				}
			grupyDoWylozenia.add(grupa);
		}
		return grupa;
	}
	

	private void szukajSekwensow() {		// szuka sekwensów, dodaje je do listy, usuwa odpowiednie karty z rêki gracza komp.
		for(int i = 0; i < liczbaKart() - 2; i++){
			if (karty.get(i).getNumer() + 1 == karty.get(i+1).getNumer() && karty.get(i).getKolorN() == karty.get(i+1).getKolorN()){
				if (karty.get(i+1).getNumer() + 1 == karty.get(i+2).getNumer() && karty.get(i+1).getKolorN() == karty.get(i+2).getKolorN()){
					Sekwens sekwens = new Sekwens();
					sekwens.dodajKarte(zobaczKarteID(i));
					sekwens.dodajKarte(zobaczKarteID(i + 1));	
					sekwens.dodajKarte(zobaczKarteID(i + 2));
					i += 2;
					for(; i < liczbaKart() - 1 && (karty.get(i).getNumer() + 1 == karty.get(i+1).getNumer() && karty.get(i).getKolorN() == karty.get(i+1).getKolorN()); i++){
						GrupaKart grupa = this.szukajGrupyDlaKarty(karty.get(i+1));
						if(grupa == null){
							sekwens.dodajKarte(zobaczKarteID(i + 1));
						}
						else
							break;
					}
					for(int j = 0; j < sekwens.liczbaKart() - 3; j++){
						GrupaKart grupa = this.szukajGrupyDlaKarty(sekwens.zobaczKarte(j));
						if(grupa != null){
							for(int k = j; k >= 0; k--){
								sekwens.usunKarte(k);
							}
						}
					}
					sekwensyDoWylozenia.add(sekwens);
				}
				else
					i++;
			}
		}
		for (int i = 0; i < sekwensyDoWylozenia.size(); i++){
			int liczbaKart = sekwensyDoWylozenia.get(i).liczbaKart();
			for(int j = 0; j < liczbaKart; j++)
				this.zagrajKarta(sekwensyDoWylozenia.get(i).zobaczKarte(j).getNrWTalii());
		}
	}

	private Karta poPierwszymWylozeniu() {		// schemat dzia³añ w ruchu po pierwszym wy³o¿eniu kart
		szukajSekwensow();
		szukajGrup();
		szukajDwojekSekwensy();
		szukajDwojekGrupy();
		dolaczJokera();
		sprobujDolozyc();
		Karta doWyrzucenia =  szukajKartyDoWyrzucenia();
		sprobujWylozycKolejnyRaz();
		zwrocWykladaneKarty();
		return doWyrzucenia;
	}
	
	private int liczbaWykladanychSekwensow(){
		return sekwensyDoWylozenia.size();
	}
	
	private int liczbaWykladanychGrup(){
		return grupyDoWylozenia.size();
	}
	
	
}
