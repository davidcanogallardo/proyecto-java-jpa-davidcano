package controlador;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Client;
import model.ClientDAO;
import utils.GenericFormatter;

public class ClientsMenuController {

	private ClientDAO dao;

	private ResourceBundle texts;
	private Stage ventana;

	@FXML
	private Button btnAdd;
	@FXML
	private Button btnList;
	@FXML
	private Button btnReturn;

	private EntityManager em;

	public void setDBConnection(EntityManager em) throws IOException {
		this.em = em;
		dao = new ClientDAO(em);
	}

	@FXML
	private void initialize() throws IOException {
		texts = GenericFormatter.getResourceBundle();
	}

	public Stage getVentana() {
		return ventana;
	}

	public void setVentana(Stage ventana) {
		this.ventana = ventana;
	}

	@FXML
	private void onActionSortir(ActionEvent e) throws IOException {
		ventana.close();
	}

	@FXML
	private void onAction(ActionEvent e) throws Exception {
		if (e.getSource() == btnAdd) {
			changeScene("/vista/ClientsView.fxml", texts.getString("clientform.title"));
		} else if (e.getSource() == btnList) {
			for (Client client : dao.getPackMap()) {
				System.out.println(client.toString() + "\n");
			}
		} else if (e.getSource() == btnReturn) {
			Platform.exit();
		}
	}

	private void changeScene(String path, String title) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(path));

		loader.setResources(texts);

		Stage stage = new Stage();
		Scene fm_scene = new Scene(loader.load());
		stage.setTitle(title);
		stage.setScene(fm_scene);
		stage.show();

		if (title.equals(texts.getString("clientform.title"))) {
			ClientsController clientsAdd = loader.getController();
			clientsAdd.setDBConnection(em);
			clientsAdd.setVentana(stage);

			stage.setOnCloseRequest((WindowEvent we) -> {
				try {
					clientsAdd.sortir();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
	}

}
