package controller.admin;

import com.jfoenix.controls.JFXTextField;
import entity.AdminEntity;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.StaffService;
import util.AdminSession;
import util.AlertUtil;
import util.ServiceType;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class StaffManagementFormController implements Initializable {

    @FXML
    private TableColumn<StaffEntity, String> colAddress;
    @FXML
    private TableColumn<StaffEntity, String> colContact;
    @FXML
    private TableColumn<StaffEntity, LocalDate> colDOB;
    @FXML
    private TableColumn<StaffEntity, String> colEmail;
    @FXML
    private TableColumn<StaffEntity, String> colFullName;
    @FXML
    private TableColumn<StaffEntity, String> colNIC;
    @FXML
    private TableColumn<StaffEntity, Double> colSalary;
    @FXML
    private TableColumn<StaffEntity, String> colStaffID;

    @FXML
    private DatePicker dateDOB;

    @FXML
    private Label labelStaffID;

    @FXML
    private TableView<StaffEntity> tblStaffTable;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtContact;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtFullName;

    @FXML
    private Text txtID;

    @FXML
    private JFXTextField txtNIC;

    @FXML
    private JFXTextField txtSalary;

    @FXML
    private TextField txtSearchField;

    private final StaffService staffService = ServiceFactory.getInstance().getServiceType(ServiceType.STAFF_SERVICE);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String newStaffId = staffService.generateNewStaffId();
        labelStaffID.setText(newStaffId);
        loadStaffTable();
    }

    public void setLoggedAdminId(String adminId) {
        txtID.setText(adminId);
    }

    private void loadStaffTable() {
        colStaffID.setCellValueFactory(new PropertyValueFactory<>("staffId"));
        colFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("username"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));

        List<StaffEntity> staffList = staffService.getAllStaffMembers();
        ObservableList<StaffEntity> observableStaffList = FXCollections.observableArrayList(staffList);
        tblStaffTable.setItems(observableStaffList);
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (!validateFields()) return;

        String staffId = labelStaffID.getText();
        String email = txtEmail.getText();
        String password = txtNIC.getText();
        String fullName = txtFullName.getText();
        String address = txtAddress.getText();
        String contact = txtContact.getText();
        String nic = txtNIC.getText();
        LocalDate dob = dateDOB.getValue();
        Double salary = Double.parseDouble(txtSalary.getText());

        boolean isAdded = staffService.addStaff(staffId, email, password, fullName, address, contact, nic, dob, salary);

        if (isAdded) {
            AlertUtil.showAlert("Success", "Staff member added successfully", null, javafx.scene.control.Alert.AlertType.INFORMATION);
            clearTextFields();
            loadStaffTable();
        } else {
            AlertUtil.showAlert("Error", "Failed to add staff member", null, javafx.scene.control.Alert.AlertType.ERROR);
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String staffId = labelStaffID.getText();
        boolean isDeleted = staffService.deleteStaff(staffId);

        if (isDeleted) {
            AlertUtil.showAlert("Success", "Staff member deleted successfully", null, javafx.scene.control.Alert.AlertType.INFORMATION);
            clearTextFields();
            loadStaffTable();
        } else {
            AlertUtil.showAlert("Error", "Failed to delete staff member", null, javafx.scene.control.Alert.AlertType.ERROR);
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String staffId = txtSearchField.getText().trim();
        if (staffId.isEmpty()) {
            AlertUtil.showAlert("Validation Error", "Please enter a Staff ID to search.", null, javafx.scene.control.Alert.AlertType.WARNING);
            return;
        }

        StaffEntity staff = staffService.findStaffById(staffId);

        if (staff != null) {
            labelStaffID.setText(staff.getStaffId());
            txtFullName.setText(staff.getFullName());
            txtEmail.setText(staff.getUsername());
            txtAddress.setText(staff.getAddress());
            txtNIC.setText(staff.getNic());
            txtContact.setText(staff.getPhoneNumber());
            txtSalary.setText(staff.getSalary().toString());
            dateDOB.setValue(staff.getDob());
        } else {
            AlertUtil.showAlert("Error", "Staff member not found", null, javafx.scene.control.Alert.AlertType.ERROR);
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validateFields()) return;

        String staffId = labelStaffID.getText();
        String fullName = txtFullName.getText();
        String email = txtEmail.getText();
        String address = txtAddress.getText();
        String contact = txtContact.getText();
        String nic = txtNIC.getText();
        LocalDate dob = dateDOB.getValue();
        Double salary = Double.parseDouble(txtSalary.getText());

        boolean isUpdated = staffService.updateStaff(staffId, email, null, fullName, address, contact, nic, dob, salary);

        if (isUpdated) {
            AlertUtil.showAlert("Success", "Staff member updated successfully", null, javafx.scene.control.Alert.AlertType.INFORMATION);
            clearTextFields();
            loadStaffTable();
        } else {
            AlertUtil.showAlert("Error", "Failed to update staff member", null, javafx.scene.control.Alert.AlertType.ERROR);
        }
    }

    @FXML
    void btnDashboardOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/admin_dashboard_form.fxml"));
            Parent root = loader.load();

            AdminDashboardFormController adminDashboardFormController = loader.getController();
            AdminEntity admin = AdminSession.getInstance().getAdmin();
            adminDashboardFormController.setAdminUsername(admin.getUsername());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Manage Account - Admin");
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
        txtSearchField.clear();
        txtFullName.clear();
        txtEmail.clear();
        txtAddress.clear();
        txtNIC.clear();
        txtContact.clear();
        txtSalary.clear();
        dateDOB.setValue(null);

        String newStaffId = staffService.generateNewStaffId();
        labelStaffID.setText(newStaffId);
    }

    public void handleMouseClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            StaffEntity selectedStaff = tblStaffTable.getSelectionModel().getSelectedItem();
            if (selectedStaff != null) {
                populateFields(selectedStaff);
            }
        }
    }

    private void populateFields(StaffEntity staff) {
        labelStaffID.setText(staff.getStaffId());
        txtFullName.setText(staff.getFullName());
        txtEmail.setText(staff.getUsername());
        txtAddress.setText(staff.getAddress());
        txtNIC.setText(staff.getNic());
        txtContact.setText(staff.getPhoneNumber());
        txtSalary.setText(staff.getSalary().toString());
        dateDOB.setValue(staff.getDob());
    }

    private boolean validateFields() {
        String email = txtEmail.getText();
        String contact = txtContact.getText();
        String salaryText = txtSalary.getText();
        String nic = txtNIC.getText();
        LocalDate dob = dateDOB.getValue();

        String contactRegex = "^07[0-9]{8}$";
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String nicRegex = "^[0-9]{9}[vV]$|^[0-9]{12}$";

        if (txtFullName.getText().isEmpty() || email.isEmpty() || txtAddress.getText().isEmpty() ||
                nic.isEmpty() || contact.isEmpty() || salaryText.isEmpty() || dob == null) {
            AlertUtil.showAlert("Validation Error", "Please fill in all fields before proceeding.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        if (!email.matches(emailRegex)) {
            AlertUtil.showAlert("Validation Error", "Please enter a valid email address.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        if (!nic.matches(nicRegex)) {
            AlertUtil.showAlert("Validation Error", "NIC must be 9 digits with 'v' or 12 digits.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        if (!contact.matches(contactRegex)) {
            AlertUtil.showAlert("Validation Error", "Contact number must start with '07' and be 10 digits long.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        if (dob.isAfter(LocalDate.now())) {
            AlertUtil.showAlert("Validation Error", "Date of Birth cannot be in the future.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        try {
            double salary = Double.parseDouble(salaryText);
            if (salary < 0) {
                AlertUtil.showAlert("Validation Error", "Salary must be a non-negative number.", null, javafx.scene.control.Alert.AlertType.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            AlertUtil.showAlert("Validation Error", "Salary must be a valid number.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

}
