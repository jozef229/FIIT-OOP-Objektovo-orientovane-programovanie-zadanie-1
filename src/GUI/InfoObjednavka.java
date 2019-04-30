package GUI;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;

import Preprava.Transport;

/**
 * Celkove informacie o objednavke a moznost prebrat objednavku
 * 
 * @author jofy
 *
 */
public class InfoObjednavka extends Stage {
	public static final long serialVersionUID = 0;

	private Button spat = new Button("Spat");
	private Button hladaj = new Button("Vyhladat");
	private Button prevziat = new Button("Prevziat objednavku");
	private TextField cisloOBJ = new TextField();
	private Label hladajOzn = new Label("Vyhladaj podla cisla objednavky");
	private Label vypis = new Label(" ");

	public InfoObjednavka(Stage InfoObjStage, Transport transport) {
		super();
		FlowPane Pane = new FlowPane();
		Pane.setPadding(new Insets(20));
		Pane.setHgap(10);
		Pane.setVgap(10);
		Pane.getChildren().add(spat);
		Pane.getChildren().add(hladajOzn);
		Pane.getChildren().add(cisloOBJ);
		Pane.getChildren().add(hladaj);
		Pane.getChildren().add(vypis);
		Pane.getChildren().add(prevziat);
		prevziat.setVisible(false);

		hladaj.setOnAction(e -> {
			try {
				vypis.setText(transport.VyhladanieTlacidlo(Integer.parseInt(cisloOBJ.getText())));
				prevziat.setVisible(transport.VyhladanieTlacidlo(3,
						transport.VyhladajObjednavku(Integer.parseInt(cisloOBJ.getText()))));
			} catch (NumberFormatException e1) {
				Alert chyba = new Alert(AlertType.ERROR);
				chyba.setTitle("Chyba");
				chyba.setContentText("Nezadane cislo");
				chyba.showAndWait();
			}
		});

		prevziat.setOnAction(e -> {
			try {
				vypis.setText(transport.prevzatie(Integer.parseInt(cisloOBJ.getText())));
				new ZakaznikOkno(InfoObjStage, transport);
			} catch (NumberFormatException e1) {
				Alert chyba = new Alert(AlertType.ERROR);
				chyba.setTitle("Chyba");
				chyba.setContentText("Nezadane cislo");
				chyba.showAndWait();
			}
		});

		InfoObjStage.setOnCloseRequest(e -> {
			try {
				transport.uloz();
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
		});

		spat.setOnAction(e -> new ZakaznikOkno(InfoObjStage, transport));
		InfoObjStage.setScene(new Scene(Pane, 500, 350));
	}
}
