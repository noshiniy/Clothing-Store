package controller.supply;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.admin.AdminDashboardFormController;
import controller.staff.StaffDashboardFormController;
import entity.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.ServiceFactory;
import service.custom.ProductService;
import service.custom.SupplierService;
import service.custom.SupplyService;
import util.AdminSession;
import util.AlertUtil;
import util.ServiceType;
import util.StaffSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplyManagementFormController implements Initializable {

    @FXML
    private JFXComboBox<String> cmbCategory;

    @FXML
    private JFXComboBox<String> cmbProductID;

    @FXML
    private JFXComboBox<String> cmbSize;

    @FXML
    private JFXComboBox<String> cmbSupplierID;

    @FXML
    private TableView<SupplyCartEntity> tblSupplyCart;

    @FXML
    private TableColumn<SupplyCartEntity, String> colSupplyCartCategory;

    @FXML
    private TableColumn<SupplyCartEntity, String> colSupplyCartName;

    @FXML
    private TableColumn<SupplyCartEntity, String> colSupplyCartProductID;

    @FXML
    private TableColumn<SupplyCartEntity, Integer> colSupplyCartQty;

    @FXML
    private TableColumn<SupplyCartEntity, String> colSupplyCartSize;

    @FXML
    private TableColumn<SupplyCartEntity, String> colSupplyCartSupplierId;

    @FXML
    private TableColumn<SupplyCartEntity, BigDecimal> colSupplyCartTotal;

    @FXML
    private TableColumn<SupplyCartEntity, BigDecimal> colSupplyCartUnitCost;

    @FXML
    private TableView<SupplyEntity> tblSupplyTable;

    @FXML
    private TableColumn<SupplyEntity, LocalDateTime> colDateTime;

    @FXML
    private TableColumn<SupplyEntity, String> colSupplierID;

    @FXML
    private TableColumn<SupplyEntity, String> colSupplyID;

    @FXML
    private TableColumn<SupplyEntity, String> colSupplyTableProductID;

    @FXML
    private TableColumn<SupplyEntity, Integer> colSupplyTableQty;

    @FXML
    private TableColumn<SupplyEntity, BigDecimal> colSupplyTableTotal;

    @FXML
    private TableColumn<SupplyEntity, BigDecimal> colSupplyTableUnitCost;

    @FXML
    private Label labelSupplyID;

    @FXML
    private Text txtID;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private JFXTextField txtQtyOnHand;

    @FXML
    private TextField txtSearchField;

    @FXML
    private JFXTextField txtTotal;

    @FXML
    private JFXTextField txtUnitCost;

    @FXML
    private JFXTextField txtUnitPrice;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;

    @FXML
    private Button btnSearch;

    private String role;

    private final SupplyService supplyService = ServiceFactory.getInstance().getServiceType(ServiceType.SUPPLY_SERVICE);
    private final SupplierService supplierService = ServiceFactory.getInstance().getServiceType(ServiceType.SUPPLIER_SERVICE);
    private final ProductService productService = ServiceFactory.getInstance().getServiceType(ServiceType.PRODUCT_SERVICE);

    private ObservableList<SupplyCartEntity> supplyCart = FXCollections.observableArrayList();

    public void setRoleAndUser(String role, String id) {
        this.role = role;
        txtID.setText(id);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDateAndTime();
        loadProductIds();
        loadSupplierIds();
        loadCategory();
        loadSize();

        cmbProductID.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadProductDetails(newValue);
            }
        });

        initializeSupplyCart();

        txtQty.textProperty().addListener((obs, oldText, newText) -> calculateTotal());
        txtUnitCost.textProperty().addListener((obs, oldText, newText) -> calculateTotal());

        initializeSupplyTable();

        String initialSupplyId = supplyService.generateSupplyId();
        labelSupplyID.setText(initialSupplyId);

        txtSearchField.setVisible(false);
        btnSearch.setVisible(false);

        tblSupplyTable.setOnMouseClicked(this::handleMouseClick);
    }

    private void initializeSupplyTable() {
        colSupplyID.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId().getSupplyId()));
        colSupplyTableProductID.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId().getProductId()));
        colSupplierID.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId().getSupplierId()));
        colSupplyTableUnitCost.setCellValueFactory(new PropertyValueFactory<>("unitCost"));
        colSupplyTableQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colSupplyTableTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colDateTime.setCellValueFactory(new PropertyValueFactory<>("dateAndTime"));

        loadSupplyTableData();
    }

    private void loadSupplyTableData() {
        List<SupplyEntity> supplyList = supplyService.findAllSupplies();
        ObservableList<SupplyEntity> observableSupplyList = FXCollections.observableArrayList(supplyList);
        tblSupplyTable.setItems(observableSupplyList);
    }

    private void initializeSupplyCart() {
        colSupplyCartProductID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colSupplyCartSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colSupplyCartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSupplyCartCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colSupplyCartSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colSupplyCartUnitCost.setCellValueFactory(new PropertyValueFactory<>("unitCost"));
        colSupplyCartQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colSupplyCartTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        tblSupplyCart.setItems(supplyCart);
    }

    private void loadProductDetails(String productId) {
        ProductEntity product = productService.findProductById(productId);

        if (product != null) {
            txtName.setText(product.getName());
            cmbCategory.setValue(product.getCategory());
            cmbSize.setValue(product.getSize());
            txtUnitPrice.setText(String.valueOf(product.getUnitPrice()));
            txtQtyOnHand.setText(String.valueOf(product.getQtyOnHand()));
        } else {
            txtName.clear();
            txtUnitPrice.clear();
            txtQtyOnHand.clear();
            cmbCategory.setValue(null);
            cmbSize.setValue(null);
        }
    }


    private void loadSupplierIds() {
        List<SupplierEntity> suppliersList = supplierService.findAllSuppliers();
        ObservableList<String> supplierIds = FXCollections.observableArrayList();

        for (SupplierEntity supplier : suppliersList) {
            supplierIds.add(supplier.getSupplierId());
        }

        cmbSupplierID.getItems().addAll(supplierIds);
    }

    private void loadProductIds() {
        List<ProductEntity> productList = productService.findAllProducts();
        ObservableList<String> productIds = FXCollections.observableArrayList();


        for (ProductEntity product : productList) {
            productIds.add(product.getProductId());
        }

        cmbProductID.getItems().addAll(productIds);
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
    void btnAddSupplyCartOnAction(ActionEvent event) {
        if (!validateInputFields()) {
            return;
        }

        String productId = cmbProductID.getValue();
        String supplierId = cmbSupplierID.getValue();
        String name = txtName.getText();
        String category = cmbCategory.getValue();
        String size = cmbSize.getValue();
        BigDecimal unitCost = new BigDecimal(txtUnitCost.getText());
        int qty = Integer.parseInt(txtQty.getText());

        SupplyCartEntity supplyCartItem = new SupplyCartEntity();
        supplyCartItem.setProductId(productId);
        supplyCartItem.setSupplierId(supplierId);
        supplyCartItem.setName(name);
        supplyCartItem.setCategory(category);
        supplyCartItem.setSize(size);
        supplyCartItem.setUnitCost(unitCost);
        supplyCartItem.setQuantity(qty);
        supplyCartItem.calculateTotal();

        supplyCart.add(supplyCartItem);
        tblSupplyCart.setItems(supplyCart);

        clearFields();

    }

    private void clearFields() {
        txtName.clear();
        txtUnitCost.clear();
        txtQty.clear();
        txtQtyOnHand.clear();
        txtTotal.clear();
        txtUnitPrice.clear();

        cmbProductID.setValue(null);
        cmbSupplierID.setValue(null);
        cmbCategory.setValue(null);
        cmbSize.setValue(null);
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (supplyCart.isEmpty()) {
            AlertUtil.showAlert("Error", "Supply Cart is empty. Add items before submitting.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return;
        }

        String currentSupplyId = labelSupplyID.getText();

        for (SupplyCartEntity cartItem : supplyCart) {
            SupplyEntity supply = new SupplyEntity(
                    new SupplyId(currentSupplyId, cartItem.getProductId(), cartItem.getSupplierId()),
                    cartItem.getUnitCost(),
                    cartItem.getQuantity(),
                    cartItem.getTotal(),
                    LocalDateTime.now()
            );

            boolean isSupplyAdded = supplyService.addSupply(supply);

            if (!isSupplyAdded) {
                AlertUtil.showAlert("Error", "Failed to add supply for Product ID: " + cartItem.getProductId(), null, javafx.scene.control.Alert.AlertType.ERROR);
                return;
            }
        }

        supplyCart.clear();
        tblSupplyCart.refresh();

        loadSupplyTable();

        String newSupplyId = supplyService.generateSupplyId();
        labelSupplyID.setText(newSupplyId);

        AlertUtil.showAlert("Success", "Supplies have been successfully added to the database.", null, javafx.scene.control.Alert.AlertType.INFORMATION);
    }

    private void loadSupplyTable() {
        List<SupplyEntity> supplyList = supplyService.findAllSupplies();
        ObservableList<SupplyEntity> supplyObservableList = FXCollections.observableArrayList(supplyList);
        tblSupplyTable.setItems(supplyObservableList);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        SupplyEntity selectedSupply = tblSupplyTable.getSelectionModel().getSelectedItem();

        if (selectedSupply == null) {
            AlertUtil.showAlert("Error", "Please select a supply to delete.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return;
        }

        String supplyId = selectedSupply.getId().getSupplyId();
        String productId = selectedSupply.getId().getProductId();
        String supplierId = selectedSupply.getId().getSupplierId();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this supply?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            boolean isDeleted = supplyService.deleteSupply(supplyId, productId, supplierId);

            if (isDeleted) {
                loadSupplyTable();
                AlertUtil.showAlert("Success", "Supply has been successfully deleted.", null, javafx.scene.control.Alert.AlertType.INFORMATION);
            } else {
                AlertUtil.showAlert("Error", "Failed to delete supply.", null, javafx.scene.control.Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validateInputFields()) {
            return;
        }

        SupplyEntity selectedSupply = tblSupplyTable.getSelectionModel().getSelectedItem();

        if (selectedSupply == null) {
            AlertUtil.showAlert("Error", "Please select a supply to update.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return;
        }

        String productId = cmbProductID.getValue();
        String supplierId = cmbSupplierID.getValue();
        BigDecimal unitCost = new BigDecimal(txtUnitCost.getText());
        int qty = Integer.parseInt(txtQty.getText());

        SupplyEntity updatedSupply = new SupplyEntity(
                new SupplyId(selectedSupply.getId().getSupplyId(), productId, supplierId),
                unitCost,
                qty,
                unitCost.multiply(BigDecimal.valueOf(qty)),
                LocalDateTime.now()
        );

        boolean isUpdated = supplyService.updateSupply(updatedSupply);

        if (isUpdated) {
            loadSupplyTable();
            AlertUtil.showAlert("Success", "Supply has been successfully updated.", null, javafx.scene.control.Alert.AlertType.INFORMATION);
        } else {
            AlertUtil.showAlert("Error", "Failed to update supply.", null, javafx.scene.control.Alert.AlertType.ERROR);
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {

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
        txtName.clear();
        txtUnitCost.clear();
        txtQty.clear();
        txtQtyOnHand.clear();
        txtTotal.clear();
        txtUnitPrice.clear();

        cmbProductID.setValue(null);
        cmbSupplierID.setValue(null);
        cmbCategory.setValue(null);
        cmbSize.setValue(null);

        tblSupplyCart.getSelectionModel().clearSelection();

        String newSupplyId = supplyService.generateSupplyId();
        labelSupplyID.setText(newSupplyId);

        tblSupplyTable.getSelectionModel().clearSelection();
    }

    public void loadDateAndTime() {
        //        Date
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = simpleDateFormat.format(date);
        lblDate.setText(formatDate);

        //        Time
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime now = LocalTime.now();
            lblTime.setText(now.getHour()+":"+now.getMinute()+":"+now.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

    private void calculateTotal() {
        BigDecimal unitCost;
        int qty;

        try {
            unitCost = new BigDecimal(txtUnitCost.getText());
            qty = Integer.parseInt(txtQty.getText());
            BigDecimal total = unitCost.multiply(BigDecimal.valueOf(qty));
            txtTotal.setText(total.toString());
        } catch (NumberFormatException e) {
            txtTotal.clear();
        }
    }


    public void handleMouseClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            SupplyEntity selectedSupply = tblSupplyTable.getSelectionModel().getSelectedItem();

            if (selectedSupply != null) {
                labelSupplyID.setText(selectedSupply.getId().getSupplyId());
                String productId = selectedSupply.getId().getProductId();
                String supplierId = selectedSupply.getId().getSupplierId();

                loadProductDetails(productId);

                txtUnitCost.setText(selectedSupply.getUnitCost().toString());
                txtQty.setText(String.valueOf(selectedSupply.getQty()));
                txtTotal.setText(selectedSupply.getTotal().toString());
            }
        }
    }

    private boolean validateInputFields() {
        if (cmbProductID.getValue() == null || cmbProductID.getValue().isEmpty()) {
            AlertUtil.showAlert("Validation Error", "Product ID is required.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        if (cmbSupplierID.getValue() == null || cmbSupplierID.getValue().isEmpty()) {
            AlertUtil.showAlert("Validation Error", "Supplier ID is required.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        if (txtName.getText() == null || txtName.getText().isEmpty()) {
            AlertUtil.showAlert("Validation Error", "Product Name is required.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        if (cmbCategory.getValue() == null || cmbCategory.getValue().isEmpty()) {
            AlertUtil.showAlert("Validation Error", "Category is required.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        if (cmbSize.getValue() == null || cmbSize.getValue().isEmpty()) {
            AlertUtil.showAlert("Validation Error", "Size is required.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        if (txtUnitCost.getText() == null || txtUnitCost.getText().isEmpty()) {
            AlertUtil.showAlert("Validation Error", "Unit Cost is required.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        if (txtQty.getText() == null || txtQty.getText().isEmpty()) {
            AlertUtil.showAlert("Validation Error", "Quantity is required.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        try {
            int qty = Integer.parseInt(txtQty.getText());
            if (qty <= 0) {
                AlertUtil.showAlert("Validation Error", "Quantity must be a positive integer.", null, javafx.scene.control.Alert.AlertType.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            AlertUtil.showAlert("Validation Error", "Quantity must be a valid integer.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        try {
            BigDecimal unitCost = new BigDecimal(txtUnitCost.getText());
            if (unitCost.compareTo(BigDecimal.ZERO) <= 0) {
                AlertUtil.showAlert("Validation Error", "Unit Cost must be a positive number.", null, javafx.scene.control.Alert.AlertType.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            AlertUtil.showAlert("Validation Error", "Unit Cost must be a valid number.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

}
