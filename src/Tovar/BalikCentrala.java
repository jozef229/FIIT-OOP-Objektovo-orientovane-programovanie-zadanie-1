package Tovar;

import Sklady.Sklad;

/**
 * 
 * @author jofy
 *
 */
public class BalikCentrala extends Balik {

	private static final long serialVersionUID = 0;

	public BalikCentrala(String meno, String priezvisko, String bydlisko, double poistenie, String typ, String sklad,
			Sklad skladd) {
		super(meno, priezvisko, bydlisko, poistenie, typ, sklad);
		stav = 3;
		skladd.setObsadenost(1);
	}

	public boolean KtoSom() {
		return false;
	}

}
