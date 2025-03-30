package controller.product;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.admin.AdminDashboardFormController;
import controller.staff.StaffDashboardFormController;
import entity.AdminEntity;
import entity.ProductEntity;
import entity.StaffEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.ProductService;
import util.AdminSession;
import util.AlertUtil;
import util.ServiceType;
import util.StaffSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProductManagementFormController implements Initializable {

    @FXML
    private JFXComboBox<String> cmbCategory;

    @FXML
    private JFXComboBox<String> cmbSize;

    @FXML
    private TableColumn<ProductEntity, String> colCategory;

    @FXML
    private TableColumn<ProductEntity, String> colName;

    @FXML
    private TableColumn<ProductEntity, String> colProductID;

    @FXML
    private TableColumn<ProductEntity, Integer> colQty;

    @FXML
    private TableColumn<ProductEntity, String> colSize;

    @FXML
    private TableColumn<ProductEntity, BigDecimal> colUnitPrice;

    @FXML
    private Label labelProductID;

    @FXML
    private TableView<ProductEntity> tblProductTable;

    @FXML
    private Text txtID;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private TextField txtSearchField;

    @FXML
    private JFXTextField txtUnitPrice;

    private String role;

    private final ProductService productService =
            ServiceFactory.getInstance().getServiceType(ServiceType.PRODUCT_SERVICE);

    public void setRoleAndUser(String role, String id) {
        this.role = role;
        txtID.setText(id);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCategory();
        loadSize();
        loadProductTable();

        String newProductId = productService.generateProductId();
        labelProductID.setText(newProductId);
    }

    private void loadProductTable() {
        colProductID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));

        List<ProductEntity> productList = productService.findAllProducts();
        ObservableList<ProductEntity> observableProductList = FXCollections.observableArrayList(productList);
        tblProductTable.setItems(observableProductList);
    }

    private void loadCategory() {
        ObservableList<String> categories = FXCollections.observableArrayList(
                "Men's Wear",
                "Women's Wear",
                "Children's Wear"
        );

        cmbCategory.setItems(categories);
    }

    private void loadSize() {
        ObservableList<String> sizes = FXCollections.observableArrayList(
                "XS",
                "S",
                "M",
                "L",
                "XL",
                "XXL"
        );

        cmbSize.setItems(sizes);
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (!validateFields()) return;

        String productId = labelProductID.getText();
        String name = txtName.getText();
        String category = cmbCategory.getValue();
        String size = cmbSize.getValue();
        BigDecimal unitPrice = new BigDecimal(txtUnitPrice.getText());
        int qtyOnHand = Integer.parseInt(txtQty.getText());

        ProductEntity newProduct = new ProductEntity(productId, name, category, size, unitPrice, qtyOnHand);

        if (productService.addProduct(newProduct)) {
            AlertUtil.showAlert("Success", "Product added successfully!", null, javafx.scene.control.Alert.AlertType.INFORMATION);
            loadProductTable();
            clearTextFields();
        } else {
            AlertUtil.showAlert("Error", "Failed to add product. Please try again.", null, javafx.scene.control.Alert.AlertType.ERROR);
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        ProductEntity selectedProduct = tblProductTable.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            if (productService.deleteProduct(selectedProduct.getProductId())) {
                AlertUtil.showAlert("Success", "Product deleted successfully!", null, javafx.scene.control.Alert.AlertType.INFORMATION);
                loadProductTable();
            } else {
                AlertUtil.showAlert("Error", "Failed to delete product. Please try again.", null, javafx.scene.control.Alert.AlertType.ERROR);
            }
        } else {
            AlertUtil.showAlert("Warning", "Please select a product to delete.", null, javafx.scene.control.Alert.AlertType.WARNING);
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String searchId = txtSearchField.getText();
        if (searchId.isEmpty()) {
            AlertUtil.showAlert("Input Error", "Please enter a Product ID to search.", null, javafx.scene.control.Alert.AlertType.WARNING);
            return;
        }

        ProductEntity foundProduct = productService.findProductById(searchId);

        if (foundProduct != null) {
            labelProductID.setText(foundProduct.getProductId());
            txtName.setText(foundProduct.getName());
            cmbCategory.setValue(foundProduct.getCategory());
            cmbSize.setValue(foundProduct.getSize());
            txtUnitPrice.setText(foundProduct.getUnitPrice().toString());
            txtQty.setText(String.valueOf(foundProduct.getQtyOnHand()));
        } else {
            AlertUtil.showAlert("Not Found", "Product not found. Please check the ID and try again.", null, javafx.scene.control.Alert.AlertType.ERROR);
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validateFields()) return;

        String productId = labelProductID.getText();
        String name = txtName.getText();
        String category = cmbCategory.getValue();
        String size = cmbSize.getValue();
        BigDecimal unitPrice = new BigDecimal(txtUnitPrice.getText());
        int qtyOnHand = Integer.parseInt(txtQty.getText());

        ProductEntity updatedProduct = new ProductEntity(productId, name, category, size, unitPrice, qtyOnHand);

        if (productService.updateProduct(updatedProduct)) {
            AlertUtil.showAlert("Success", "Product updated successfully!", null, javafx.scene.control.Alert.AlertType.INFORMATION);
            loadProductTable();
        } else {
            AlertUtil.showAlert("Error", "Failed to update product. Please try again.", null, javafx.scene.control.Alert.AlertType.ERROR);
        }
    }

    @FXML
    void btnDashboardOnAction(ActionEvent event) {
        try {
            FXMLLoader loader;
            if (role.equals("Admin")) {
                loader = new FXMLLoader(getClass().getResource("../../view/admin_dashboard_form.fxml"));
                Parent root = loader.load();

                AdminDashboardFormController adminDashboardFormController = loader.getController();
                AdminEntity admin = AdminSession.getInstance().getAdmin();
                adminDashboardFormController.setAdminUsername(admin.getUsername());
            } else {
                loader = new FXMLLoader(getClass().getResource("../../view/staff_dashboard_form.fxml"));
                Parent root = loader.load();

                StaffDashboardFormController controller = loader.getController();
                StaffEntity staff = StaffSession.getInstance().getStaff();
                controller.setStaffUsername(staff.getUsername());
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(loader.getRoot()));
            stage.setTitle(role + " Dashboard");
            stage.setResizable(false);
            stage.show();
            stage.centerOnScreen();

            Stage disposeStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            disposeStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearTextFields();
    }

    private void clearTextFields() {
        txtName.clear();
        cmbCategory.setValue(null);
        cmbSize.setValue(null);
        txtQty.clear();
        txtUnitPrice.clear();
        txtSearchField.clear();

        String newProductId = productService.generateProductId();
        labelProductID.setText(newProductId);
    }

    public void handleMouseClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            ProductEntity selectedProduct = tblProductTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                populateFields(selectedProduct);
            }
        }
    }

    private void populateFields(ProductEntity product) {
        labelProductID.setText(product.getProductId());
        txtName.setText(product.getName());
        cmbCategory.setValue(product.getCategory());
        cmbSize.setValue(product.getSize());
        txtUnitPrice.setText(product.getUnitPrice().toString());
        txtQty.setText(String.valueOf(product.getQtyOnHand()));
    }

    private boolean validateFields() {
        if (txtName.getText().isEmpty()) {
            AlertUtil.showAlert("Validation Error", "Product Name cannot be empty.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        if (cmbCategory.getValue() == null) {
            AlertUtil.showAlert("Validation Error", "Please select a Category.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        if (cmbSize.getValue() == null) {
            AlertUtil.showAlert("Validation Error", "Please select a Size.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        String unitPriceStr = txtUnitPrice.getText();
        if (unitPriceStr.isEmpty() || !isValidDecimal(unitPriceStr)) {
            AlertUtil.showAlert("Validation Error", "Unit Price must be a valid positive number.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        String qtyStr = txtQty.getText();
        if (qtyStr.isEmpty() || !isValidInteger(qtyStr)) {
            AlertUtil.showAlert("Validation Error", "Quantity must be a valid positive integer.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private boolean isValidDecimal(String str) {
        try {
            BigDecimal value = new BigDecimal(str);
            return value.compareTo(BigDecimal.ZERO) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidInteger(String str) {
        try {
            int value = Integer.parseInt(str);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
