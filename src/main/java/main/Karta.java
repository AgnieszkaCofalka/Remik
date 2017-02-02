package main;
public class Karta implements Comparable<Karta>{
	
	private Kolor kolor;			// Joker: kolor - Joker, numer - 15,16, nrWTalii- 53,54
	private int numer; // 2 - 16, nr karty w obrêbie danego koloru
	private int nrWTalii; // 1 - 54
	
	public static final int WALET = 11;
	public static final int DAMA = 12;
	public static final int KROL = 13;
	public static final int AS = 14;
	
	private final String[] nazwyKart = new String[]{
		"2", "3", "4", "5", "6", "7", "8", "9", "10", "Walet", "Dama", "Krol", "As", "Joker", "Joker"
	};
	
	
	public Karta(int nrWTalii){		// konstruktor na podstawie numeru w talii
		if (nrWTalii < 1 || nrWTalii > 54)
			throw new IllegalArgumentException();
		this.nrWTalii = nrWTalii;
		numer = toNumer(nrWTalii);
		kolor = toKolor(nrWTalii);
		
	}
	
	public Karta(Kolor k, int numer){		
		if (numer < 2 || numer > 16 || (numer > 14 && k != Kolor.Joker))
			throw new IllegalArgumentException();
		
		this.kolor = k;
		this.numer = numer;
		this.nrWTalii = toNrWTalii(k, numer);
	}
	
	public Karta(Karta karta){
		this.kolor = karta.kolor;
		this.numer = karta.numer;
		this.nrWTalii = karta.nrWTalii;
	}
	
	public void zmienKarte(int nrWTalii){
		if (nrWTalii < 1 || nrWTalii > 54)
			throw new IllegalArgumentException();
		this.nrWTalii = nrWTalii;
		numer = toNumer(nrWTalii);
		kolor = toKolor(nrWTalii);
	}
	
	public int getLiczbaPunktow(){
		if (nrWTalii > 52)
			return 150;
		else if (numer > 10)
			return 10;
		else
			return numer;
	}
	
	public int getNrWTalii(){
		return nrWTalii;
	}
	
	public int getNumer(){
		return numer;
	}
	
	public Kolor getKolor(){
		return kolor;
	}
	
	public int getKolorN(){
		return kolor.getN();
	}
	
	public String getNazwaKarty(){
		return nazwyKart[this.numer - 2];
	}
	
	public String getNazwaKoloru(){
		if (kolor == Kolor.Joker)
			return "";
		return kolor.toString();
	}
	
	public String toString(){
		String string =  getNazwaKarty() + " " + getNazwaKoloru();
		return string;
	}
	
	public String skrotNazwy(){		// skrócona nazwa karty (razem z kolorem)
		String s1;
		if (this.numer <= 10)
			s1 = getNazwaKarty();
		else
			s1 = getNazwaKarty().substring(0, 1);
		if(nrWTalii > 52)
			return s1;
		String s2 = getNazwaKoloru().substring(0, 1);
		String skrot = s1 + s2;
		return skrot;
	}
	
	public static int toNrWTalii(Kolor k, int n){
		if (n < 2 || n > 16 || (n > 14 && k != Kolor.Joker))		// 0, 1 - jokery
			throw new IllegalArgumentException();
		int nrWTalii;
		if (n > 14)		// dla jokerów
			nrWTalii = n + 38;
		else
			nrWTalii = (13 * (k.getN() - 1)) + n - 1;
		return nrWTalii;
	}
	
	public static Kolor toKolor(int nrWTalii){
		if (nrWTalii > 54 || nrWTalii < 1)
			throw new IllegalArgumentException();
		if (nrWTalii > 52)
			return Kolor.Joker;
		int k = nrWTalii / 13;
		if (nrWTalii % 13 != 0)
			k++;
		Kolor kolor;
		if (k == 1)
			kolor = Kolor.Trefl;
		else if (k == 2)
			kolor = Kolor.Karo;
		else if (k == 3)
			kolor = Kolor.Pik;	
		else
			kolor = Kolor.Kier;
		return
			kolor;
	}
	
	public static int toNumer(int nrWTalii){
		if (nrWTalii > 54 || nrWTalii < 1)
			throw new IllegalArgumentException();
		if (nrWTalii > 52)
			return nrWTalii - 38;
		int nr = (nrWTalii % 13) + 1;
		if (nr == 1)
			nr = 14;
		return nr;
	}
	
	public boolean equals(Object other){
		if (!(other instanceof Karta))
			return false;
		Karta otherK = (Karta) other;
		if (this.nrWTalii == otherK.nrWTalii)
			return true;
		else
			return false;
	}

	public int compareTo(Karta o) {
		return getNrWTalii() - o.getNrWTalii();
	}
	
}
