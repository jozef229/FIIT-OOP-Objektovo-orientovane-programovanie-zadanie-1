package Sklady;

import java.io.Serializable;

/**
 * 
 * @author jofy
 *
 */
public class Sklad implements Serializable, MenoSkladu {
	public static final long serialVersionUID = 0;
	private boolean cesta;
	private double km;
	private int pocetSkriniek;
	private int cakajuce = 0;
	private int obsadenost = 0;
	private int pocetPrichadzajucich = 0;
	private String meno;

	public Sklad(int pocetSkriniek, String meno, double km) {
		this.pocetSkriniek = pocetSkriniek;
		obsadenost = 0;
		this.meno = meno;
		this.km = km;
		cesta = false;
	}

	public double getKm() {
		return km;
	}

	public int KtoSom() {
		return 1;
	}

	public String getMenoSklad() {
		return meno;
	}

	public void cakajuceviac() {
		cakajuce++;
	}

	public void cakajucemenej() {
		cakajuce--;
	}

	public int cakajuce() {
		return cakajuce;
	}

	public int getPocetSkriniek() {
		return pocetSkriniek;
	}

	public int getPocetPrichadzajucich() {
		return pocetPrichadzajucich;
	}

	public void setPocetPrichadzajucich(int i) {
		this.pocetPrichadzajucich = pocetPrichadzajucich + i;
	}

	public void setPocetPrichadzajucichNull() {
		this.pocetPrichadzajucich = 0;
	}

	public int getVolneMiesto() {
		return (pocetSkriniek - obsadenost);
	}

	public void setIde() {
		cesta = true;
	}

	public void setIsiel() {
		cesta = false;
	}

	public boolean isCesta() {
		return cesta;
	}

	public int getObsadenost() {
		return obsadenost;
	}

	public void setObsadenost(int i) {
		obsadenost = obsadenost + i;
		if (obsadenost < 0)
			obsadenost = 0;
	}

}
