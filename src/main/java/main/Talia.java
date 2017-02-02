package main;
import java.util.*;

public class Talia {
	private boolean[] talia = new boolean[54];
	//indeks w tablicy = nrWTalii - 1
	// true - karta znajduje siê w talii, false - karta jest w grze (nie ma jej w talii)
	
	private int liczbaKart; // liczba kart znajduj¹cych siê aktualnie w talii
	
	private Random random = new Random();
	
	public Talia(){
		initTalia();
	}
	
	public void initTalia(){
		for(int i = 0; i < 54; i++)
			talia[i] = true;
		liczbaKart = 54;
	}
	
	public Karta wezKarte(){
		if (liczbaKart == 0)
			return null;
		int los = random.nextInt(liczbaKart) + 1;
		int n = 0;
		for (int i = 0; i < 54; i++){
			if (!(talia[i]))
				continue;
			n++;
			if(n == los){
				Karta karta = new Karta(i + 1);
				talia[i] = false;
				liczbaKart--;
				return karta;
			}
		}
		return null;
	}
	
	public ArrayList<Karta> rozdajKarty(int n){	// daje graczowi n kart
		if (n > 54 || n < 0)
			throw new IllegalArgumentException();
		if(liczbaKart < n)
			return null;
		ArrayList<Karta> karty = new ArrayList<Karta>();
		for (int i =0; i < n; i++)
			karty.add(this.wezKarte());
		return karty;
	}
	
	public void dodajKarte(int nrWTalii){		// dodanie karty do talii 
		talia[nrWTalii - 1] = true;
		liczbaKart++;
	}
	
	public int getLiczbaKart(){
		return liczbaKart;
	}
	
	public void pustaTalia(){		// usuwa wszystkie karty z talii i pozostawia j¹ pust¹
		for (int i = 0; i < 54; i++)
			talia[i] = false;
		liczbaKart = 0;
	}
	
	public void kopiujTalie(Talia talia){	// talia przyjmuje wartoœci z innej talii
		for(int i = 0; i < 54; i++)
			this.talia[i] = talia.czyJestKarta(i + 1);
		this.liczbaKart = talia.getLiczbaKart();
	}
	
	public boolean czyJestKarta(int nrWTalii){
		return talia[nrWTalii - 1];
	}
	
	public String toString(){
		if (liczbaKart == 0)
			return "Brak kart w talii";
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < 54; i++){
			if(talia[i]){
				Karta karta = new Karta(i + 1);
				sb.append(karta.toString());
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	public String skrotyKart(){
		if (liczbaKart == 0)
			return "Brak kart w talii";
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < 54; i++){
			if(talia[i]){
				Karta karta = new Karta(i + 1);
				sb.append(karta.skrotNazwy());
				sb.append("\n");
			}
		}
		return sb.toString();
	}
}
