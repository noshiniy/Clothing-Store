package controller.supplier;

import com.jfoenix.controls.JFXTextField;
import controller.admin.AdminDashboardFormController;
import controller.staff.StaffDashboardFormController;
import entity.AdminEntity;
import entity.StaffEntity;
import entity.SupplierEntity;
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
import service.custom.SupplierService;
import util.AdminSession;
import util.AlertUtil;
import util.ServiceType;
import util.StaffSession;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SupplierManagementFormController implements Initializable {

    @FXML
    private TableColumn<SupplierEntity, String> colSupplierID;

    @FXML
    private TableColumn<SupplierEntity, String> colFullName;

    @FXML
    private TableColumn<SupplierEntity, String> colCompany;

    @FXML
    private TableColumn<SupplierEntity, String> colEmail;

    @FXML
    private TableColumn<SupplierEntity, String> colContact;

    @FXML
    private Label labelSupplierID;

    @FXML
    private TableView<SupplierEntity> tblSupplierTable;

    @FXML
    private JFXTextField txtCompany;

    @FXML
    private JFXTextField txtContact;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtFullName;

    @FXML
    private Text txtID;

    @FXML
    private TextField txtSearchField;

    private String role;

    private final SupplierService supplierService = ServiceFactory.getInstance().getServiceType(ServiceType.SUPPLIER_SERVICE);

    public void setRoleAndUser(String role, String id) {
        this.role = role;
        txtID.setText(id);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadSupplierTable();

        String newSupplierId = supplierService.generateSupplierId();
        labelSupplierID.setText(newSupplierId);
    }

    private void loadSupplierTable() {
        colSupplierID.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colFullName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCompany.setCellValueFactory(new PropertyValueFactory<>("company"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        List<SupplierEntity> supplierList = supplierService.findAllSuppliers();
        ObservableList<SupplierEntity> observableSupplierList = FXCollections.observableArrayList(supplierList);
        tblSupplierTable.setItems(observableSupplierList);
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (!validateFields()) return;

        String supplierId = labelSupplierID.getText();
        String name = txtFullName.getText();
        String company = txtCompany.getText();
        String email = txtEmail.getText();
        String contact = txtContact.getText();

        SupplierEntity newSupplier = new SupplierEntity(supplierId, name, company, email, contact);

        if (supplierService.addSupplier(newSupplier)) {
            AlertUtil.showAlert("Success", "Supplier added successfully!", null, javafx.scene.control.Alert.AlertType.INFORMATION);
            loadSupplierTable();
            clearTextFields();
        } else {
            AlertUtil.showAlert("Error", "Failed to add supplier. Please try again.", null, javafx.scene.control.Alert.AlertType.ERROR);
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        SupplierEntity selectedSupplier = tblSupplierTable.getSelectionModel().getSelectedItem();

        if (selectedSupplier != null) {
            if (supplierService.deleteSupplier(selectedSupplier.getSupplierId())) {
                AlertUtil.showAlert("Success", "Supplier deleted successfully!", null, javafx.scene.control.Alert.AlertType.INFORMATION);
                loadSupplierTable();
            } else {
                AlertUtil.showAlert("Error", "Failed to delete supplier. Please try again.", null, javafx.scene.control.Alert.AlertType.ERROR);
            }
        } else {
            AlertUtil.showAlert("Warning", "Please select a supplier to delete.", null, javafx.scene.control.Alert.AlertType.WARNING);
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String searchId = txtSearchField.getText().trim();
        if (searchId.isEmpty()) {
            AlertUtil.showAlert("Warning", "Please enter a Supplier ID to search.", null, javafx.scene.control.Alert.AlertType.WARNING);
            return;
        }

        SupplierEntity foundSupplier = supplierService.findSupplierById(searchId);

        if (foundSupplier != null) {
            labelSupplierID.setText(foundSupplier.getSupplierId());
            txtFullName.setText(foundSupplier.getName());
            txtCompany.setText(foundSupplier.getCompany());
            txtEmail.setText(foundSupplier.getEmail());
            txtContact.setText(foundSupplier.getPhoneNumber());
        } else {
            AlertUtil.showAlert("Not Found", "Supplier not found. Please check the ID and try again.", null, javafx.scene.control.Alert.AlertType.ERROR);
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validateFields()) return;

        String supplierId = labelSupplierID.getText();
        String name = txtFullName.getText();
        String company = txtCompany.getText();
        String email = txtEmail.getText();
        String contact = txtContact.getText();

        SupplierEntity updatedSupplier = new SupplierEntity(supplierId, name, company, email, contact);

        if (supplierService.updateSupplier(updatedSupplier)) {
            AlertUtil.showAlert("Success", "Supplier updated successfully!", null, javafx.scene.control.Alert.AlertType.INFORMATION);
            loadSupplierTable();
        } else {
            AlertUtil.showAlert("Error", "Failed to update supplier. Please try again.", null, javafx.scene.control.Alert.AlertType.ERROR);
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearTextFields();
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
    public void handleMouseClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            SupplierEntity selectedSupplier = tblSupplierTable.getSelectionModel().getSelectedItem();
            if (selectedSupplier != null) {
                populateFields(selectedSupplier);
            }
        }
    }

    private void populateFields(SupplierEntity supplier) {
        labelSupplierID.setText(supplier.getSupplierId());
        txtFullName.setText(supplier.getName());
        txtCompany.setText(supplier.getCompany());
        txtEmail.setText(supplier.getEmail());
        txtContact.setText(supplier.getPhoneNumber());
    }

    private void clearTextFields() {
        txtFullName.clear();
        txtCompany.clear();
        txtEmail.clear();
        txtContact.clear();
        txtSearchField.clear();

        String newSupplierId = supplierService.generateSupplierId();
        labelSupplierID.setText(newSupplierId);
    }

    private boolean validateFields() {
        if (txtFullName.getText().isEmpty()) {
            AlertUtil.showAlert("Validation Error", "Full Name cannot be empty.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        if (txtCompany.getText().isEmpty()) {
            AlertUtil.showAlert("Validation Error", "Company cannot be empty.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        String email = txtEmail.getText().trim();
        if (email.isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            AlertUtil.showAlert("Validation Error", "Please enter a valid email address.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        String contact = txtContact.getText().trim();
        if (contact.isEmpty() || !contact.matches("\\d{10,15}")) {
            AlertUtil.showAlert("Validation Error", "Contact Number must be a valid number with 10 to 15 digits.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

}
