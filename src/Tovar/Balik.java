package Tovar;

import java.io.Serializable;

import Sklady.MenoSkladu;

/**
 * 
 * @author jofy
 *
 */
public class Balik implements Serializable, MenoSkladu {
	public static final long serialVersionUID = 0;
	private double poistenie;
	private String typ, sklad;
	private int id;
	protected int stav;
	InfoUzivatel uzivatel;

	public Balik(String meno, String priezvisko, String bydlisko, double poistenie, String typ, String sklad) {
		this.uzivatel = new InfoUzivatel(meno, priezvisko, bydlisko);
		this.poistenie = poistenie;
		this.typ = typ;
		this.sklad = sklad;
		this.id = this.hashCode();
		stav = 0;
	}

	public int getStav() {
		return stav;
	}

	public void setStav(int i) {
		this.stav = i;
	}

	public int getId() {
		return id;
	}

	public double getPoistenie() {
		return poistenie;
	}

	public InfoUzivatel getUzivatel() {
		return uzivatel;
	}

	public String getTyp() {
		return typ;
	}

	public String getMenoSklad() {
		return sklad;
	}

	public boolean KtoSom() {
		return false;
	}

}
