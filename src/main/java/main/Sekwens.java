package main;
import java.util.ArrayList;

public class Sekwens{
	/*
	 - konstruktor
	 - dodawanie karty do sekwensu
	 - ?obs³uga jokerów? -> funkcja 'wymien na jokera'
	 - dok³adanie kart: przycisk obok grupy kart / sekwensu, karty bêd¹ tylko obrazami
	 - jeœli do³o¿ono jokera, to aktywuje siê przycisk 'wez jokera'
	 */
	private ArrayList<Karta> karty;
	
	public Sekwens(){
		karty = new ArrayList<Karta>();
	}
	
	public int liczbaKart(){	
		return karty.size();
	}
	
	public Karta pierwszaKarta(){	// dla jokera zwraca tê kartê, któr¹ on zastêpuje
		int nrWTalii = 54;
		for (int i = 0; nrWTalii > 52 && i < liczbaKart(); i++)
			nrWTalii = karty.get(i).getNrWTalii() - i;
		Karta pierwsza = new Karta(nrWTalii);
		return pierwsza;
	}
	
	public Karta ostatniaKarta(){	// dla jokera zwraca tê kartê, któr¹ on zastêpuje
		int nrWTalii = 54;
		for (int i = liczbaKart() - 1; nrWTalii > 52 && i >= 0; i--)
			nrWTalii = karty.get(i).getNrWTalii() + (liczbaKart() - i - 1);
		Karta ostatnia = new Karta(nrWTalii);
		return ostatnia;
	}
	
	public boolean dodajKarte(int nrWTalii){	// true - uda³o siê do³o¿yæ, false - nie uda³o siê
		boolean czyDodano = false;
		Karta k = new Karta(nrWTalii);
		if(liczbaKart() !=0){
			Karta p = pierwszaKarta();
			Karta o = ostatniaKarta();
			if(k.getKolorN() == p.getKolorN()){
				if ((k.getNumer() == p.getNumer() - 1) || (k.getKolor().equals(Kolor.Joker) && !(karty.get(0).getKolor().equals(Kolor.Joker)))){
					czyDodano = true;
					karty.add(0, k);
				}
				else if((k.getNumer() == o.getNumer() + 1) || (k.getKolor().equals(Kolor.Joker) && !(karty.get(liczbaKart() - 1).getKolor().equals(Kolor.Joker)))){
					czyDodano = true;
					karty.add(k);
				}
			}
		}
		else{
			karty.add(k);
			czyDodano = true;
		}
		return czyDodano;
	}
	// dodajKarte - u¿ywane przy tworzeniu sekwensu
	public boolean dodajKarte(Karta k){	// true - uda³o siê do³o¿yæ, false - nie uda³o siê
		boolean czyDodano = false;
		if(liczbaKart() !=0 && !(liczbaKart() == 1 && karty.get(0).getKolor().equals(Kolor.Joker))){
			Karta p = pierwszaKarta();
			Karta o = ostatniaKarta();
			
			if(k.getKolorN() == p.getKolorN() || k.getKolor().equals(Kolor.Joker)){
				if((k.getNumer() == o.getNumer() + 1) && !(k.getKolor().equals(Kolor.Joker))|| (k.getKolor().equals(Kolor.Joker) && !(karty.get(liczbaKart() - 1).getKolor().equals(Kolor.Joker)) && o.getNumer() != 14)){
					czyDodano = true;
					karty.add(k);
				}
			}
		}
		else if (liczbaKart() == 1 && karty.get(0).getKolor().equals(Kolor.Joker)&& !(k.getKolor().equals(Kolor.Joker))){
			if(k.getNumer() != 14){
				czyDodano = true;
				karty.add(k);
			}
		}
		else{
			karty.add(k);
			czyDodano = true;
		}
		return czyDodano;
	}
	
	public boolean dodajKarteDoWylozonegoSekwensu(Karta k){		// 
		if (this.liczbaKart() < 3)
			return false;			// nie powinno wyst¹piæ
		else{
			Karta p = pierwszaKarta();		
			Karta o = ostatniaKarta();
			if (k.getKolorN() != p.getKolorN() && !(k.getKolor().equals(Kolor.Joker))){
				return false;
			}
			else if (k.getNumer() == p.getNumer() - 1){
				karty.add(0, k);
				return true;
			}
			else if (!(k.getKolor().equals(Kolor.Joker)) && k.getNumer() == o.getNumer() + 1){
				karty.add(k);
				return true;
			}
			else if (k.getKolor().equals(Kolor.Joker) && !(karty.get(liczbaKart() - 1).getKolor().equals(Kolor.Joker)) && o.getNumer() != 14){
				karty.add(k);
				return true;
			}
			else if (k.getKolor().equals(Kolor.Joker) && !(karty.get(0).getKolor().equals(Kolor.Joker)) && p.getNumer() != 2){
				karty.add(0, k);
				return true;
			}
			
		}
		System.out.println("a");
		return false;	
	}
	

	public Karta wezJokera(Karta kartaDoZamiany){
		if (liczbaKart() < 3)
			return null;
		Karta joker = null;
		for(int i = 0; i < karty.size() - 1; i++){
			if(karty.get(i).getKolor().equals(Kolor.Joker) && (karty.get(i + 1).getNrWTalii() == kartaDoZamiany.getNrWTalii() + 1)){
				joker = karty.get(i);
				karty.remove(i);
				karty.add(i, kartaDoZamiany);
				return joker;
			}
		}
		if (karty.get(karty.size() - 1).getKolor().equals(Kolor.Joker)&& karty.get(liczbaKart() - 2).getNrWTalii() == kartaDoZamiany.getNrWTalii() - 1){
			joker = karty.get(liczbaKart() - 1);
			karty.remove(liczbaKart() - 1);
			karty.add(kartaDoZamiany);
		}	
		return joker;
	}
	
	public Karta usunKarte(int id){
		if (id >= liczbaKart() || id < 0)
			return null;
		Karta karta = karty.get(id);
		karty.remove(id);
		return karta;
	}
	
	public Karta zobaczKarte(int id){
		if (id >= liczbaKart() || id < 0)
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
	
	public boolean dolozJokeraNaKoncu(Karta k){
		if (ostatniaKarta().getNumer() != 14 && !(karty.get(liczbaKart() - 1).getKolor().equals(Kolor.Joker))){
			karty.add(k);
			return true;
		}
		else
			return false;
	}
	
	public boolean dolozJokeraNaPoczatku(Karta k){
		if (pierwszaKarta().getNumer() != 2 && !(karty.get(0).getKolor().equals(Kolor.Joker))){
			karty.add(0, k);
			return true;
		}
		else
			return false;
	}
	
	
	public int punktyZaSekwens(){
		if (liczbaKart() == 0)
			return 0;
		int punkty = 0;
		for (int i = 0; i < liczbaKart() - 1; i++){
			if(karty.get(i).getKolor().equals(Kolor.Joker)){
				if (karty.get(i + 1).getNumer() > 10)
					punkty += karty.get(i + 1).getLiczbaPunktow();
				else
					punkty += (karty.get(i + 1).getLiczbaPunktow()) - 1;
			}
			else
				punkty += karty.get(i).getLiczbaPunktow();
		}
		if(karty.get(liczbaKart() - 1).getKolor().equals(Kolor.Joker) && liczbaKart() > 1){
			if (karty.get(liczbaKart() - 2).getNumer() >= 10)
				punkty += karty.get(liczbaKart() - 2).getLiczbaPunktow();
			else
				punkty += (karty.get(liczbaKart() - 2).getLiczbaPunktow()) + 1;
		}
		else if (!(karty.get(liczbaKart() - 1).getKolor().equals(Kolor.Joker)))
			punkty += karty.get(liczbaKart() - 1).getLiczbaPunktow();
		return punkty;
	}
	
}
