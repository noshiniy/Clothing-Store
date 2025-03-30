package repository.custom.impl;

import entity.SupplyEntity;
import entity.SupplyId;
import repository.custom.SupplyDao;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SupplyDaoImpl implements SupplyDao {

    @Override
    public boolean add(SupplyEntity entity) {
        String sql = "INSERT INTO supply (SupplyID, ProductID, SupplierID, UnitCost, Qty, Total, DateAndTime) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            return CrudUtil.execute(sql, entity.getId().getSupplyId(), entity.getId().getProductId(), entity.getId().getSupplierId(),
                    entity.getUnitCost(), entity.getQty(), entity.getTotal(), entity.getDateAndTime());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(SupplyEntity entity) {
        String sql = "UPDATE supply SET UnitCost = ?, Qty = ?, Total = ?, DateAndTime = ? WHERE SupplyID = ? AND ProductID = ? AND SupplierID = ?";
        try {
            return CrudUtil.execute(sql, entity.getUnitCost(), entity.getQty(), entity.getTotal(), entity.getDateAndTime(),
                    entity.getId().getSupplyId(), entity.getId().getProductId(), entity.getId().getSupplierId());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM supply WHERE SupplyID = ?";
        try {
            return CrudUtil.execute(sql, id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public SupplyEntity findById(String id) {
        String sql = "SELECT * FROM supply WHERE SupplyID = ?";
        try {
            ResultSet resultSet = CrudUtil.execute(sql, id);
            if (resultSet.next()) {
                return new SupplyEntity(
                        new SupplyId(
                                resultSet.getString("SupplyID"),
                                resultSet.getString("ProductID"),
                                resultSet.getString("SupplierID")
                        ),
                        resultSet.getBigDecimal("UnitCost"),
                        resultSet.getInt("Qty"),
                        resultSet.getBigDecimal("Total"),
                        resultSet.getTimestamp("DateAndTime").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SupplyEntity> findAll() {
        List<SupplyEntity> supplyList = new ArrayList<>();
        String sql = "SELECT * FROM supply";
        try {
            ResultSet resultSet = CrudUtil.execute(sql);
            while (resultSet.next()) {
                supplyList.add(new SupplyEntity(
                        new SupplyId(
                                resultSet.getString("SupplyID"),
                                resultSet.getString("ProductID"),
                                resultSet.getString("SupplierID")
                        ),
                        resultSet.getBigDecimal("UnitCost"),
                        resultSet.getInt("Qty"),
                        resultSet.getBigDecimal("Total"),
                        resultSet.getTimestamp("DateAndTime").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supplyList;
    }

    @Override
    public Optional<String> getLastSupplyId() {
        String sql = "SELECT SupplyID FROM supply ORDER BY SupplyID DESC LIMIT 1";
        try {
            ResultSet resultSet = CrudUtil.execute(sql);
            if (resultSet.next()) {
                return Optional.of(resultSet.getString("SupplyID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
