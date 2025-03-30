package controller.admin;

import com.jfoenix.controls.JFXButton;
import controller.order.OrderManagementFormController;
import controller.product.ProductManagementFormController;
import controller.supplier.SupplierManagementFormController;
import controller.supply.SupplyManagementFormController;
import entity.AdminEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import util.AdminSession;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminDashboardFormController implements Initializable {

    @FXML
    private JFXButton btnReports;

    @FXML
    private Text txtAdminUsername;

    public void setAdminUsername(String username) {
        txtAdminUsername.setText(username);
    }

    @FXML
    void btnHomepageOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/home_page.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Home Page");
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
    void btnManageAccountOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/admin_manage_account_form.fxml"));
            Parent root = loader.load();

            AdminManageAccountFormController controller = loader.getController();
            AdminEntity admin = AdminSession.getInstance().getAdmin();
            controller.setAdminDetails(admin.getAdminId(), admin.getUsername(), admin.getPhoneNumber(), admin.getPassword());

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
    void btnOrderManagementOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/order_management_form.fxml"));
            Parent root = loader.load();

            OrderManagementFormController controller = loader.getController();
            AdminEntity admin = AdminSession.getInstance().getAdmin();
            controller.setRoleAndUser("Admin", admin.getAdminId());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Order Management");
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
    void btnProductManagementOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/product_management_form.fxml"));
            Parent root = loader.load();

            ProductManagementFormController controller = loader.getController();
            AdminEntity admin = AdminSession.getInstance().getAdmin();
            controller.setRoleAndUser("Admin", admin.getAdminId());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Product Management");
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
    void btnStaffManagementOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/staff_management_form.fxml"));
            Parent root = loader.load();

            StaffManagementFormController staffController = loader.getController();
            AdminEntity admin = AdminSession.getInstance().getAdmin();
            staffController.setLoggedAdminId(admin.getAdminId());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Staff Management");
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
    void btnSupplierManagementOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/supplier_management_form.fxml"));
            Parent root = loader.load();

            SupplierManagementFormController controller = loader.getController();
            AdminEntity admin = AdminSession.getInstance().getAdmin();
            controller.setRoleAndUser("Admin", admin.getAdminId());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Supplier Management");
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
    void btnSupplyManagementOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/supply_management_form.fxml"));
            Parent root = loader.load();

            SupplyManagementFormController controller = loader.getController();
            AdminEntity admin = AdminSession.getInstance().getAdmin();
            controller.setRoleAndUser("Admin", admin.getAdminId());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Supply Management");
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
    void btnReportOnAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnReports.setVisible(false);
    }
}
