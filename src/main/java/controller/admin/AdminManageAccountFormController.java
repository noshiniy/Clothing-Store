package controller.admin;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import entity.AdminEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.AdminService;
import util.AdminSession;
import util.AlertUtil;
import util.ServiceType;

import java.io.IOException;

public class AdminManageAccountFormController {

    @FXML
    private Text txtAdminID;

    @FXML
    private JFXTextField txtContactNumber;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXPasswordField txtRePassword;

    @FXML
    private JFXTextField txtUsername;

    private final AdminService adminService = ServiceFactory.getInstance().getServiceType(ServiceType.ADMIN_SERVICE);

    public void setAdminDetails(String adminId, String username, String contactNumber, String password) {
        txtAdminID.setText(adminId);
        txtUsername.setText(username);
        txtContactNumber.setText(contactNumber);
        txtPassword.setText(password);
        txtRePassword.clear();
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
    void btnUpdateOnAction(ActionEvent event) {
        if (txtUsername.getText().isEmpty() || txtContactNumber.getText().isEmpty() ||
                txtPassword.getText().isEmpty() || txtRePassword.getText().isEmpty()) {
            AlertUtil.showAlert("Validation Error", "All fields must be filled out.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return;
        }

        if (!txtPassword.getText().equals(txtRePassword.getText())) {
            AlertUtil.showAlert("Password Mismatch", "Passwords do not match.", "Please ensure both password fields match.", javafx.scene.control.Alert.AlertType.ERROR);
            return;
        }

        String contactNumber = txtContactNumber.getText();
        if (!contactNumber.matches("^(\\+94|0)?7\\d{9}$")) {
            AlertUtil.showAlert("Validation Error", "Contact number must be a valid Sri Lankan mobile number.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return;
        }

        String adminId = txtAdminID.getText();
        String username = txtUsername.getText();

        String password = txtPassword.getText();

        boolean isUpdated = adminService.updateAdminDetails(adminId, username, password, contactNumber);

        if (isUpdated) {
            AdminEntity updatedAdmin = new AdminEntity(adminId, username, password, contactNumber);
            AdminSession.getInstance().setAdmin(updatedAdmin);

            AlertUtil.showAlert("Success", "Admin details updated successfully", null, javafx.scene.control.Alert.AlertType.INFORMATION);

            btnDashboardOnAction(event);
        } else {
            AlertUtil.showAlert("Update Failed", "Could not update admin details", null, javafx.scene.control.Alert.AlertType.ERROR);
        }
    }

}
