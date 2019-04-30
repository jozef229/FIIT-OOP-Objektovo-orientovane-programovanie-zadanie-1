package Sklady;

/**
 * 
 * @author jofy
 *
 */
public class SkladCas extends Sklad {
	private int pocetOdchadzajucich = 0;
	public static final long serialVersionUID = 0;

	public SkladCas(int pocetSkriniek, String meno, double km) {
		super(pocetSkriniek, meno, km);
		this.pocetOdchadzajucich = 0;

	}

	public int getPocetOdchadzajucich() {
		return pocetOdchadzajucich;
	}

	public void setPocetOdchadzajucich(int i) {
		pocetOdchadzajucich = pocetOdchadzajucich + i;
	}

	public int KtoSom() {
		return 2;
	}

	public void setPocetOdchadzajucichNull() {
		pocetOdchadzajucich = 0;
	}

	public String getMenoSklad() {
		return "Casovy sklad " + super.getMenoSklad();
	}
}
