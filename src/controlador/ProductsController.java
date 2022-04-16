package controlador;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.TreeSet;

import javax.persistence.EntityManager;

import model.Pack;
import model.Product;
import model.ProductDAO;
import utils.AlertWindow;
import utils.GenericFormatter;

import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ProductsController {

    private ProductDAO dao;

    @FXML
    private TextField idTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextField stockTextField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private CheckBox isPack;
    @FXML
    private TextField guiDiscount;
    @FXML
    private TextArea guiProdList;

    @FXML
    private GridPane packContainer;

    private Stage ventana;

    private ResourceBundle texts;

    // utilizo un validador para packs para que cuando se quiera crear un producto
    // no valide los campos de pack
    private ValidationSupport vsProd;
    private ValidationSupport vsPack;

	private EntityManager em;

	public void setDBConnection(EntityManager em) throws IOException {
		this.em = em;
		dao = new ProductDAO(em);
	}

    @FXML
    private void initialize() throws IOException {
        texts = GenericFormatter.getResourceBundle();
        String priceRegex = "[0-9]{1,9}\\.[0-9]{1,2}|[0-9]{1,9}";
        String numRegex = "\\d{1,9}";
        // La lista de productos de un pack es una serie de números separados por una
        // coma
        String prodListRegex = "^$|^(([0-9]{1,9})([,][0-9]{1,9})*)$";

        vsProd = new ValidationSupport();
        vsPack = new ValidationSupport();
        vsProd.registerValidator(idTextField, true, Validator.createEmptyValidator(texts.getString("alert.prod.id")));
        vsProd.registerValidator(nameTextField, true,
                Validator.createEmptyValidator(texts.getString("alert.prod.name")));
        vsProd.registerValidator(priceTextField,
                Validator.createRegexValidator(texts.getString("alert.prod.price"), priceRegex, Severity.ERROR));
        vsProd.registerValidator(stockTextField,
                Validator.createRegexValidator(texts.getString("alert.prod.stock"), numRegex, Severity.ERROR));
        vsProd.registerValidator(startDatePicker, true,
                Validator.createEmptyValidator(texts.getString("alert.prod.datestart")));
        vsProd.registerValidator(endDatePicker, true,
                Validator.createEmptyValidator(texts.getString("alert.prod.dateend")));
        vsPack.registerValidator(guiDiscount,
                Validator.createRegexValidator(texts.getString("alert.pack.discount"), numRegex, Severity.ERROR));
        vsPack.registerValidator(guiProdList, Validator.createRegexValidator(texts.getString("alert.pack.prods"),
                prodListRegex, Severity.ERROR));
    }

    public Stage getVentana() {
        return ventana;
    }

    public void setVentana(Stage ventana) {
        this.ventana = ventana;
    }

    public void onCloseWindow() throws IOException {
    }

    @FXML
    private void onActionExit(ActionEvent e) throws IOException {
        ventana.close();
    }

    @FXML
    private void addProd() {
        // TODO
        if (isPack.isSelected()) {
            // stockTextField.setText("1");
        }
        boolean productValid = isProductValid(vsProd);
        boolean packValid = isProductValid(vsPack);
        // Añado producto si elijo se quiere añadir un producto si los campos de
        // producto son válidos o si se quiere añadir un producto y los campos de pack y
        // producto son válidos
        if ((!isPack.isSelected() && productValid) || (isPack.isSelected() && packValid && productValid)) {
            Product prod = getProductFromForm();
            if (prod.getStartCatalog().isAfter(prod.getEndCatalog())) {
                AlertWindow.show(ventana, "Error", "La fecha de inicio de catálogo no puede ser después de la fecha fin", "");
            } else {
                if (dao.get(getProductId(), isPack.isSelected()) == null) {
                    System.out.println();
                    AlertWindow.show(ventana, texts.getString("alert.prod.createtitle"),
                            texts.getString("alert.prod.createprod"), "");
                    dao.add(prod);
                    try {
                        dao.load();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    AlertWindow.show(ventana, texts.getString("alert.prod.createtitle"),
                            texts.getString("alert.prod.prodmodify"), "");
                    dao.modify(prod);
                    try {
                        dao.load();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    private boolean isProductValid(ValidationSupport vs) {
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
    private void onKeyPressedId(KeyEvent e) throws IOException {
        if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.TAB) {
        	System.out.println(isPack.isSelected());
        	System.out.println(dao.get(getProductId(), isPack.isSelected()));
            if (dao.get(getProductId(), isPack.isSelected()) != null) {
                if (isPack.isSelected()) {
                    isPack.setSelected(true);
                    packContainer.setVisible(true);
                    Pack product = (Pack) dao.get(getProductId(), true);
    
                    guiDiscount.setText(product.getDiscount().toString());
                    guiProdList.setText("");
                    for (Product prod : product.getProdList()) {
                        if (product.getProdList().last().equals(prod)) {
                            guiProdList.setText(guiProdList.getText() + prod.getId());
                        } else {
                            guiProdList.setText(guiProdList.getText() + prod.getId() + ",");
    
                        }
                    }
                    stockTextField.setText(product.getStock().toString());
                    setProductFields(product);
                } else {
                    Product product = dao.get(getProductId(),false);
                    stockTextField.setText(product.getStock().toString());
                    isPack.setSelected(false);
                    packContainer.setVisible(false);
                    setProductFields(product);
                }
            } else {
                nameTextField.setText("");
                priceTextField.setText("");
                if (!isPack.isSelected()) {
                    stockTextField.setText("");
                }
                startDatePicker.setValue(null);
                endDatePicker.setValue(null);
                guiDiscount.setText("");
                guiProdList.setText("");
            }
        }
    }

    private void setProductFields(Product prod) {
        nameTextField.setText(prod.getName());
        priceTextField.setText(prod.getPrice().toString());
        startDatePicker.setValue(prod.getStartCatalog());
        endDatePicker.setValue(prod.getEndCatalog());
    }

    // Devuelve una instancia de producto con la informacion de la GUI
    private Product getProductFromForm() {
        Integer id = getProductId();
        String name = nameTextField.getText();
        double price = Double.parseDouble(priceTextField.getText());
        LocalDate startCatalog = startDatePicker.getValue();
        LocalDate endCatalog = endDatePicker.getValue();

        if (!isPack.isSelected()) {
            Integer stock = Integer.parseInt(stockTextField.getText());
            Product product = new Product(id, name, price, stock, startCatalog, endCatalog);
            return (Product) product;
        } else {
            Integer discount = Integer.parseInt(guiDiscount.getText());

            TreeSet<Product> productList = new TreeSet<>();
            if (!guiProdList.getText().equals("")) {
                String[] prodIds = guiProdList.getText().split(",");
                for (String idProd : prodIds) {
                    if (dao.get(Integer.parseInt(idProd),false) != null && dao.get(Integer.parseInt(idProd), false) instanceof Product) {
                        productList.add(dao.get(Integer.parseInt(idProd),false));
                    }
                }
            }
            Pack product = new Pack(productList, discount, id, name, price, startCatalog, endCatalog, null);
            System.out.println("pack");
            System.out.println(product.toString());
            return (Product) product;
        }

    }

    private Integer getProductId() {
        return Integer.parseInt(idTextField.getText());
    }

    @FXML
    private void deleteProd() {
        int id = getProductId();
        if (dao.get(id, isPack.isSelected()) != null) {
            // Aviso que el producto se ha podido borrar
            AlertWindow.show(ventana, texts.getString("alert.prod.deletetitle"),
                    texts.getString("alert.prod.deletecontent"), "");
            dao.delete(dao.get(id, isPack.isSelected()));
        } else {
            // Aviso que no se ha podido borrar el producto
            AlertWindow.show(ventana, texts.getString("alert.prod.deletetitle"),
                    texts.getString("alert.prod.prodnotfound"), "");
        }
    }


    @FXML
    private void list() {
        System.out.println(dao.getPackMap());
    }

    // Función que llamo cuando se hace clic en el checkbox de pack, oculta o
    // muestra según si se quiere añadir un producto o pack
    @FXML
    private void pack() {
        if (!isPack.isSelected()) {
            packContainer.setVisible(false);
            stockTextField.setDisable(false);
        } else {
            packContainer.setVisible(true);
            stockTextField.setDisable(true);
            stockTextField.setText("0");
        }
    }

}
