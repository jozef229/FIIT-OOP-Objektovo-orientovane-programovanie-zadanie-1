package GUI;

import java.io.IOException;

import Preprava.Transport;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Manazerske menu a spracovanie progressbaru
 * 
 * @author jofy
 *
 */
public class ManazerOkno extends Stage {
	public static final long serialVersionUID = 0;

	private Button spat = new Button("Spat");
	private Button dalsi_den = new Button("Dalsi den");
	private Button vytvor_plan = new Button("Vytvorenie/Vypis planu");
	private Button startkuriera = new Button("Start sofer");
	private Button stav_skladov = new Button("Stav skladov");
	private Button prehlad_obj = new Button("Prehlad objednavok");
	private ProgressBar stav_bar = new ProgressBar(0);
	private Label stav_kuriera = new Label("Stav kuriera na ceste:");

	public ManazerOkno(Stage ManazerStage, Transport transport) {
		super();

		transport.KontrolaStavu(this);
		transport.setCoJeOtvorene(0);
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(10));
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.add(spat, 0, 0, 1, 1);
		gridPane.add(vytvor_plan, 1, 0, 1, 1);
		gridPane.add(stav_skladov, 2, 1, 1, 1);
		gridPane.add(prehlad_obj, 1, 1, 1, 1);
		gridPane.add(startkuriera, 2, 0, 1, 1);
		gridPane.add(dalsi_den, 1, 2, 1, 1);
		gridPane.add(stav_kuriera, 1, 3, 1, 1);
		gridPane.add(stav_bar, 2, 3, 1, 1);

		spat.setOnAction(e -> new VyberUzivatela(ManazerStage, transport));
		stav_skladov.setOnAction(e -> new StavSkladov(ManazerStage, transport, stav_bar, stav_kuriera));
		prehlad_obj.setOnAction(e -> new PrehladObjednavok(ManazerStage, transport, stav_bar, stav_kuriera));
		vytvor_plan.setOnAction(e -> {
			new VypisPlanu(ManazerStage, transport);
			startkuriera.setDisable(transport.VratPocetTrueSkladov() == 0);
		});
		startkuriera.setOnAction(e -> {
			transport.NaplnanieCasu(this);
			stav_bar.setVisible(transport.VratPocetTrueSkladov() != 0);
			stav_kuriera.setVisible(transport.VratPocetTrueSkladov() != 0);
		});
		dalsi_den.setOnAction(e -> transport.DalsiDen());
		stav_bar.setVisible(false);
		stav_kuriera.setVisible(false);
		startkuriera.setDisable(transport.VratPocetTrueSkladov() == 0);

		ManazerStage.setOnCloseRequest(e -> {
			try {
				transport.uloz();
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
		});

		ManazerStage.setScene(new Scene(gridPane));
	}

	public void stav(double i) {
		stav_bar.setVisible(i != 0.0);
		stav_kuriera.setVisible(i != 0.0);
		startkuriera.setDisable(true);
		dalsi_den.setDisable(i != 0.0);
		vytvor_plan.setDisable(i != 0.0);
		stav_bar.setProgress(i);
	}

}
