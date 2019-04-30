package Sklady;

/**
 * Oproti Skladu ma nulovu vzdialenost tym padom su baliky na sklade okamzite
 * 
 * @author jofy
 *
 */
public class CentralnySklad extends Sklad {
	private static final long serialVersionUID = 0;

	public CentralnySklad(int pocetSkriniek, String meno) {
		super(pocetSkriniek, meno, 0);
	}

	public int KtoSom() {
		return 3;
	}

	public String getMenoSklad() {
		return "Centralny sklad " + super.getMenoSklad();
	}
}
