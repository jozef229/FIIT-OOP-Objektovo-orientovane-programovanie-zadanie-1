package GUI;

import java.io.IOException;

import Preprava.Transport;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 * Vymazavanie a prehlad objednavok
 * 
 * @author jofy
 *
 */
public class PrehladObjednavok extends Stage {
	public static final long serialVersionUID = 0;

	private Button potvrdenie = new Button("Potvrdenie");
	private Button spat = new Button("Spat");
	private TextArea vypis = new TextArea();
	private TextField cisloOBJ = new TextField();
	private Label mazanie = new Label("Cislo objednavky na vymazanie");

	public PrehladObjednavok(Stage PrehladObjednavokStage, Transport transport, ProgressBar stav_bar,
			Label stav_kuriera) {

		super();
		FlowPane Pane = new FlowPane();

		Pane.setPadding(new Insets(20));
		Pane.setHgap(10);
		Pane.setVgap(10);
		Pane.getChildren().add(spat);
		Pane.getChildren().add(cisloOBJ);
		Pane.getChildren().add(mazanie);
		Pane.getChildren().add(potvrdenie);
		Pane.getChildren().add(vypis);
		Pane.getChildren().add(stav_kuriera);
		Pane.getChildren().add(stav_bar);
		vypis.setEditable(false);

		vypis.setText(transport.VypisBalikALL());
		transport.setCoJeOtvorene(1);
		transport.RefresPrehladObjednavky(this, transport);
		potvrdenie.setOnAction(e -> {
			try {
				transport.VymazBalik(transport.VyhladajObjednavku(Integer.parseInt(cisloOBJ.getText())));
				vypis.setText(transport.VypisBalikALL());
				cisloOBJ.clear();
			} catch (NumberFormatException e1) {
				Alert chyba = new Alert(AlertType.ERROR);
				chyba.setTitle("Chyba");
				chyba.setContentText("Nezadane cislo");
				chyba.showAndWait();
			}
		});

		PrehladObjednavokStage.setOnCloseRequest(e -> {
			try {
				transport.uloz();
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
		});

		spat.setOnAction(e -> new ManazerOkno(PrehladObjednavokStage, transport));
		PrehladObjednavokStage.setScene(new Scene(Pane));
	}

	public void setRefres(Transport transport) {
		vypis.setText(transport.VypisBalikALL());
	}
}