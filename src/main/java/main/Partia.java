package main;

import java.util.ArrayList;

public class Partia {
	
	private int[] wynikiGry;
	private Gracz gracz;
	private AutomatycznyGracz automatycznyGracz;
	private Talia talia;
	private Talia stos;
	private KartyNaStole kartyNaStole;
	private Karta wyrzucona;
	private ArrayList<Sekwens> wykladaneSekwensy;
	private ArrayList<GrupaKart> wykladaneGrupy;
	int punktyWylozenia;
	
	public Partia(){			// co z rozpoczynaj鉍ym?

		wynikiGry = new int[2];
		wynikiGry[0] = 0;
		wynikiGry[1] = 0;
		talia = new Talia();
		stos = new Talia();
		kartyNaStole = new KartyNaStole();
		wykladaneSekwensy = new ArrayList<Sekwens>();
		wykladaneGrupy = new ArrayList<GrupaKart>();
		punktyWylozenia = 0;
		wyrzucona = null;
	}
	

	
	public int[] getWynikiGry() {
		return wynikiGry;
	}

	public Gracz getGracz(){
		return gracz;
	}
	
	public Gracz getAutomatycznyGracz(){
		return automatycznyGracz;
	}

	public KartyNaStole getKartyNaStole() {
		return kartyNaStole;
	}

	public void setWynikiGry(int[] wynikiGry) {
		this.wynikiGry = wynikiGry;
	}
	

	public Karta getWyrzucona() {
		return wyrzucona;
	}
	
	
	public int getPunktyWylozenia() {
		return punktyWylozenia;
	}

	public boolean czyMoznaDokladac(){
		return (gracz.liczbaKart() < 12 && gracz.liczbaKart() > 1);
	}
	
	public boolean czyMoznaDokladacDoGrupyNr(int nrGrupy){
		return kartyNaStole.liczbaKartWGrupieNr(nrGrupy) < 4;
	}
	
	public int liczbaKartGracza(){
		return gracz.liczbaKart();
	}
	
	public boolean czyJestJokerWSekwensieNr(int nrSekwensu){	// dotyczy kart na stole
		return kartyNaStole.czyJestJokerWSekwensie(nrSekwensu);
	}
	
	public boolean czyJestJokerWGrupieNr(int nrGrupy){	// dotyczy kart na stole
		return kartyNaStole.czyJestJokerWGrupie(nrGrupy);
	}

	public void rozpocznijPartie(){
		talia.initTalia();
		stos.pustaTalia();
		kartyNaStole.wyczyscStol();
		wykladaneSekwensy.clear();
		wykladaneGrupy.clear();
		wyrzucona = null;
		gracz = new Gracz(talia.rozdajKarty(13));
		automatycznyGracz = new AutomatycznyGracz(talia.rozdajKarty(12));
		automatycznyGracz.setPartia(this);
	}
	
	private void sprawdzTalie() {
		if (talia.getLiczbaKart() == 0){
			talia.kopiujTalie(stos);
			stos.pustaTalia();
		}
	}
	
	public void wezWyrzucona(){
		if (wyrzucona != null){
			gracz.dodajKarte(wyrzucona);
			gracz.sortujKarty();
		}
	}
	
	public boolean sprobujDolozycDoSekwensuNr(int nrSekwensu, int idKarty){
		boolean czyMozna =  kartyNaStole.dolozKarteDoSekwensuNr(gracz.zobaczKarteID(idKarty), nrSekwensu);
		if (czyMozna)
			gracz.zagrajKartaId(idKarty);
		return czyMozna;
	}
	
	public boolean sprobujDolozycDoGrupyNr(int nrGrupy, int idKarty){
		boolean czyMozna =  kartyNaStole.dolozKarteDoGrupyNr(gracz.zobaczKarteID(idKarty), nrGrupy);
		if (czyMozna)
			gracz.zagrajKartaId(idKarty);
		return czyMozna;
	}
	
	public Karta sprobujWziacJokeraZSekwensuNr(int nrSekwensu, int idKarty){
		Karta joker =  kartyNaStole.wezJokeraZSekwensuNr(gracz.zobaczKarteID(idKarty), nrSekwensu);
		if (joker != null){
			gracz.zagrajKartaId(idKarty);
			gracz.dodajKarte(joker);
		}
		return joker;
	}
	
	public Karta sprobujWziacJokeraZGrupyNr(int nrGrupy, int idKarty){
		Karta joker =  kartyNaStole.wezJokeraZGrupyNr(gracz.zobaczKarteID(idKarty), nrGrupy);
		if (joker != null){
			gracz.zagrajKartaId(idKarty);
			gracz.dodajKarte(joker);
		}
		return joker;
	}
	
	public Karta wezZTalii(){
		Karta nowaKarta = talia.wezKarte();
		gracz.dodajKarte(nowaKarta);
		dodajWyrzuconaNaStos();
		return nowaKarta;
	}
	
	public void poczatekWykladania(){
		punktyWylozenia = 0;
		wykladaneSekwensy.clear();
		wykladaneGrupy.clear();
	}
	
	public void oddajKartyGraczowi(){
		for (int i = 0; i < wykladaneSekwensy.size(); i++){
			int liczbaKart = wykladaneSekwensy.get(i).liczbaKart();
			for(int j = 0; j < liczbaKart; j++)
				gracz.dodajKarte(wykladaneSekwensy.get(i).usunKarte(0));
		}
		for (int i = 0; i < wykladaneGrupy.size(); i++){
			int liczbaKart = wykladaneGrupy.get(i).liczbaKart();
			for(int j = 0; j < liczbaKart; j++)
				gracz.dodajKarte(wykladaneGrupy.get(i).usunKarte(0));
		}
		punktyWylozenia = 0;
		wykladaneSekwensy.clear();
		wykladaneGrupy.clear();
		gracz.sortujKarty();
	}
	
	public String sprawdzCzyPoprawnieWylozono(){
		String komunikat = null;
		String k1 = "Nie masz karty do wyrzucenia, otrzymujesz\nz powrotem wy這穎ne karty";
		String k2 = "Nie wy這穎no 瘸dnych kart";
		String k3 = "Suma kart nie przekroczy豉 51 lub nie wy這穎no sekwensu,\notrzymujesz z powrotem wy這穎ne karty";
		if (gracz.liczbaKart() == 0)
			komunikat = k1;
		else if (wykladaneSekwensy.size() == 0 && wykladaneGrupy.size() == 0)
			komunikat = k2;
		else if ((gracz.liczbaKart()+liczbaWykladanychKart()) > 12 && (policzPunktyZaKarty() < 51 || wykladaneSekwensy.size() == 0))
			komunikat = k3;
		else
			dodajWylozoneKartyNaStol();
		if (komunikat != null)
			oddajKartyGraczowi();
		return komunikat;
	}
	
	private int policzPunktyZaKarty(){
		int punkty = 0;
		for (int i = 0; i < wykladaneSekwensy.size(); i++)
			punkty += wykladaneSekwensy.get(i).punktyZaSekwens();
		for (int i = 0; i < wykladaneGrupy.size(); i++)
			punkty += wykladaneGrupy.get(i).punktyZaGrupe();
		return punkty;
	}

	
	private int liczbaWykladanychKart(){
		int liczbaWykladanych = 0;
		for(int i = 0; i < wykladaneSekwensy.size(); i++)
			liczbaWykladanych += wykladaneSekwensy.get(i).liczbaKart();
		for(int i = 0; i < wykladaneGrupy.size(); i++)
			liczbaWykladanych += wykladaneGrupy.get(i).liczbaKart();
		return liczbaWykladanych;
	}
	
	private void dodajWylozoneKartyNaStol() {
		for(int i = 0; i < wykladaneSekwensy.size(); i++)
			kartyNaStole.dodajSekwens(wykladaneSekwensy.get(i));
		for(int i = 0; i < wykladaneGrupy.size(); i++)
			kartyNaStole.dodajGrupeKart(wykladaneGrupy.get(i));
		wykladaneSekwensy.clear();
		wykladaneGrupy.clear();
		punktyWylozenia = 0;
	}
	
	public boolean maWyrzucona() {
		boolean czyMaWyrzucona = false;
		if(wyrzucona != null)
			czyMaWyrzucona = gracz.czyMaKarteNrWTalii(wyrzucona.getNrWTalii());
		return czyMaWyrzucona;
	}
	
	public void oddajWyrzucona(){
		gracz.zagrajKarta(wyrzucona.getNrWTalii());
	}
	
	public void nowySekwens(){
		Sekwens sekwens = new Sekwens();
		wykladaneSekwensy.add(sekwens);
	}
	
	public void zatwierdzSekwens(){
		punktyWylozenia += wykladaneSekwensy.get(wykladaneSekwensy.size() - 1).punktyZaSekwens();
	}
	
	public void oddajGraczowiSekwens(){
		if(wykladaneSekwensy.size() > 0){
			Sekwens s = wykladaneSekwensy.get(wykladaneSekwensy.size() - 1);
			int liczbaKart = s.liczbaKart();
			for(int i = 0; i < liczbaKart; i++){
				if(s.zobaczKarte(0) != null)
					gracz.dodajKarte(s.usunKarte(0));
				}
			wykladaneSekwensy.remove(wykladaneSekwensy.size() - 1);
		}
	}
	
	
	public void nowaGrupa(){
		GrupaKart grupa = new GrupaKart();
		wykladaneGrupy.add(grupa);
	}
	
	public void zatwierdzGrupe(){
		punktyWylozenia += wykladaneGrupy.get(wykladaneGrupy.size() - 1).punktyZaGrupe();
	}
	
	public void oddajGraczowiGrupe(){
		if(wykladaneGrupy.size() > 0){
			GrupaKart g = wykladaneGrupy.get(wykladaneGrupy.size() - 1);
			int liczbaKart = g.liczbaKart();
			for(int i = 0; i < liczbaKart; i++){
				if(g.zobaczKarte(0) != null)
					gracz.dodajKarte(g.usunKarte(0));
				}
			wykladaneGrupy.remove(wykladaneGrupy.size() - 1);
		}
	}
	
	public boolean czyMoznaZatwierdzicGrupe(){
		return wykladaneGrupy.get(wykladaneGrupy.size() - 1).liczbaKart() >= 3;
	}
	
	public boolean czyMoznaZatwierdzicSekwens(){
		return wykladaneSekwensy.get(wykladaneSekwensy.size() - 1).liczbaKart() >= 3;
	}
	
	public boolean czyMoznaDodacDoWykladanejGrupy(int IDKarty){
		GrupaKart grupa = wykladaneGrupy.get(wykladaneGrupy.size() - 1);
		boolean czyMoznaDodac = grupa.dodajKarte(gracz.zobaczKarteID(IDKarty));
		if(czyMoznaDodac){
			gracz.zagrajKartaId(IDKarty);
		}
		return czyMoznaDodac;
	}
	
	public boolean czyMoznaDodacDoWykladanegoSekwensu(int IDKarty){		// dotyczy aktualnie wykladanego sekwensu
		Sekwens sekwens = wykladaneSekwensy.get(wykladaneSekwensy.size() - 1);
		boolean czyMoznaDodac = sekwens.dodajKarte(gracz.zobaczKarteID(IDKarty));
		if(czyMoznaDodac){
			gracz.zagrajKartaId(IDKarty);
		}
		return czyMoznaDodac;
	}
		
	public int koniecKolejki(int IDKarty){
		int wygrany = -1;
		sprawdzTalie();
		wyrzucona = gracz.zagrajKartaId(IDKarty);
		stos.dodajKarte(wyrzucona.getNrWTalii());
		if(gracz.liczbaKart() == 0)
			wygrany = 0;
		wyrzucona = automatycznyGracz.wykonajRuch();
		if(automatycznyGracz.liczbaKart() == 0)
			wygrany = 1;
		if(wygrany >= 0)
			zliczPunkty();
		sprawdzTalie();
		return wygrany;
	}
	
	
	private void zliczPunkty(){
		wynikiGry[0] += gracz.podliczPunkty();
		wynikiGry[1] += automatycznyGracz.podliczPunkty();
	}
	
	public boolean czyJokerNaMiejscuOId(int IDKarty){
		return (gracz.zobaczKarteID(IDKarty).getKolor().equals(Kolor.Joker));
	}
	
	public void sortujKartyGracza(){
		gracz.sortujKarty();
	}
	
	public Karta kartaDlaAutomatycznegoGracza(){
		return talia.wezKarte();
	}
	
	public boolean dolozJokeraNaKoncuSekwensuNr(int nrSekwensu, int IDKarty){
		Karta k = gracz.zobaczKarteID(IDKarty);
		boolean czyDolozono = kartyNaStole.dolozJokeraNaKoncuSekwensu(nrSekwensu, k);
		if(czyDolozono)
			gracz.zagrajKartaId(IDKarty);
		return czyDolozono;
	}
	public boolean dolozJokeraNaPoczatkuSekwensuNr(int nrSekwensu, int IDKarty){
		Karta k = gracz.zobaczKarteID(IDKarty);
		boolean czyDolozono = kartyNaStole.dolozJokeraNaPoczatkuSekwensu(nrSekwensu, k);
		if(czyDolozono)
			gracz.zagrajKartaId(IDKarty);
		return czyDolozono;
	}

	public void dodajWyrzuconaNaStos() {
		stos.dodajKarte(wyrzucona.getNrWTalii());
		wyrzucona = null;
	}

}
