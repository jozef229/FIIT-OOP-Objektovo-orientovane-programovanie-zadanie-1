
package GUI;

import java.io.IOException;
import Preprava.Transport;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 * Prvotne spustenie gui main a vytvorenie Transportu
 * 
 * @author jofy
 *
 */
public class StartGUI_Uvitanie extends Application {
	public static final long serialVersionUID = 0;

	public void start(Stage HlavnyStage) {

		HlavnyStage.setTitle("Hodinkomat");
		FlowPane HlavnyPane = new FlowPane();
		Transport transport = new Transport();
		transport.TvorbaSkladov();
		try {
			transport.nacitaj();
			transport.uloz();
		} catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
		}
		HlavnyStage.setScene(new Scene(HlavnyPane, 230, 100));
		HlavnyStage.show();
		new VyberUzivatela(HlavnyStage, transport);
	}

	public static void main(String[] args) {
		launch(args);
	}
}