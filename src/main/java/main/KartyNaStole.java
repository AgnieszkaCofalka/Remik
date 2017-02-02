package main;
import java.util.ArrayList;

public class KartyNaStole {		// reprezentuje karty wy³o¿one na stó³
	private ArrayList<GrupaKart> grupyKart;
	
	private ArrayList<Sekwens> sekwensy;
	
	public KartyNaStole(){
		grupyKart = new ArrayList<GrupaKart>();
		sekwensy = new ArrayList<Sekwens>();
	}
	
	public int getLiczbaGrup(){		// zwraca liczbê grup kart
		return grupyKart.size();
	}
	
	public int getLiczbaSekwensow(){		// zwraca liczbê sekwensów
		return sekwensy.size();
	}
	
	public void dodajGrupeKart(GrupaKart grupa){		// dodaje now¹ grupê kart na stó³
		grupyKart.add(grupa);
	}
	
	public void dodajSekwens(Sekwens sekwens){		// dodaje nowy sekwens na stó³
		sekwensy.add(sekwens);
	}
	
	public boolean dolozKarteDoSekwensuNr(Karta karta, int nr){
		boolean czyDolozono = sekwensy.get(nr).dodajKarteDoWylozonegoSekwensu(karta);
		return czyDolozono;
	}
	
	public boolean dolozKarteDoGrupyNr(Karta karta, int nr){
		boolean czyDolozono = grupyKart.get(nr).dodajKarte(karta);
		return czyDolozono;
	}
	
	public boolean dolozJokeraNaKoncuSekwensu(int nrSekwensu, Karta k){
		boolean czyDolozono = sekwensy.get(nrSekwensu).dolozJokeraNaKoncu(k);
		return czyDolozono;
	}
	
	public boolean dolozJokeraNaPoczatkuSekwensu(int nrSekwensu, Karta k){
		boolean czyDolozono = sekwensy.get(nrSekwensu).dolozJokeraNaPoczatku(k);
		return czyDolozono;
	}
	
	
	public Karta wezJokeraZSekwensuNr(Karta kartaDoZamiany, int nr){
		Karta karta = sekwensy.get(nr).wezJokera(kartaDoZamiany);
		return karta;
	}
	
	public Karta wezJokeraZGrupyNr(Karta kartaDoZamiany, int nr){
		Karta karta = grupyKart.get(nr).wezJokera(kartaDoZamiany);
		return karta;
	} 
	
	public boolean czyJestJokerWSekwensie(int nrSekwensu){
		return sekwensy.get(nrSekwensu).czyJestJoker();
	}
	
	public boolean czyJestJokerWGrupie(int nrGrupy){
		return grupyKart.get(nrGrupy).czyJestJoker();
	}
	
	public Karta zobaczKarteZSekwensuNr(int IDKarty, int nrSekwensu){
		Karta k = new Karta(sekwensy.get(nrSekwensu).zobaczKarte(IDKarty));
		return k;
		
	}
	
	public Karta zobaczKarteZGrupyNr(int IDKarty, int nrGrupy){
		Karta k = new Karta(grupyKart.get(nrGrupy).zobaczKarte(IDKarty));
		return k;
	}
	
	public int liczbaKartWSekwensieNr(int nrSekwensu){
		return sekwensy.get(nrSekwensu).liczbaKart();
	}
	
	public int liczbaKartWGrupieNr(int nrGrupy){
		return grupyKart.get(nrGrupy).liczbaKart();
	}
	
	public void wyczyscStol(){
		sekwensy.clear();
		grupyKart.clear();
	}

}
