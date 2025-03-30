package repository.custom.impl;

import entity.SupplierEntity;
import repository.custom.SupplierDao;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SupplierDaoImpl implements SupplierDao {

    @Override
    public boolean add(SupplierEntity supplier) {
        try {
            return CrudUtil.execute("INSERT INTO Supplier (SupplierID, Name, Company, Email, PhoneNumber) VALUES (?, ?, ?, ?, ?)",
                    supplier.getSupplierId(), supplier.getName(), supplier.getCompany(), supplier.getEmail(), supplier.getPhoneNumber());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(SupplierEntity supplier) {
        try {
            return CrudUtil.execute("UPDATE Supplier SET Name = ?, Company = ?, Email = ?, PhoneNumber = ? WHERE SupplierID = ?",
                    supplier.getName(), supplier.getCompany(), supplier.getEmail(), supplier.getPhoneNumber(), supplier.getSupplierId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String supplierId) {
        try {
            return CrudUtil.execute("DELETE FROM Supplier WHERE SupplierID = ?", supplierId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public SupplierEntity findById(String supplierId) {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM Supplier WHERE SupplierID = ?", supplierId);
            if (resultSet.next()) {
                return new SupplierEntity(
                        resultSet.getString("SupplierID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Company"),
                        resultSet.getString("Email"),
                        resultSet.getString("PhoneNumber")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SupplierEntity> findAll() {
        List<SupplierEntity> supplierList = new ArrayList<>();
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM Supplier");
            while (resultSet.next()) {
                supplierList.add(new SupplierEntity(
                        resultSet.getString("SupplierID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Company"),
                        resultSet.getString("Email"),
                        resultSet.getString("PhoneNumber")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supplierList;
    }

    @Override
    public Optional<String> getLastSupplierId() {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT SupplierID FROM Supplier ORDER BY SupplierID DESC LIMIT 1");
            if (resultSet.next()) {
                return Optional.of(resultSet.getString("SupplierID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
