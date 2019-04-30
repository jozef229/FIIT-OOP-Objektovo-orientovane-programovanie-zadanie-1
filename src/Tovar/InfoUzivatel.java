package Tovar;

import java.io.Serializable;

/**
 * 
 * @author jofy
 *
 */
public class InfoUzivatel implements Serializable {
	private static final long serialVersionUID = 0;

	private String meno, priezvisko, bydlisko;

	public InfoUzivatel(String meno, String priezvisko, String bydlisko) {
		this.meno = meno;
		this.priezvisko = priezvisko;
		this.bydlisko = bydlisko;

	}

	public String getMeno() {
		return meno;
	}

	public String getPriezvisko() {
		return priezvisko;
	}

	public String getBydlisko() {
		return bydlisko;
	}
}
