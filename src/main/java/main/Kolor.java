package main;

// mo¿liwe kolory kart

public enum Kolor {
	Trefl(1),
	Karo(2),
	Pik(3),
	Kier(4),
	Joker(5);
	
	private final int N;		// liczbowe oznaczenie kolorów kart
	
	Kolor(int n){
		this.N = n;
	}
	
	public int getN(){
		return N;
	}
}
