package GUI;

import java.io.IOException;

import Preprava.Transport;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Vypis zavozneho planu
 * 
 * @author jofy
 *
 */
public class VypisPlanu extends Stage {
	public static final long serialVersionUID = 0;

	private Button spat = new Button("Spat");
	private TextArea vypis = new TextArea();

	public VypisPlanu(Stage VypisPlanuStage, Transport transport) {
		super();
		GridPane gridPane = new GridPane();

		gridPane.setPadding(new Insets(20));
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.add(spat, 0, 0, 1, 1);
		gridPane.add(vypis, 0, 1, 1, 1);

		vypis.setEditable(false);

		new Thread() {
			@Override
			public void run() {
				vypis.setText(transport.TvorbaPlanu());
			}
		}.start();

		VypisPlanuStage.setOnCloseRequest(e -> {
			try {
				transport.uloz();
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
		});

		spat.setOnAction(e -> new ManazerOkno(VypisPlanuStage, transport));

		VypisPlanuStage.setScene(new Scene(gridPane, 500, 200));

	}
}
