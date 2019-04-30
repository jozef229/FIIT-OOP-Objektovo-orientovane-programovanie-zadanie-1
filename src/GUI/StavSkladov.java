package GUI;

import java.io.IOException;

import Preprava.Transport;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 * Zistovanie stavu skladov
 * 
 * @author jofy
 *
 */
public class StavSkladov extends Stage {
	public static final long serialVersionUID = 0;

	private Button spat = new Button("Spat");
	private TextArea vypis = new TextArea();

	public StavSkladov(Stage StavSkladovStage, Transport transport, ProgressBar stav_bar, Label stav_kuriera) {
		super();
		FlowPane Pane = new FlowPane();

		Pane.setPadding(new Insets(20));
		Pane.setHgap(10);
		Pane.setVgap(10);
		Pane.getChildren().add(spat);
		Pane.getChildren().add(vypis);
		Pane.getChildren().add(stav_kuriera);
		Pane.getChildren().add(stav_bar);
		transport.setCoJeOtvorene(2);
		transport.RefresStavSkladov(this, transport);
		vypis.setEditable(false);
		vypis.setText(transport.VypisSkladovALL());
		spat.setOnAction(e -> new ManazerOkno(StavSkladovStage, transport));
		StavSkladovStage.setScene(new Scene(Pane));

		StavSkladovStage.setOnCloseRequest(e -> {
			try {
				transport.uloz();
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
		});

	}

	public void setRefres(Transport transport) {
		vypis.setText(transport.VypisSkladovALL());
	}
}