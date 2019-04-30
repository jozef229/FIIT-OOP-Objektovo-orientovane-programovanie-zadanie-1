package Preprava;

import java.io.Serializable;
import java.util.Observable;

import Sklady.Sklad;
import Sklady.SkladCas;
import Tovar.Balik;
import Tovar.BalikCas;

/**
 * Trieda Observera sluziaca
 * 
 * @author jofy
 */
public class Casovac extends Observable implements Sledovatel, Serializable {
	public static final long serialVersionUID = 0;
	private Balik balik;
	private Sklad sklad;

	public Casovac(Balik balik, Sklad sklad) {
		this.balik = balik;
		this.sklad = sklad;
	}

	/**
	 * Casovemu baliku znizuje pocet dni na sklade (zivotnost) a nastavuje
	 * Casovemu skladu odosielanie balikov naspat
	 */
	public void upovedom() {

		if (balik.getStav() == 3) {
			((BalikCas) balik).setCasUber(1);
			if (((BalikCas) balik).getCas() == 0) {
				balik.setStav(4);
				sklad.setIde();
				((SkladCas) sklad).setPocetOdchadzajucich(1);
				sklad.setObsadenost(-1);
			}
		}
	}

}