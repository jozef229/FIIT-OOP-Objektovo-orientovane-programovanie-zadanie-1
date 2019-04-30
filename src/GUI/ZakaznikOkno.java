package GUI;

import java.io.IOException;

import Preprava.Transport;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Menu pre zakaznika
 * 
 * @author jofy
 *
 */
public class ZakaznikOkno extends Stage {
	public static final long serialVersionUID = 0;

	private Button spat = new Button("Spat");
	private Button nova_objednavka = new Button("Nova objednavka");
	private Button info_objednavka = new Button("Stav objednavky");

	public ZakaznikOkno(Stage ZakaznikStage, Transport transport) {

		super();
		GridPane gridPane = new GridPane();

		gridPane.setPadding(new Insets(20));
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.add(spat, 0, 0, 1, 1);
		gridPane.add(nova_objednavka, 1, 0, 1, 1);
		gridPane.add(info_objednavka, 2, 0, 1, 1);

		ZakaznikStage.setOnCloseRequest(e -> {
			try {
				transport.uloz();
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
		});

		spat.setOnAction(e -> new VyberUzivatela(ZakaznikStage, transport));
		nova_objednavka.setOnAction(e -> new TvorbaObjednavky(ZakaznikStage, transport));
		info_objednavka.setOnAction(e -> new InfoObjednavka(ZakaznikStage, transport));

		ZakaznikStage.setScene(new Scene(gridPane, 400, 80));
	}
}
