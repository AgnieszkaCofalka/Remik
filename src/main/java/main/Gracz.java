package main;

//klasa reprezentuje karty trzymane przez gracza w r�ku

import java.util.*;

public class Gracz {
	
	protected ArrayList<Karta> karty = new ArrayList<Karta>();	// karty nale��ce do danego gracza
	
	// konstruktory:
	
	public Gracz(){
		// brak kart
	}
	
	public Gracz(int[] nryKart){		
		for(int i = 0; i < nryKart.length; i++){
			Karta karta = new Karta(nryKart[i]);
			karty.add(karta);
		}
	}
	
	public Gracz(Karta[] karty){
		for(int i = 0; i < karty.length; i++){
			Karta karta = new Karta(karty[i]);
			this.karty.add(karta);
		}
	}
	
	public Gracz(ArrayList<Karta> karty){
		for(Karta karta : karty){
			Karta nowaKarta = new Karta(karta);
			this.karty.add(nowaKarta);
		}
	}
	
	public void dodajKarte(Karta karta){		// dodawanie pojedynczej karty
		karty.add(karta);
	}
	
	public void dodajKarte(int nrWTalii){
		Karta karta = new Karta(nrWTalii);
		karty.add(karta);
	}
	
	public int liczbaKart(){		// zwraca aktualn� liczb� kart w r�ku gracza
		return karty.size();
	}
	
	public void sortujKarty(){
		Collections.sort(karty);
	}
	
	public boolean czyMaKarteNrWTalii(int nrWTalii){
		for(Karta karta : karty){
			if (karta.getNrWTalii() == nrWTalii)
				return true;
		}
		return false;
	}
	
	public boolean czyMaKarteNumer(int numer){
		for(Karta karta : karty){
			if (karta.getNumer() == numer)
				return true;
		}
		return false;
	}
	
	public boolean czyMaKarteKolor(Kolor kolor){
		for(Karta karta : karty){
			if (karta.getKolor() == kolor)
				return true;
		}
		return false;
	}
	
	public Karta zobaczKarteID(int id){
		if (id > karty.size() - 1)
			return null;
		Karta karta = karty.get(id);
		return karta;
	}
	
	// pobiera kart� o okre�lonym indeksie i usuwa j� z r�ki gracza
	public Karta zagrajKartaId(int indeks){		
		if (indeks > karty.size() - 1)
			return null;
		Karta karta = karty.get(indeks);
		karty.remove(indeks);
		return karta;
	}
	
	//pobiera kart� o danym numerze w talii i usuwa j� u gracza
	public Karta zagrajKarta(int nrWTalii){
		int size = karty.size();
		for(int i = 0; i < size; i++){
			Karta karta = karty.get(i);
			if (karta.getNrWTalii() == nrWTalii){
				karty.remove(i);
				return karta;
			}	
		}
		return null;
	}
	
	public int podliczPunkty(){		// wywo�ywana na koniec partii, podlicza punkty z pozosta�ych u gracza kart
		int punkty = 0;
		for (int i = 0; i < liczbaKart(); i++)
			punkty += karty.get(i).getLiczbaPunktow();
		return punkty;
	}
	
	public String toString(){
		if (karty.isEmpty())
			return "Brak kart";
		StringBuilder sb = new StringBuilder("");
		int i = 1;
		for(Karta karta : karty){
			sb.append(i+": "+karta.toString());
			sb.append("\n");
			i++;
		}	// tworzy list� posiadanych przez gracza kart (w formie stringa)
		return sb.toString();
	}
	
	public String skrotyKart(){ //dzia�a jak powy�sza, ale u�ywa skr�t�w nazw kart
		if (karty.isEmpty())
			return "Brak kart";
		StringBuilder sb = new StringBuilder("");
		for(Karta karta : karty){
			sb.append(karta.skrotNazwy());
			sb.append("\n");
		}	// tworzy list� posiadanych przez gracza kart (w formie stringa)
		return sb.toString();
	}
	
}
