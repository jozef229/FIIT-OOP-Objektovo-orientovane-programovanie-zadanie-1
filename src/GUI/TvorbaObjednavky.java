package GUI;

import Preprava.ChybaZadania;
import Preprava.Transport;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Celkova tvorba objednavky a vyber hodiniek
 * 
 * @author jofy
 *
 */
public class TvorbaObjednavky extends Stage {
	public static final long serialVersionUID = 0;

	private Button spat = new Button("Spat");
	private Button vytvor = new Button("Vytvorit objednavku");
	private TextField meno = new TextField();
	private TextField priezvisko = new TextField();
	private TextField bydlisko = new TextField();
	private TextField poistenie = new TextField();
	private Label kodID = new Label("");
	private Label menoOzn = new Label("Krstne meno");
	private Label priezviskoOzn = new Label("Priezvisko");
	private Label bydliskoOzn = new Label("Bydlisko");
	private Label poistenieOzn = new Label("Vyska poistenia");
	private Label skladOzn = new Label("Vyber skladu");
	private Label hodinkyOzn = new Label("Vyber hodiniek");

	public TvorbaObjednavky(Stage TvorbaObjStage, Transport transport) {
		super();

		final ComboBox<String> boxSklad = new ComboBox<String>();
		final ComboBox<String> boxHodinky = new ComboBox<String>();
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(20));
		gridPane.setHgap(10);
		gridPane.setVgap(10);

		gridPane.add(spat, 0, 0, 1, 1);
		gridPane.add(menoOzn, 1, 0, 1, 1);
		gridPane.add(priezviskoOzn, 1, 1, 1, 1);
		gridPane.add(bydliskoOzn, 1, 2, 1, 1);
		gridPane.add(poistenieOzn, 1, 3, 1, 1);
		gridPane.add(skladOzn, 1, 4, 1, 1);
		gridPane.add(meno, 2, 0, 1, 1);
		gridPane.add(priezvisko, 2, 1, 1, 1);
		gridPane.add(bydlisko, 2, 2, 1, 1);
		gridPane.add(poistenie, 2, 3, 1, 1);
		gridPane.add(vytvor, 2, 6, 1, 1);
		gridPane.add(kodID, 2, 7, 1, 1);
		gridPane.add(boxSklad, 2, 4, 1, 1);
		gridPane.add(boxHodinky, 2, 5, 1, 1);
		gridPane.add(hodinkyOzn, 1, 5, 1, 1);

		for (int i = 0; i < transport.VratPocetSkladov(); i++) {
			boxSklad.getItems().add(transport.VratMenoSkladu(i));
		}

		boxHodinky.getItems().addAll("HUBLOT BLACK CAVIAR BANG (1200e)", "ROLEX SEA-DWELLER (3330)",
				"ULYSSE NARDIN ROYAL BLUE TOURBILLON (3200)", "ROLEX DATEJUST 41 (4000)",
				"LOUIS MOINET METEORIS (2900)", "CASIO OSESK (2200)", "LOUIS COSKEL (3300)");

		vytvor.setOnAction(e -> {
			try {
				transport.VytvorBalik(meno.getText(), priezvisko.getText(), bydlisko.getText(),
						Double.parseDouble(poistenie.getText()), boxHodinky.getValue(), boxSklad.getValue());

				kodID.setText("Cislo objednavky je:" + transport.IDbalik());
				meno.clear();
				priezvisko.clear();
				bydlisko.clear();
				poistenie.clear();
				boxHodinky.setValue(null);
				boxSklad.setValue(null);
			} catch (NumberFormatException | ChybaZadania | NullPointerException e2) {
				Alert chyba = new Alert(AlertType.ERROR);
				chyba.setTitle("Chyba");
				chyba.setContentText("Bud ste nieco nezadali alebo nevybrali");
				chyba.showAndWait();
			}
		});

		TvorbaObjStage.setOnCloseRequest(e -> {
			try {
				transport.uloz();
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
		});

		spat.setOnAction(e -> new ZakaznikOkno(TvorbaObjStage, transport));
		TvorbaObjStage.setScene(new Scene(gridPane, 600, 300));
	}
}
