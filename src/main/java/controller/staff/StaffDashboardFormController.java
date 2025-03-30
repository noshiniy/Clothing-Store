package controller.staff;

import controller.order.OrderManagementFormController;
import controller.product.ProductManagementFormController;
import controller.supplier.SupplierManagementFormController;
import controller.supply.SupplyManagementFormController;
import entity.StaffEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import util.StaffSession;

import java.io.IOException;

public class StaffDashboardFormController {

    @FXML
    private Text txtStaffUsername;

    public void setStaffUsername(String username) {
        txtStaffUsername.setText(username);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/staff_manage_account_form.fxml"));
            Parent root = loader.load();

            StaffManageAccountFormController controller = loader.getController();
            StaffEntity staff = StaffSession.getInstance().getStaff();
            controller.setStaffDetails(staff.getStaffId(), staff.getUsername(), staff.getPhoneNumber(), staff.getPassword());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Manage Account - Staff");
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
            StaffEntity staff = StaffSession.getInstance().getStaff();
            controller.setRoleAndUser("Staff", staff.getStaffId());

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
            StaffEntity staff = StaffSession.getInstance().getStaff();
            controller.setRoleAndUser("Staff", staff.getStaffId());

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
    void btnSupplierManagementOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/supplier_management_form.fxml"));
            Parent root = loader.load();

            SupplierManagementFormController controller = loader.getController();
            StaffEntity staff = StaffSession.getInstance().getStaff();
            controller.setRoleAndUser("Staff", staff.getStaffId());

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
            StaffEntity staff = StaffSession.getInstance().getStaff();
            controller.setRoleAndUser("Staff", staff.getStaffId());

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

}
