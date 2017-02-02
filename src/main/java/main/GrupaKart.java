package main;
import java.util.ArrayList;

public class GrupaKart{	// GrupaKart to np. 3 ósemki
		
	private ArrayList<Karta> karty;		// lista kart w danej grupie
	
	public GrupaKart(){				// konstruktory
		karty = new ArrayList<Karta>();
	}
	
	public GrupaKart(Karta k){
		dodajKarte(k);
	}
	
	public GrupaKart(ArrayList<Karta> karty){
		for(Karta karta : karty){
			Karta nowaKarta = new Karta(karta);
			this.karty.add(nowaKarta);
		}
	}
	
	public int liczbaKart(){	// liczba kart w grupie
		return karty.size();
	}
	
	public Karta wezJokera(Karta k){		// podmienienie jokera le¿¹cego na stole na odpowiadaj¹c¹ mu kartê
		Karta joker = null;
		boolean czyDodano = dodajKarte(k);
		if(liczbaKart() > 3 && czyDodano){
			for(int i = 0; i < liczbaKart(); i++){
				if (karty.get(i).getKolor().equals(Kolor.Joker)){
					joker = karty.get(i);
					karty.remove(i);
					return joker;
				}
			}
			
		}
		karty.remove(k);
		return null;		
	}
	
	public boolean dodajKarte(Karta k){		// dodanie pojedynczej karty do grupy (przyjmuje kartê)
		boolean czyDodano = false;
		if (liczbaKart() != 0){		// ograniczenie: pierwsza karta nie mo¿e byæ jokerem
			if(karty.get(0).getNumer() == k.getNumer() || k.getKolor().equals(Kolor.Joker)){
				karty.add(k);
				czyDodano = true;
			}
		}
		else if (liczbaKart() == 0 && !(k.getKolor().equals(Kolor.Joker))){
			karty.add(k);
			czyDodano = true;
		}
		return czyDodano;
	}
	
	public boolean dodajKarte(int nrWTalii){		//jw., przyjmuje nrWTalii
		Karta k = new Karta(nrWTalii);
		boolean czyDodano = false;
		if (liczbaKart() != 0){		// ograniczenie: pierwsza karta nie mo¿e byæ jokerem
			if(karty.get(0).getNumer() == k.getNumer() || k.getKolor().equals(Kolor.Joker)){
				karty.add(k);
				czyDodano = true;
			}
		}
		else if (liczbaKart() == 0 && !(k.getKolor().equals(Kolor.Joker))){
			karty.add(k);
			czyDodano = true;
		}
		return czyDodano;
	}
	
	public Karta usunKarte(int id){
		if (id > liczbaKart() || id < 0)
			return null;
		Karta karta = karty.get(id);
		karty.remove(id);
		return karta;
	}
	
	public Karta zobaczKarte(int id){
		if (id > liczbaKart() || id < 0)
			return null;
		Karta karta = karty.get(id);
		return karta;
	}
	
	public boolean czyJestJoker(){
		for(int i = 0; i < liczbaKart(); i++){
			if(karty.get(i).getKolor().equals(Kolor.Joker))
				return true;
		}
		return false;
	}
	
	
	public int punktyZaGrupe(){
		int punkty = (karty.get(0).getLiczbaPunktow()) * liczbaKart();
		return punkty;
	}
	
}
