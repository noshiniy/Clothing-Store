package repository.custom.impl;

import entity.OrderEntity;
import repository.custom.OrderDao;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDaoImpl implements OrderDao {
    @Override
    public boolean add(OrderEntity entity) {
        String sql = "INSERT INTO orders (OrderID, NetTotal, TotalDiscount, OrderDateAndTime, StaffID, CustomerEmail, CustomerPhoneNumber) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            return CrudUtil.execute(sql, entity.getOrderId(), entity.getNetTotal(), entity.getTotalDiscount(),
                    entity.getDateAndTime(), entity.getStaffId(), entity.getCustomerEmail(), entity.getCustomerPhoneNumber());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(OrderEntity entity) {
        String sql = "UPDATE orders SET NetTotal = ?, TotalDiscount = ?, OrderDateAndTime = ?, StaffID = ?, CustomerEmail = ?, CustomerPhoneNumber = ? WHERE OrderID = ?";
        try {
            return CrudUtil.execute(sql, entity.getNetTotal(), entity.getTotalDiscount(), entity.getDateAndTime(),
                    entity.getStaffId(), entity.getCustomerEmail(), entity.getCustomerPhoneNumber(), entity.getOrderId());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM orders WHERE OrderID = ?";
        try {
            return CrudUtil.execute(sql, id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public OrderEntity findById(String id) {
        String sql = "SELECT * FROM orders WHERE OrderID = ?";
        try {
            ResultSet resultSet = CrudUtil.execute(sql, id);
            if (resultSet.next()) {
                return new OrderEntity(
                        resultSet.getString("OrderID"),
                        resultSet.getBigDecimal("NetTotal"),
                        resultSet.getBigDecimal("TotalDiscount"),
                        resultSet.getTimestamp("OrderDateAndTime").toLocalDateTime(),
                        resultSet.getString("StaffID"),
                        resultSet.getString("CustomerEmail"),
                        resultSet.getString("CustomerPhoneNumber")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<OrderEntity> findAll() {
        List<OrderEntity> orderList = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try {
            ResultSet resultSet = CrudUtil.execute(sql);
            while (resultSet.next()) {
                orderList.add(new OrderEntity(
                        resultSet.getString("OrderID"),
                        resultSet.getBigDecimal("NetTotal"),
                        resultSet.getBigDecimal("TotalDiscount"),
                        resultSet.getTimestamp("OrderDateAndTime").toLocalDateTime(),
                        resultSet.getString("StaffID"),
                        resultSet.getString("CustomerEmail"),
                        resultSet.getString("CustomerPhoneNumber")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    @Override
    public Optional<String> getLastOrderId() {
        String sql = "SELECT OrderID FROM orders ORDER BY OrderID DESC LIMIT 1";
        try {
            ResultSet resultSet = CrudUtil.execute(sql);
            if (resultSet.next()) {
                return Optional.of(resultSet.getString("OrderID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
