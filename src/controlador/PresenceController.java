package controlador;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;

import model.Presence;
import model.PresenceRegisterDAO;
import utils.AlertWindow;
import utils.GenericFormatter;

import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PresenceController {

    private PresenceRegisterDAO dao;

    @FXML
    private TextField guiId;

    @FXML
    private Button guiClockIn;
    @FXML
    private Button guiClockOut;

    private Stage ventana;

    private ValidationSupport vs;
    private ResourceBundle texts;

	private EntityManager em;

	public void setDBConnection(EntityManager em) throws IOException {
		this.em = em;
		dao = new PresenceRegisterDAO(em);
	}
    @FXML
    private void initialize() throws IOException {
        texts = GenericFormatter.getResourceBundle();

        vs = new ValidationSupport();
        vs.registerValidator(guiId, true, Validator.createEmptyValidator(texts.getString("alert.presence.id")));
    }

    public Stage getVentana() {
        return ventana;
    }

    public void setVentana(Stage ventana) {
        this.ventana = ventana;
    }

    public void sortir() throws IOException {
    }

    @FXML
    private boolean isDatosValidos() {
        if (vs.isInvalid()) {
            String errors = vs.getValidationResult().getMessages().toString();
            String title = GenericFormatter.getResourceBundle().getString("alert.title");
            String header = GenericFormatter.getResourceBundle().getString("alert.message");
            AlertWindow.show(ventana, title, header, errors);

            return false;
        }

        return true;
    }

    @FXML
    private void onActionSortir(ActionEvent e) throws IOException {
        ventana.close();
    }

    @FXML
    private void onAction(ActionEvent e) throws Exception {
        if (e.getSource() == guiClockIn) {
            if (isDatosValidos()) {
                Presence presence = new Presence(Integer.parseInt(guiId.getText()), LocalDateTime.now());
                if (dao.add(presence) == null) {
                    AlertWindow.show(ventana, "Error", texts.getString("alert.presence.clockin"), "");
                }
            }
        } else if (e.getSource() == guiClockOut) {
            if (isDatosValidos()) {
                if (!dao.addLeaveTime(Integer.parseInt(guiId.getText()))) {
                    AlertWindow.show(ventana, "Error2", texts.getString("alert.presence.clockout"), "");
                }
            }
        }
        System.out.println("--------------------------");
        dao.list();
        System.out.println("--------------------------");
    }

    @FXML
    private void list() {
        dao.list();
    }

}
