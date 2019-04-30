package GUI;

import java.io.IOException;
import Preprava.Transport;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 * Prepinanie uzivatelov Uzivatel/ Manazer
 * 
 * @author jofy
 *
 */
public class VyberUzivatela extends Stage {
	public static final long serialVersionUID = 0;

	private Button manazer = new Button("Manazer");
	private Button zakaznik = new Button("Zakaznik");

	public VyberUzivatela(Stage HlavnyStage, Transport transport) {
		super();
		HlavnyStage.setTitle("Hodinkomat");
		FlowPane HlavnyPane = new FlowPane();
		HlavnyPane.setPadding(new Insets(20));
		HlavnyPane.setHgap(20);
		HlavnyPane.setVgap(20);
		HlavnyPane.getChildren().add(manazer);
		HlavnyPane.getChildren().add(zakaznik);

		HlavnyStage.setOnCloseRequest(e -> {
			try {
				transport.uloz();
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
		});

		manazer.setOnAction(e -> new ManazerOkno(HlavnyStage, transport));
		zakaznik.setOnAction(e -> new ZakaznikOkno(HlavnyStage, transport));

		HlavnyStage.setScene(new Scene(HlavnyPane, 230, 100));
		HlavnyStage.show();
	}
}
