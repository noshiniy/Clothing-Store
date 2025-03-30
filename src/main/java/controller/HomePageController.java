package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePageController {

    @FXML
    void btnAdminOnAction(ActionEvent event) {
        loadLoginPage(event, "Admin");
    }

    @FXML
    void btnStaffOnAction(ActionEvent event) {
        loadLoginPage(event, "Staff");
    }

    private void loadLoginPage(ActionEvent event, String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/login_page.fxml"));
            Parent root = loader.load();

            LoginPageController loginPageController = loader.getController();
            loginPageController.setRole(role);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Login Page - " + role);
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
