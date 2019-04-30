package Preprava;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import GUI.ManazerOkno;
import GUI.PrehladObjednavok;
import GUI.StavSkladov;
import Sklady.CentralnySklad;
import Sklady.Sklad;
import Sklady.SkladCas;
import Tovar.Balik;
import Tovar.BalikCas;
import Tovar.BalikCentrala;

/**
 * Hlavna trieda v ktorej sa nachadza vnutorna trieda Plan cesty a celkova
 * logika programu
 * 
 * @author jofy
 */

public class Transport extends Observable implements Serializable {
	public static final long serialVersionUID = 0;

	private List<Balik> balik = new LinkedList<>();
	private List<Sklad> sklad = new LinkedList<>();
	private List<Sledovatel> sledovatelia = new ArrayList<>();
	private Casovac casovac;
	private int StavNaplneCasu = 0, CoJeOtvorene = 0;
	private boolean PrepnutieVlakna = false;
	PlanCesty Vytvor = new PlanCesty();

	/**
	 * KontrolaStavu() najskor zisti ci bol spusteny vyjazd vodica vdaka
	 * getStavNalneCasui() ak nie tak skonci ale ak bol zmeni hodnotu
	 * PrepnutieVlakna na true nasledne vytvori nove vlakno ktore pokracuje v
	 * robote toho prveho (getter PrepnutieVlakna nam sluzi na vypinanie
	 * beziacich vlakien, novo vytvorene vlakno je tiez naviazane na tento
	 * getter (premennu))
	 * 
	 * @param manazer
	 *            objekt ManazerOkno
	 */
	public void KontrolaStavu(ManazerOkno manazer) {
		if (getStavNaplneCasui() != 0) {
			setPrepnutieVlakna(true);
			manazer.stav(((100 / CasKuriera()) * getStavNaplneCasui()) / 100);
			new Thread() {
				@Override

				/**
				 * run() tu prepisujeme povodnu metodu vlakna a vytvarame
				 * vynimku ak by bola chyba pri uspavani vlakna. Ak nie je chyba
				 * tak pracuje s prijatym objektom manazer a nastavuje mu stav
				 * ktorý sluzi ako hodnota pre prograsbar podla toho kolko casu
				 * preslo. V cykle for zistujeme ci uz nadisiel CasKuriera t.j.
				 * ci sa uz getStavNaplneCasui rovna vypocitanemu casu kolko by
				 * mal kurier prejst). Nasledne pri kazdom prejdeni cykle vo
				 * fore uspime vlakno a kontrolujeme ci sa vlakno nema vypnut
				 * (getPrepnutieVlakna). Ak skonci cyklus for a vlakno nebolo
				 * umelo ukoncene tak sa spusti vyjazd sofera a nastavi sa
				 * manazerov stav na 0. Nakoniec sa program ulozi.
				 */
				public void run() {
					for (int i = getStavNaplneCasui(); i <= CasKuriera(); i++) {
						try {
							setStavNaplneCasui(i);
							Thread.sleep(100);
							manazer.stav(((100 / CasKuriera()) * i) / 100);
							if (getPrepnutieVlakna() == true)
								break;
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					if (getPrepnutieVlakna() == false) {
						VyjazdSofera();
						manazer.stav(0);
						setStavNaplneCasui(0);
						try {
							uloz();
						} catch (ClassNotFoundException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (getPrepnutieVlakna() == true)
						setPrepnutieVlakna(false);
				}

			}.start();
		}
	}

	/**
	 * Vracia hodnotu kolko casu preslo od poslanie vodica na zavoz
	 * 
	 * @return StavNaplneCasu
	 */
	public int getStavNaplneCasui() {
		return StavNaplneCasu;
	}

	/**
	 * Nastavuje hodnotu kolko casu preslo od poslanie vodica na zavoz
	 * 
	 * @param i
	 *            hodnota setteru
	 */
	public void setStavNaplneCasui(int i) {
		StavNaplneCasu = i;
	}

	/**
	 * Nastavuje hodnotu na ktorej su zavysle vlakna (vypina a zapina ich)
	 * 
	 * @param i
	 *            hodnota urcujuca vypnutie aktivneho vlakna (ak je false
	 *            vypina)
	 */
	public void setPrepnutieVlakna(boolean i) {
		PrepnutieVlakna = i;
	}

	/**
	 * Vracia hodnotu na ktorej su zavysle vlakna (vypina a zapina ich)
	 * 
	 * @return Hodnotu ktora ukazuje ci sa maju dane vlakna vypnut
	 */
	public boolean getPrepnutieVlakna() {
		return PrepnutieVlakna;
	}

	/**
	 * NaplnenieCasu() najskor zisti ci existuju sklady ktore su potrebne
	 * navstivit ak nie tak skonci inak vytvori nove vlakno
	 * 
	 * @param manazer
	 *            vdaka nemu sa vytvara nove okno pri naplneni casu
	 */

	public void NaplnanieCasu(ManazerOkno manazer) {
		if (VratPocetTrueSkladov() > 0) {
			new Thread() {
				@Override
				/**
				 * run() tu prepisujeme povodnu metodu vlakna a vytvarame
				 * vynimku ak by bola chyba pri uspavani vlakna. Ak nie je chyba
				 * tak pracuje s prijatym objektom manazer a nastavuje mu stav
				 * ktorý sluzi ako hodnota pre prograsbar podla toho kolko casu
				 * preslo. V cykle for zistujeme ci uz nadisiel CasKuriera t.j.
				 * ci sa uz i rovna vypocitanemu casu kolko by mal kurier
				 * prejst). Nasledne pri kazdom prejdeni cykle vo fore uspime
				 * vlakno a kontrolujeme ci sa vlakno nema vypnut
				 * (getPrepnutieVlakna). Ak skonci cyklus for a vlakno nebolo
				 * umelo ukoncene tak sa spusti vyjazd sofera a nastavi sa
				 * manazerov stav na 0. Nakoniec sa program ulozi.
				 */
				public void run() {
					for (int i = 1; i <= CasKuriera(); i++) {
						try {
							setStavNaplneCasui(i);
							Thread.sleep(100);
							manazer.stav(((100 / CasKuriera()) * i) / 100);
							if (getPrepnutieVlakna() == true)
								break;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if (getPrepnutieVlakna() == false) {
						VyjazdSofera();
						manazer.stav(0);
						setStavNaplneCasui(0);
						try {
							uloz();
						} catch (ClassNotFoundException | IOException e) {
							e.printStackTrace();
						}
					}
					if (getPrepnutieVlakna() == true)
						setPrepnutieVlakna(false);
				}
			}.start();
		}
	}

	/**
	 * RefresPrehladObjednavky() najskor zistuje ci cas od vyjazdu vodica nieje
	 * 0 ak nieje tak pokracuje a vytvori vlakno inac ukonci metodu
	 * 
	 * @param prehlad
	 *            PrehladObjednavok pre lepsi pristup k nastavovaniu prvkov
	 * @param transport
	 *            Transport na moznost poslanie naspät
	 */
	public void RefresPrehladObjednavky(PrehladObjednavok prehlad, Transport transport) {
		if (getStavNaplneCasui() != 0) {
			new Thread() {
				@Override

				/**
				 * run() tu prepisujeme povodnu metodu vlakna a vytvarame
				 * vynimku ak by bola chyba pri uspavani vlakna. Ak nie je chyba
				 * tak spusti cyklus for. V cykle for zistujeme ci uz nadisiel
				 * CasKuriera t.j. ci sa uz i rovna vypocitanemu casu kuriera a
				 * ak ano tak refresne okno inak podla foru uspava na 100
				 * millisekund vlakno a kontroluje ci sa neotvorilo nove okno
				 * vdaka getCoJeOtvorene()
				 */
				public void run() {
					for (int i = getStavNaplneCasui(); i <= CasKuriera(); i++) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (getCoJeOtvorene() == 2 || getCoJeOtvorene() == 0) {
							break;
						}
					}
					if (getCoJeOtvorene() == 1) {
						prehlad.setRefres(transport);
					}
				}
			}.start();
		}
	}

	/**
	 * RefresStavSkladov() najskor zistuje ci cas od vyjazdu vodica nieje 0 ak
	 * nieje tak pokracuje a vytvori vlakno inac ukonci metodu
	 * 
	 * @param stav
	 *            StavSkladov pre lepsi pristup k nastavovaniu prvkov
	 * @param transport
	 *            Transport na moznost poslanie naspät
	 */
	public void RefresStavSkladov(StavSkladov stav, Transport transport) {
		if (getStavNaplneCasui() != 0) {
			new Thread() {
				@Override
				/**
				 * run() tu prepisujeme povodnu metodu vlakna a vytvarame
				 * vynimku ak by bola chyba pri uspavani vlakna. Ak nie je chyba
				 * tak spusti cyklus for. V cykle for zistujeme ci uz nadisiel
				 * CasKuriera t.j. ci sa uz i rovna vypocitanemu casu kuriera a
				 * ak ano tak refresne okno, inak podla foru uspava na 100
				 * millisekund vlakno a kontroluje ci sa neotvorilo nove okno
				 * vdaka getCoJeOtvorene()
				 */
				public void run() {
					for (int i = getStavNaplneCasui(); i <= CasKuriera(); i++) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (getCoJeOtvorene() == 1 || getCoJeOtvorene() == 0)
							break;
					}
					if (getCoJeOtvorene() == 2)
						stav.setRefres(transport);
				}
			}.start();
		}
	}

	/**
	 * Nastavuje hodnotu ktora je priradena jednotlivim oknam
	 * 
	 * @param i
	 *            hodnota ktora sa nastavi
	 */
	public void setCoJeOtvorene(int i) {
		CoJeOtvorene = i;
	}

	/**
	 * @return hodnota ktora je priradena jednotlivim okna
	 */
	public int getCoJeOtvorene() {
		return CoJeOtvorene;
	}

	/**
	 * Vypocitava cas kuriera kolko by sa malo cakat aby kurier navstivil vsetky
	 * sklady
	 * 
	 * @return hodnotu casu ktora sa vypocitana suctom casov vsetkych skladov
	 *         pricom ten cas je vzdialenost v km * 3
	 */
	public double CasKuriera() {
		double cas = 0;
		for (int i = 0; i < VratPocetSkladov(); i++) {
			if (sklad.get(i).isCesta() == true)
				cas = cas + sklad.get(i).getKm();
		}
		return cas * 3;
	}

	/**
	 * Pridanie Sledovatela potrebneho pre observer
	 * 
	 * @param sledovatelStavu
	 *            dany sledovatel ktoreho vytvarame
	 */
	public void pridajSledovatela(Sledovatel sledovatelStavu) {
		sledovatelia.add(sledovatelStavu);
	}

	/**
	 * @return Slovo ktore je vytvorene z retazcov ktore obsahuju vsetky baliky
	 *         (udaje su uschovane vo VypisBalik(i) pricom i je poradove cislo v
	 *         LinkedListe)
	 */
	public String VypisBalikALL() {
		String Slovo = "";
		for (int i = 0; i < VratPocetBalikov(); i++) {
			Slovo = Slovo + ((i + 1) + ":\n" + VypisBalik(i));
		}
		return Slovo;
	}

	/**
	 * @return Slovo ktore je vytvorene z retazcov ktore obsahuju vsetky sklady
	 *         (udaje su uschovane vo VypisSklad(i) pricom i je poradove cislo v
	 *         LinkedListe)
	 */
	public String VypisSkladovALL() {
		String Slovo = "";
		for (int i = 0; i < VratPocetSkladov(); i++) {
			Slovo = Slovo + (i + 1) + ":\n" + VypisSklad(i);
		}
		return Slovo;
	}

	/**
	 * Prevzatie() je vyuzivane na prevzatie (vymazanie) balika podla zadaneho
	 * cisla teda ak je ho mozne prevziat
	 * 
	 * @param cisloOBJ
	 *            cislo objednavky ktoru sme zadali v GUI (tu ktoru chceme
	 *            prevziat)
	 * @return Ak dane cislo neexistuje alebo nieje ho mozne prevziat vrati
	 *         String :Balik nieje na sklade alebo cislo danej objednavky
	 *         neexistuje, inac vrati String: Balik cislo : cisloOBJ bol
	 *         prebraty
	 */
	public String prevzatie(int cisloOBJ) {
		if (StavObjednavkyCislo(VyhladajObjednavku(cisloOBJ)) == 3) {
			VymazBalik(VyhladajObjednavku(cisloOBJ));
			return "Balik cislo : " + cisloOBJ + " bol prebraty";
		}
		return "Balik nieje na sklade alebo cislo danej objednavky neexistuje";
	}

	/**
	 * Sluzi na vyhladavanie balikov podla cisla zadaneho v GUI
	 * 
	 * @param cisloOBJ
	 *            cislo objednavky ktoru sme zadali v GUI (tu ktoru chceme
	 *            prevziat)
	 * @return Ak dane cislo neexistuje vrati String Zadené cislo neexistuje
	 *         alebo bolo uz prebrane. inac vrati VypisBalika vo je metoda
	 *         vracajuca String v ktorom su informacie o Baliku
	 */
	public String VyhladanieTlacidlo(int cisloOBJ) {
		String Slovo;
		if (VyhladajObjednavku(cisloOBJ) != -1) {
			Slovo = VypisBalik(VyhladajObjednavku(cisloOBJ));
		} else
			Slovo = "Zadené cislo neexistuje alebo bolo uz prebrane.";
		return Slovo;
	}

	/**
	 * Sluzi na vyhladavanie balikov podla cisla a danej hodnoty
	 * 
	 * @return Ak dane cislo neexistuje false ak existuje vrati false
	 * @param HladanyStav
	 *            definuje hodnotu ktoru hladame
	 * @param cislo
	 *            cislo objednavky
	 */
	public boolean VyhladanieTlacidlo(int HladanyStav, int cislo) {
		if (cislo != -1 && StavObjednavkyCislo(cislo) == HladanyStav) {
			return true;
		} else
			return false;
	}

	/**
	 * U vsetkych sledovateloch spusti metodu upovedom (funkcia observera)
	 */
	public void upovedomSledovatelov() {
		for (Sledovatel s : sledovatelia)
			s.upovedom();
	}

	/**
	 * @param i
	 *            hodnota ktora urcuje poradie skladu v LinkedListe
	 * @return meno skladu ktoreho chceme
	 */
	public String VratMenoSkladu(int i) {
		return sklad.get(i).getMenoSklad();
	}

	/**
	 * Vytvori sklady ktore vlozi do LinkedListu
	 */
	public void TvorbaSkladov() {
		if (VratPocetSkladov() == 0) {
			sklad.add(new Sklad(8, "Stare Grunty 55, 84545 Bratislava", 22));
			sklad.add(new Sklad(8, "Tomasikova 4, 80545 Bratislava", 32));
			sklad.add(new Sklad(5, "Mokrohajska 62, 80932 Bratislava", 12));
			sklad.add(new Sklad(10, "Mackyho 24, 80325 Bratislava", 25));
			sklad.add(new Sklad(4, "Jofyho 31, 80345 Bratislava", 44));
			sklad.add(new CentralnySklad(5, "Hlavna 23, 80300 Bratislava"));
			sklad.add(new SkladCas(5, "Horehronska 84, 80345 Bratislava", 34));

		}
	}

	/**
	 * zavola metodu upovedomSledovatelov() co je vlastne observer
	 */
	public void DalsiDen() {
		upovedomSledovatelov();
	}

	/**
	 * zavola metodu Vyjazd vnorenej triedy Vytvro
	 */
	public void VyjazdSofera() {
		Vytvor.Vyjazd();
	}

	/**
	 * Zistuje pocet skladov ktore je potrebne navstivit
	 * 
	 * @return pocet skladov ktore maju nastavene hodnotu True
	 */
	public int VratPocetTrueSkladov() {
		int i = 0;
		for (int x = 0; x < VratPocetSkladov(); x++) {
			if (sklad.get(x).isCesta() == true) {
				i++;
			}
		}
		return i;
	}

	/**
	 * Vnutorna trieda, dovod vytvorenia je odelenie logiky vypoctu a vyjazdu od
	 * zvysku
	 * 
	 * @author jofy
	 */
	class PlanCesty {

		/**
		 * Vytvory plan jazdy vodica ktory rozvaza tovar do skladov. Plan sa
		 * tvory pomocou vzdialenosti ktore ma kazdy sklad (je to vzdialenost od
		 * Centralneho skladu) a my predpokladame ze ku kazdemu skladu ide iba
		 * jedna cesta tam aj naspät takze dame vzdialenosti zoradime pomocou
		 * bublesortu a to zoradenie je vlastne dany plan Na zaciatku nastavime
		 * kazdemu skladu (podla toho ci je to Sklad, SkladCas, CentralnySklad -
		 * to zistujeme pomocou metody skladu getStav()) jednotlive atributy
		 * ktore potrebuje ako je obsadenost, pocet prichadzajucich .... podla
		 * moznosti skladu potom vyuzieme bublesort na zoradenie skladov ale uz
		 * iba tych ktorych sa to tyka teda ktore maju nastavene isCesta() na
		 * hodnote true t.j. ze ich treba navstivit (bud zobrat alebo doviest
		 * tovar). Nakoniec len vytvorime String ktory je zretazenim informacii
		 * o skladoch v poradi ake nam vytvoril bublesort
		 * 
		 * @return vrati String ktori je vytvoreny zretazenim skladov
		 *         (informacii danych skladov) podla zoradenia (ak nie je ziadna
		 *         polozka na prevoz to znamena ze vodic nema ziaden zavozny
		 *         plan vypise sa : Ziadne polozky na prevoz)
		 */
		public String Tvorba() {
			String slovo = " ";
			for (int i = 0; i < VratPocetBalikov(); i++) {
				for (int x = 0; x < VratPocetSkladov(); x++) {
					if (balik.get(i).getMenoSklad().equals(sklad.get(x).getMenoSklad()) && balik.get(i).getStav() == 2
							&& sklad.get(x).getVolneMiesto() > 0) {
						sklad.get(x).setPocetPrichadzajucich(1);
						sklad.get(x).cakajucemenej();
						sklad.get(x).setObsadenost(1);
						balik.get(i).setStav(1);
						sklad.get(x).setIde();
					} else if (balik.get(i).getMenoSklad().equals(sklad.get(x).getMenoSklad())
							&& balik.get(i).getStav() == 0 && sklad.get(x).getVolneMiesto() > 0) {
						sklad.get(x).setPocetPrichadzajucich(1);
						sklad.get(x).setObsadenost(1);
						balik.get(i).setStav(1);
						sklad.get(x).setIde();
					} else if (balik.get(i).getMenoSklad().equals(sklad.get(x).getMenoSklad())
							&& balik.get(i).getStav() == 0 && sklad.get(x).getVolneMiesto() == 0) {
						balik.get(i).setStav(2);
						sklad.get(x).cakajuceviac();
					}
				}
			}
			int j = 0;
			double swap = 0;
			double[] pole = new double[VratPocetSkladov()];
			for (int x = 0; x < VratPocetSkladov(); x++) {
				if (sklad.get(x).isCesta() == true) {
					pole[j] = sklad.get(x).getKm();
					j++;
					if (j < VratPocetSkladov())
						pole[j] = -1;
				}
			}
			for (int p = 0; p < (VratPocetTrueSkladov() - 1); p++) {
				for (int d = 0; d < VratPocetTrueSkladov() - p - 1; d++) {
					if (pole[d] > pole[d + 1]) {
						swap = pole[d];
						pole[d] = pole[d + 1];
						pole[d + 1] = swap;
					}
				}
			}
			for (int i = 0; i < VratPocetTrueSkladov(); i++) {
				for (int x = 0; x < VratPocetSkladov(); x++) {
					if (pole[i] == sklad.get(x).getKm()) {
						slovo = slovo.concat(Integer.toString(i + 1));
						slovo = slovo.concat(": ");
						slovo = slovo.concat(sklad.get(x).getMenoSklad());
						slovo = slovo.concat("\n ");
					}
				}
			}
			if (VratPocetTrueSkladov() == 0)
				slovo = "Ziadne polozky na prevoz";
			return slovo;
		}

		/**
		 * Ako prve je vytvoreny cyklus ktory prejde vsetky SkladyCas (tu sme
		 * vyuzili RTTI) a ak nejaky ma balik na vratenie (jeho getStav() sa
		 * rovna 4) tak ho vymaze z LinkedListu nasledne vsetky baliky ktore sa
		 * prevazali (getStav()==1) nastavy na hodntou 3 vynuluje pocty
		 * odchadzajucich v SkladCas (vsetkych z tejto triedy) nastavy pocet
		 * prichadzajucich u vsetkych skladov na 0 a nastavy sklady na hodnotu
		 * false t.j. sklady uz niesu potrebne navstivit
		 */
		public void Vyjazd() {
			for (int y = 0; y < VratPocetSkladov(); y++) {
				if (sklad.get(y) instanceof SkladCas) {
					for (int x = 0; x < ((SkladCas) sklad.get(y)).getPocetOdchadzajucich(); x++) {
						for (int i = 0; i < VratPocetBalikov(); i++) {
							if (balik.get(i).getStav() == 4) {
								VymazBalik(i);
								break;
							}
						}
					}
				}
			}

			for (int i = 0; i < VratPocetBalikov(); i++) {
				if (balik.get(i).getStav() == 1) {
					balik.get(i).setStav(3);
				}
			}
			for (int y = 0; y < VratPocetSkladov(); y++) {
				if (sklad.get(y).KtoSom() == 2) {
					((SkladCas) sklad.get(y)).setPocetOdchadzajucichNull();
				}
			}

			for (int x = 0; x < VratPocetSkladov(); x++) {
				sklad.get(x).setIsiel();
				sklad.get(x).setPocetPrichadzajucichNull();
			}
		}
	}

	/**
	 * Spusti medotu Tvorba z vnutornej triedy Vytvor ktora vracia string
	 * vytvoreneho planu
	 * 
	 * @return Vrati String Tvorba
	 */
	public String TvorbaPlanu() {
		return Vytvor.Tvorba();
	}

	/**
	 * @return Vrati pocet skladov (velkost LinkedListu)
	 */
	public int VratPocetSkladov() {
		return sklad.size();
	}

	/**
	 * Vytvara balik podla toho o aky balik sa jedna
	 * 
	 * @param meno
	 *            String meno uzivatela
	 * @param priezvisko
	 *            String priezvysko uzivatela
	 * @param bydlisko
	 *            String bydlisko uzivatela
	 * @param poistenie
	 *            double poistenie zasielky
	 * @param typ
	 *            String o ake hodinky sa jedna
	 * @param skladd
	 *            String meno skladu
	 * @throws ChybaZadania
	 *             Chyba ktora sa vytvori ak je nejake pole prazdne alebo zle
	 *             zadany typ ci poistenie je zaporne cislo
	 */
	public void VytvorBalik(String meno, String priezvisko, String bydlisko, double poistenie, String typ,
			String skladd) throws ChybaZadania {
		if (meno.equals("") || priezvisko.equals("") || bydlisko.equals("") || typ == null || poistenie < 0)
			throw new ChybaZadania();
		else {
			for (int x = 0; x < VratPocetSkladov(); x++) {
				if (skladd.equals(sklad.get(x).getMenoSklad()) && sklad.get(x).KtoSom() == 2) {
					balik.add(new BalikCas(meno, priezvisko, bydlisko, poistenie, typ, skladd));
					casovac = new Casovac(balik.get(balik.size() - 1), sklad.get(x));
					pridajSledovatela(casovac);
				} else if (skladd.equals(sklad.get(x).getMenoSklad()) && sklad.get(x).KtoSom() == 3) {
					balik.add(new BalikCentrala(meno, priezvisko, bydlisko, poistenie, typ, skladd, sklad.get(x)));
				} else if (skladd.equals(sklad.get(x).getMenoSklad())) {
					balik.add(new Balik(meno, priezvisko, bydlisko, poistenie, typ, skladd));
				}
			}
		}
	}

	/**
	 * Ako prve zisti stav v akom sa nachadza balik (ci je na sklade ci je v
	 * cakacej listine na odoslane...) a nasledne hodnoty ktore menil jeho stav
	 * napr pocet obsadenych balikov v sklade sa zmeni ak jeho stav je 3 t.j. ze
	 * sa nachadza v sklade a nakoniec sa ho pokusy vymazat pricom sa moze
	 * spustit chyba neplatneho indexu ktora je zabezpecena try{} catch{}
	 * 
	 * @param i
	 *            cislo poradia v LinkedListe
	 */
	public void VymazBalik(int i) {
		if (balik.get(i).getStav() == 3 || balik.get(i).getStav() == 4) {
			for (int x = 0; x < VratPocetSkladov(); x++) {
				if (balik.get(i).getMenoSklad().equals(sklad.get(x).getMenoSklad())) {
					if (balik.get(i).getStav() == 3)
						sklad.get(x).setObsadenost(-1);
				}
			}
		}
		if (balik.get(i).getStav() == 2) {
			for (int x = 0; x < VratPocetSkladov(); x++) {
				if (balik.get(i).getMenoSklad().equals(sklad.get(x).getMenoSklad())) {
					sklad.get(x).cakajucemenej();
				}
			}
		}
		if (balik.get(i).getStav() == 1) {
			for (int x = 0; x < VratPocetSkladov(); x++) {
				if (balik.get(i).getMenoSklad().equals(sklad.get(x).getMenoSklad())) {
					sklad.get(x).setPocetPrichadzajucich(-1);
				}
			}
		}
		try {
			balik.remove(i);
		} catch (IndexOutOfBoundsException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * @return Velkost LiknedListu balik t.j. pocet balikov
	 */
	public int VratPocetBalikov() {
		return balik.size();
	}

	/**
	 * @return Vrati ID balika
	 */
	public int IDbalik() {
		return balik.get(balik.size() - 1).getId();
	}

	/**
	 * 
	 * @param i
	 *            poradie balika v LinkedListe
	 * @return String ktory je zretazenim balikovych premennych (meno,
	 *         priezvysko....) vzhladom na typ baliku ak je to casovy tak prida
	 *         sa Pocet dni na vyzdvihnutie: ak este nejake su
	 */
	public String VypisBalik(int i) {
		String slovo = "ID Objednavky: " + balik.get(i).getId() + "\nSklad: " + balik.get(i).getMenoSklad()
				+ "\nMeno, Priezvisko: " + balik.get(i).getUzivatel().getMeno() + " "
				+ balik.get(i).getUzivatel().getPriezvisko() + "\nBydlisko: " + balik.get(i).getUzivatel().getBydlisko()
				+ "\nTyp hodiniek: " + balik.get(i).getTyp() + "\nCena poistenia "
				+ Double.toString(balik.get(i).getPoistenie()) + "\nStav objednavky: " + StavObjednavkySlovo(i) + "\n";
		if (balik.get(i).KtoSom() == true && balik.get(i).getStav() == 3) {
			slovo = slovo.concat("Pocet dni na vyzdvihnutie: " + ((BalikCas) balik.get(i)).getCas() + "\n");
		}
		if (balik.get(i).KtoSom() == true && balik.get(i).getStav() == 4) {
			for (int y = 0; y < VratPocetSkladov(); y++) {
				if (balik.get(i).getMenoSklad().equals(sklad.get(y).getMenoSklad()))
					System.out.print(((SkladCas) sklad.get(y)).getPocetOdchadzajucich());
			}
		}
		slovo = slovo.concat("--------------------------------------------------------------------\n");
		return slovo;
	}

	/**
	 * @param i
	 *            identifikuje poradie v LinkedListe
	 * @return Vrati getStav() t.j. stav objednavky daneho balika (podla i)
	 */
	public int StavObjednavkyCislo(int i) {
		return balik.get(i).getStav();
	}

	/**
	 * Vyhlada objednavku podla cisla obhednavky
	 * 
	 * @param x
	 *            hladane cislo objednavky (hashcode)
	 * @return vrati poradie objednavky v LinkedListe
	 */
	public int VyhladajObjednavku(int x) {
		for (int i = 0; i < VratPocetBalikov(); i++) {
			if (x == VypisId(i))
				return i;
		}
		return -1;
	}

	/**
	 * Stav Objednavky meni na text
	 * 
	 * @param i
	 *            poradove cislo balika v LinkedListe
	 * @return Vracia String ktory ktory znaci stav objednavky (ak ma stav
	 *         hodnotu 1 tak vrati tovar caka na odoslanie a.t.d)
	 */
	public String StavObjednavkySlovo(int i) {
		String Slovo = null;
		if (balik.get(i).getStav() == 0)
			Slovo = "objednavka este nieje spracovana ";
		if (balik.get(i).getStav() == 1)
			Slovo = "tovar caka na odoslanie";
		if (balik.get(i).getStav() == 2)
			Slovo = "sklady su plne hned ako bude mozne tovar odosleme";
		if (balik.get(i).getStav() == 3)
			Slovo = "tovar je na sklade (caka na prebratie)";
		if (balik.get(i).getStav() == 4)
			Slovo = "tovar caka na vratenie";
		return Slovo;
	}

	/**
	 * Sluzi na ziskanie textu (informacii) o sklade
	 * 
	 * @param i
	 *            je poradie skladu v LinkedListe
	 * @return String ktory je zretazenim informacii o sklade. Su 3 druhy
	 *         skladov prve sa zisti o ktory sa to jedna a podla toho o ktory
	 *         ide sa vypisu parametre ktore obsahuje ak je to napr Centralny
	 *         slad takze ma hodnotu ktoSom() rovnu 3 tak vypise okrem
	 *         standardnych informacii aj: Pocet boxov: Nie je obmedzeny Doba
	 *         dodania: Hned nakolko tuto moznost ostatne sklady nepodporuju.
	 */
	public String VypisSklad(int i) {
		String Slovo = "Sidlo skladu:" + sklad.get(i).getMenoSklad() + "\nPocet obsadenych : "
				+ (sklad.get(i).getObsadenost() - sklad.get(i).getPocetPrichadzajucich());
		if (sklad.get(i).KtoSom() == 1 || sklad.get(i).KtoSom() == 2) {
			Slovo = Slovo.concat("\nPocet boxov: " + sklad.get(i).getPocetSkriniek() + "\nPocet volnych: "
					+ sklad.get(i).getVolneMiesto() + "\nPocet prichadzajucich :"
					+ sklad.get(i).getPocetPrichadzajucich() + "\nPocet cakajucich na uvolnenie miesta: "
					+ sklad.get(i).cakajuce());
		}
		if (sklad.get(i).KtoSom() == 2) {
			Slovo = Slovo.concat(
					"\nPocet odchadzajucich balikov na sklad: " + ((SkladCas) sklad.get(i)).getPocetOdchadzajucich());
		}
		if (sklad.get(i).KtoSom() == 3) {
			Slovo = Slovo.concat("\nPocet boxov: Nie je obmedzeny \nDoba dodania: Hned");
		}
		Slovo = Slovo.concat("\n--------------------------------------------------------------------\n");
		return Slovo;
	}

	/**
	 * @param i
	 *            poradove cislo balika v LinkedListe
	 * @return int s ID balika
	 */
	public int VypisId(int i) {
		return balik.get(i).getId();
	}

	/**
	 * Sluzi na ulozenie udajov teda Serializaciu a uklada informacie o
	 * LinkedListoch balik, sklad, a sledovatelia (uklada do dokumentu
	 * hodinkomat.out) dane chyby sluzia na informaciu ci sa podarilo ulozenie
	 * ak nie tak vypise chybovu hlasku do Consoly
	 * 
	 * @throws ClassNotFoundException
	 *             Chyba pri serializacii
	 * @throws IOException
	 *             Chyba pri serializacii
	 */
	public void uloz() throws ClassNotFoundException, IOException {
		try {
			FileOutputStream fvystup = new FileOutputStream("hodinkomat.out");
			ObjectOutputStream obvystup = new ObjectOutputStream(fvystup);
			obvystup.writeObject(this.balik);
			obvystup.writeObject(this.sklad);
			obvystup.writeObject(this.sledovatelia);
			obvystup.close();
			fvystup.close();
		} catch (Exception e) {
			System.out.println("Chyba pri serializacii: " + e);
		}
	}

	@SuppressWarnings("unchecked")
	/**
	 * Sluzi na nacitanie udajov teda Serializaciu - nacitava informacie o
	 * LinkedListoch balik, sklad, a sledovatelia (vytvara dokument
	 * hodinkomat.out ak nie je uz vytvoreny) dane chyby sluzia na informaciu ci
	 * sa podarilo nacitanie ak nie tak vypise chybovu hlasku do Consoly
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void nacitaj() throws ClassNotFoundException, IOException {
		try {
			FileInputStream fvstup = new FileInputStream("hodinkomat.out");
			ObjectInputStream obvstup = new ObjectInputStream(fvstup);
			this.balik = (List<Balik>) obvstup.readObject();
			this.sklad = (List<Sklad>) obvstup.readObject();
			this.sledovatelia = (ArrayList<Sledovatel>) obvstup.readObject();
			obvstup.close();
			fvstup.close();
		} catch (Exception e) {
			System.out.println("Chyba pri serializacii: " + e);
		}
	}

}
