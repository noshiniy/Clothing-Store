package controller.order;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.ServiceFactory;
import service.custom.OrderDetailService;
import service.custom.OrderService;
import service.custom.ProductService;
import service.custom.StaffService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class OrderManagementFormController implements Initializable {

    @FXML
    private JFXComboBox<String> cmbProductID;

    @FXML
    private JFXComboBox<String> cmbStaffID;

    @FXML
    private TableColumn<CartEntity, Void> colAction;

    @FXML
    private TableColumn<CartEntity, BigDecimal> colCartDiscount;

    @FXML
    private TableColumn<CartEntity, String> colCartProduct;

    @FXML
    private TableColumn<CartEntity, String> colCartProductID;

    @FXML
    private TableColumn<CartEntity, Integer> colCartQty;

    @FXML
    private TableColumn<CartEntity, String> colCartSize;

    @FXML
    private TableColumn<CartEntity, BigDecimal> colCartTotal;

    @FXML
    private TableColumn<CartEntity, BigDecimal> colCartUnitPrice;

    @FXML
    private TableView<OrderDetailEntity> tblOrderDetails;

    @FXML
    private TableColumn<OrderDetailEntity, BigDecimal> colOrderDetailsDiscount;

    @FXML
    private TableColumn<OrderDetailEntity, String> colOrderDetailsOrderID;

    @FXML
    private TableColumn<OrderDetailEntity, String> colOrderDetailsProduct;

    @FXML
    private TableColumn<OrderDetailEntity, String> colOrderDetailsProductID;

    @FXML
    private TableColumn<OrderDetailEntity, Integer> colOrderDetailsQty;

    @FXML
    private TableColumn<OrderDetailEntity, String> colOrderDetailsSize;

    @FXML
    private TableColumn<OrderDetailEntity, BigDecimal> colOrderDetailsTotal;

    @FXML
    private TableColumn<OrderDetailEntity, BigDecimal> colOrderDetailsUnitPrice;

    @FXML
    private Label labelOrderID;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblNetDiscount;

    @FXML
    private Label lblNetTotal;

    @FXML
    private Label lblTime;

    @FXML
    private TableView<CartEntity> tblCart;

    @FXML
    private JFXTextField txtCategory;

    @FXML
    private JFXTextField txtCustomerContact;

    @FXML
    private JFXTextField txtCustomerEmail;

    @FXML
    private JFXTextField txtDiscount;

    @FXML
    private Text txtID;

    @FXML
    private JFXTextField txtProduct;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private TextField txtSearchField;

    @FXML
    private JFXTextField txtSize;

    @FXML
    private JFXTextField txtTotal;

    @FXML
    private JFXTextField txtUnitPrice;

    @FXML
    private JFXButton btnSearch;

    private String role;

    private final OrderService orderService = ServiceFactory.getInstance().getServiceType(ServiceType.ORDER_SERVICE);
    private final OrderDetailService orderDetailService = ServiceFactory.getInstance().getServiceType(ServiceType.ORDER_DETAIL_SERVICE);
    private final ProductService productService = ServiceFactory.getInstance().getServiceType(ServiceType.PRODUCT_SERVICE);
    private final StaffService staffService = ServiceFactory.getInstance().getServiceType(ServiceType.STAFF_SERVICE);

    private ObservableList<CartEntity> cartItems = FXCollections.observableArrayList();

    public void setRoleAndUser(String role, String id) {
        this.role = role;
        txtID.setText(id);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDateAndTime();

        String initialOrderId = orderService.generateOrderId();
        labelOrderID.setText(initialOrderId);

        loadStaffIds();
        loadProductIds();

        cmbProductID.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadProductDetails(newValue);
            }
        });

        initializeCart();
        initializeListeners();
        initializeOrderDetailTable();

        txtDiscount.setText("0");
        lblNetDiscount.setText("0");
        lblNetTotal.setText("0");

        btnSearch.setVisible(false);
        txtSearchField.setVisible(false);

        tblOrderDetails.setOnMouseClicked(this::handleMouseClick);

    }

    private void initializeOrderDetailTable() {
        colOrderDetailsOrderID.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getId().getOrderId()));

        colOrderDetailsProductID.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getId().getProductId()));

        colOrderDetailsProduct.setCellValueFactory(cellData -> {
            String productId = cellData.getValue().getId().getProductId();
            ProductEntity product = productService.findProductById(productId);
            return new SimpleStringProperty(product != null ? product.getName() : "Unknown");
        });

        colOrderDetailsSize.setCellValueFactory(cellData -> {
            String productId = cellData.getValue().getId().getProductId();
            ProductEntity product = productService.findProductById(productId);
            return new SimpleStringProperty(product != null ? product.getSize() : "Unknown");
        });

        colOrderDetailsUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colOrderDetailsQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colOrderDetailsDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colOrderDetailsTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        loadOrderDetails();
    }

    private void loadOrderDetails() {
        List<OrderDetailEntity> orderDetailEntities = orderDetailService.findAllOrderDetails();

        ObservableList<OrderDetailEntity> orderDetailList = FXCollections.observableArrayList(orderDetailEntities);
        tblOrderDetails.setItems(orderDetailList);
    }

    private void initializeListeners() {
        txtUnitPrice.textProperty().addListener((observable, oldValue, newValue) -> calculateAndSetTotal());
        txtQty.textProperty().addListener((observable, oldValue, newValue) -> calculateAndSetTotal());
        txtDiscount.textProperty().addListener((observable, oldValue, newValue) -> calculateAndSetTotal());
    }

    private void initializeCart() {
        colCartProductID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colCartProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colCartSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colCartUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colCartQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colCartDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colCartTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        colAction.setCellFactory(param -> new TableCell<CartEntity, Void>() {
            private final JFXButton btnEdit = new JFXButton("Edit");
            private final JFXButton btnDelete = new JFXButton("Delete");

            {
                btnEdit.setStyle(
                        "-fx-background-color: #4CAF50; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-border-radius: 5px; " +
                                "-fx-background-radius: 5px; " +
                                "-fx-font-size: 10px;"
                );

                btnDelete.setStyle(
                        "-fx-background-color: #f44336; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-border-radius: 5px; " +
                                "-fx-background-radius: 5px; " +
                                "-fx-font-size: 10px;"
                );

                btnEdit.setOnAction(event -> {
                    CartEntity cartItem = getTableView().getItems().get(getIndex());
                    if (cartItem != null) {
                        cmbProductID.setValue(cartItem.getProductId());
                        txtProduct.setText(cartItem.getProductName());
                        txtSize.setText(cartItem.getSize());
                        txtUnitPrice.setText(cartItem.getUnitPrice().toString());
                        txtQty.setText(String.valueOf(cartItem.getQuantity()));
                        txtDiscount.setText(cartItem.getDiscount().toString());
                        txtTotal.setText(cartItem.getTotal().toString());

                        getTableView().getItems().remove(cartItem);
                        tblCart.refresh();
                        calculateNetDiscountAndTotal();
                    }
                });

                btnDelete.setOnAction(event -> {
                    CartEntity cartItem = getTableView().getItems().get(getIndex());
                    if (cartItem != null) {
                        getTableView().getItems().remove(cartItem);
                        tblCart.refresh();
                        calculateNetDiscountAndTotal();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox actionButtons = new HBox(5);
                    actionButtons.getChildren().addAll(btnEdit, btnDelete);
                    setGraphic(actionButtons);
                }
            }
        });

        tblCart.setItems(cartItems);
    }

    private void loadProductDetails(String productId) {
        ProductEntity product = productService.findProductById(productId);

        if (product != null) {
            txtProduct.setText(product.getName());
            txtCategory.setText(product.getCategory());
            txtSize.setText(product.getSize());
            txtUnitPrice.setText(String.valueOf(product.getUnitPrice()));
        } else {
            txtProduct.clear();
            txtUnitPrice.clear();
            txtCategory.clear();
            txtSize.clear();
        }
    }

    private void loadProductIds() {
        List<ProductEntity> productList = productService.findAllProducts();
        ObservableList<String> productIds = FXCollections.observableArrayList();

        for (ProductEntity product : productList) {
            productIds.add(product.getProductId());
        }

        cmbProductID.setItems(productIds);
    }

    private void loadStaffIds() {
        List<StaffEntity> staffList = staffService.getAllStaffMembers();
        ObservableList<String> staffIds = FXCollections.observableArrayList();

        for (StaffEntity staff : staffList) {
            staffIds.add(staff.getStaffId());
        }

        cmbStaffID.setItems(staffIds);
    }

    private void calculateAndSetTotal() {
        try {
            BigDecimal unitPrice = new BigDecimal(txtUnitPrice.getText());
            int quantity = Integer.parseInt(txtQty.getText());
            BigDecimal discount = new BigDecimal(txtDiscount.getText());

            BigDecimal total = unitPrice.multiply(BigDecimal.valueOf(quantity))
                    .subtract(BigDecimal.valueOf(quantity).multiply(discount));

            txtTotal.setText(total.toString());
        } catch (NumberFormatException e) {
            txtTotal.clear();
        }
    }


    @FXML
    void btnAddCartOnAction(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        String productId = cmbProductID.getValue();
        String productName = txtProduct.getText();
        String size = txtSize.getText();
        BigDecimal unitPrice = new BigDecimal(txtUnitPrice.getText());
        int quantity = Integer.parseInt(txtQty.getText());
        BigDecimal discount = new BigDecimal(txtDiscount.getText());
        BigDecimal total = unitPrice.multiply(BigDecimal.valueOf(quantity))
                .subtract(BigDecimal.valueOf(quantity).multiply(discount));

        CartEntity cartItem = new CartEntity(productId, productName, size, unitPrice, quantity, discount, total);
        cartItems.add(cartItem);

        tblCart.setItems(cartItems);
        tblCart.refresh();
        clearForm();

        calculateNetDiscountAndTotal();
    }

    private void calculateNetDiscountAndTotal() {
        BigDecimal netDiscount = BigDecimal.ZERO;
        BigDecimal netTotal = BigDecimal.ZERO;

        for (CartEntity item : cartItems) {
            BigDecimal discountAmount = item.getDiscount().multiply(BigDecimal.valueOf(item.getQuantity()));
            netDiscount = netDiscount.add(discountAmount);
            netTotal = netTotal.add(item.getTotal());
        }

        lblNetDiscount.setText(netDiscount.toString());
        lblNetTotal.setText(netTotal.toString());
    }

    private void clearForm() {
        cmbProductID.setValue(null);
        txtProduct.clear();
        txtCategory.clear();
        txtSize.clear();
        txtUnitPrice.clear();
        txtQty.clear();
        txtDiscount.setText("0");
        txtTotal.clear();
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        String orderId = labelOrderID.getText();
        String staffId = cmbStaffID.getValue();
        String customerEmail = txtCustomerEmail.getText();
        String customerContact = txtCustomerContact.getText();

        BigDecimal netTotal = calculateNetTotal();
        BigDecimal totalDiscount = calculateTotalDiscount();

        List<OrderDetailEntity> orderDetails = new ArrayList<>();
        for (CartEntity cartItem : cartItems) {
            OrderDetailEntity orderDetail = new OrderDetailEntity(
                    new OrderDetailId(orderId, cartItem.getProductId()),
                    cartItem.getUnitPrice(),
                    cartItem.getQuantity(),
                    cartItem.getDiscount(),
                    cartItem.getTotal()
            );
            orderDetails.add(orderDetail);
        }

        OrderEntity order = new OrderEntity(orderId, netTotal, totalDiscount, LocalDateTime.now(), staffId, customerEmail, customerContact);
        order.setOrderDetails(orderDetails);

        boolean isOrderAdded = orderService.addOrder(order);
        if (isOrderAdded) {
            cartItems.clear();
            tblCart.refresh();
            clearForm();
            lblNetDiscount.setText("0");
            lblNetTotal.setText("0");

            String newOrderId = orderService.generateOrderId();
            labelOrderID.setText(newOrderId);

            AlertUtil.showAlert("Success", "Order has been placed successfully.", null, javafx.scene.control.Alert.AlertType.INFORMATION);
        } else {
            AlertUtil.showAlert("Error", "Failed to place the order. Please try again.", null, javafx.scene.control.Alert.AlertType.ERROR);
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        OrderDetailEntity selectedOrderDetail = tblOrderDetails.getSelectionModel().getSelectedItem();

        if (selectedOrderDetail != null) {
            tblOrderDetails.getItems().remove(selectedOrderDetail);
            tblOrderDetails.refresh();

            AlertUtil.showAlert("Success", "Order detail deleted successfully.", null, javafx.scene.control.Alert.AlertType.INFORMATION);

            calculateNetDiscountAndTotal();
        } else {
            AlertUtil.showAlert("Error", "Please select an order detail to delete.", null, javafx.scene.control.Alert.AlertType.WARNING);
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        OrderDetailEntity selectedOrderDetail = tblOrderDetails.getSelectionModel().getSelectedItem();

        if (selectedOrderDetail != null) {
            selectedOrderDetail.setQty(Integer.parseInt(txtQty.getText()));
            selectedOrderDetail.setDiscount(new BigDecimal(txtDiscount.getText()));

            BigDecimal unitPrice = selectedOrderDetail.getUnitPrice();
            BigDecimal discount = selectedOrderDetail.getDiscount();
            BigDecimal total = unitPrice.multiply(BigDecimal.valueOf(selectedOrderDetail.getQty()))
                    .subtract(BigDecimal.valueOf(selectedOrderDetail.getQty()).multiply(discount));
            selectedOrderDetail.setTotal(total);

            tblOrderDetails.refresh();

            AlertUtil.showAlert("Success", "Order detail updated successfully.", null, javafx.scene.control.Alert.AlertType.INFORMATION);

            calculateNetDiscountAndTotal();
        } else {
            AlertUtil.showAlert("Error", "Please select an order detail to update.", null, javafx.scene.control.Alert.AlertType.WARNING);
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
        clearForm();
        txtCustomerContact.clear();
        txtCustomerEmail.clear();
        tblCart.refresh();
        lblNetDiscount.setText("0");
        lblNetTotal.setText("0");

        String initialOrderId = orderService.generateOrderId();
        labelOrderID.setText(initialOrderId);
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {

    }

    private BigDecimal calculateNetTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartEntity cartItem : cartItems) {
            total = total.add(cartItem.getTotal());
        }
        return total;
    }

    private BigDecimal calculateTotalDiscount() {
        BigDecimal discount = BigDecimal.ZERO;
        for (CartEntity cartItem : cartItems) {
            discount = discount.add(cartItem.getDiscount());
        }
        return discount;
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

    public void handleMouseClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            OrderDetailEntity selectedOrderDetail = tblOrderDetails.getSelectionModel().getSelectedItem();

            if (selectedOrderDetail != null) {

                labelOrderID.setText(selectedOrderDetail.getId().getOrderId());
                cmbProductID.setValue(selectedOrderDetail.getId().getProductId());

                ProductEntity product = productService.findProductById(selectedOrderDetail.getId().getProductId());
                if (product != null) {
                    txtProduct.setText(product.getName());
                    txtCategory.setText(product.getCategory());
                    txtSize.setText(product.getSize());
                }

                txtUnitPrice.setText(selectedOrderDetail.getUnitPrice().toString());
                txtQty.setText(selectedOrderDetail.getQty().toString());
                txtDiscount.setText(selectedOrderDetail.getDiscount().toString());
                txtTotal.setText(selectedOrderDetail.getTotal().toString());

                OrderEntity order = orderService.findOrderById(selectedOrderDetail.getId().getOrderId());
                if (order != null) {
                    cmbStaffID.setValue(order.getStaffId());
                    txtCustomerEmail.setText(order.getCustomerEmail());
                    txtCustomerContact.setText(order.getCustomerPhoneNumber());
                }
            }
        }
    }

    private boolean validateFields() {
        if (cmbProductID.getValue() == null || cmbProductID.getValue().isEmpty()) {
            AlertUtil.showAlert("Validation Error", "Product ID is required.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        if (txtProduct.getText() == null || txtProduct.getText().isEmpty()) {
            AlertUtil.showAlert("Validation Error", "Product Name is required.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        if (txtSize.getText() == null || txtSize.getText().isEmpty()) {
            AlertUtil.showAlert("Validation Error", "Size is required.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        if (txtUnitPrice.getText() == null || txtUnitPrice.getText().isEmpty()) {
            AlertUtil.showAlert("Validation Error", "Unit Price is required.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        if (txtQty.getText() == null || txtQty.getText().isEmpty()) {
            AlertUtil.showAlert("Validation Error", "Quantity is required.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        if (txtDiscount.getText() == null || txtDiscount.getText().isEmpty()) {
            AlertUtil.showAlert("Validation Error", "Discount is required.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        try {
            BigDecimal unitPrice = new BigDecimal(txtUnitPrice.getText());
            if (unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
                AlertUtil.showAlert("Validation Error", "Unit Price must be a positive number.", null, javafx.scene.control.Alert.AlertType.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            AlertUtil.showAlert("Validation Error", "Unit Price must be a valid number.", null, javafx.scene.control.Alert.AlertType.ERROR);
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
            BigDecimal discount = new BigDecimal(txtDiscount.getText());
            if (discount.compareTo(BigDecimal.ZERO) < 0) {
                AlertUtil.showAlert("Validation Error", "Discount cannot be negative.", null, javafx.scene.control.Alert.AlertType.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            AlertUtil.showAlert("Validation Error", "Discount must be a valid number.", null, javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

}
