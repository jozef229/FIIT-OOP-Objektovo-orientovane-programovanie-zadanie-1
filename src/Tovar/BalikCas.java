package Tovar;

/**
 * 
 * @author jofy
 *
 */
public class BalikCas extends Balik {

	private static final long serialVersionUID = 0;
	private int cas;

	public BalikCas(String meno, String priezvisko, String bydlisko, double poistenie, String typ, String sklad) {
		super(meno, priezvisko, bydlisko, poistenie, typ, sklad);
		this.cas = 3;
	}

	public int getCas() {
		return cas;
	}

	public void setCasUber(int i) {
		this.cas = cas - i;
	}

	public void setCasNuluj() {
		this.cas = 0;
	}

	public boolean KtoSom() {
		return true;
	}

}
