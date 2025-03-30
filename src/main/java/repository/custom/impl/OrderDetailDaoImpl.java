package repository.custom.impl;

import entity.OrderDetailEntity;
import entity.OrderDetailId;
import repository.custom.OrderDetailDao;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDaoImpl implements OrderDetailDao {

    @Override
    public boolean add(OrderDetailEntity entity) {
        String sql = "INSERT INTO orderdetail (OrderID, ProductID, UnitPrice, Qty, Discount, Total) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            return CrudUtil.execute(sql,
                    entity.getId().getOrderId(),
                    entity.getId().getProductId(),
                    entity.getUnitPrice(),
                    entity.getQty(),
                    entity.getDiscount(),
                    entity.getTotal()
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(OrderDetailEntity entity) {
        String sql = "UPDATE orderdetail SET UnitPrice = ?, Qty = ?, Discount = ?, Total = ? WHERE OrderID = ? AND ProductID = ?";
        try {
            return CrudUtil.execute(sql,
                    entity.getUnitPrice(),
                    entity.getQty(),
                    entity.getDiscount(),
                    entity.getTotal(),
                    entity.getId().getOrderId(),
                    entity.getId().getProductId()
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(OrderDetailId id) {
        String sql = "DELETE FROM orderdetail WHERE OrderID = ? AND ProductID = ?";
        try {
            return CrudUtil.execute(sql, id.getOrderId(), id.getProductId());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public OrderDetailEntity findById(OrderDetailId id) {
        String sql = "SELECT * FROM orderdetail WHERE OrderID = ? AND ProductID = ?";
        try {
            ResultSet resultSet = CrudUtil.execute(sql, id.getOrderId(), id.getProductId());
            if (resultSet.next()) {
                return new OrderDetailEntity(
                        id,
                        resultSet.getBigDecimal("UnitPrice"),
                        resultSet.getInt("Qty"),
                        resultSet.getBigDecimal("Discount"),
                        resultSet.getBigDecimal("Total"),
                        null
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<OrderDetailEntity> findAll() {
        List<OrderDetailEntity> orderDetails = new ArrayList<>();
        String sql = "SELECT * FROM orderdetail";
        try {
            ResultSet resultSet = CrudUtil.execute(sql);
            while (resultSet.next()) {
                OrderDetailId id = new OrderDetailId(
                        resultSet.getString("OrderID"),
                        resultSet.getString("ProductID")
                );
                orderDetails.add(new OrderDetailEntity(
                        id,
                        resultSet.getBigDecimal("UnitPrice"),
                        resultSet.getInt("Qty"),
                        resultSet.getBigDecimal("Discount"),
                        resultSet.getBigDecimal("Total"),
                        null
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderDetails;
    }

    @Override
    public OrderDetailEntity findById(String id) {
        throw new UnsupportedOperationException("This method should not be used. Use delete(OrderDetailId id) instead.");
    }

    @Override
    public boolean delete(String id) {
        throw new UnsupportedOperationException("This method should not be used. Use findById(OrderDetailId id) instead.");
    }
}
