package repository.custom.impl;

import entity.ProductEntity;
import repository.custom.ProductDao;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDaoImpl implements ProductDao {
    @Override
    public boolean add(ProductEntity product) {
        try {
            return CrudUtil.execute("INSERT INTO Product (ProductID, Name, Category, Size, UnitPrice, QtyOnHand) VALUES (?, ?, ?, ?, ?, ?)",
                    product.getProductId(), product.getName(), product.getCategory(), product.getSize(),
                    product.getUnitPrice(), product.getQtyOnHand());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(ProductEntity product) {
        try {
            return CrudUtil.execute("UPDATE Product SET Name = ?, Category = ?, Size = ?, UnitPrice = ?, QtyOnHand = ? WHERE ProductID = ?",
                    product.getName(), product.getCategory(), product.getSize(),
                    product.getUnitPrice(), product.getQtyOnHand(), product.getProductId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String productId) {
        try {
            return CrudUtil.execute("DELETE FROM Product WHERE ProductID = ?", productId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ProductEntity findById(String productId) {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM Product WHERE ProductID = ?", productId);
            if (resultSet.next()) {
                return new ProductEntity(
                        resultSet.getString("ProductID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Category"),
                        resultSet.getString("Size"),
                        resultSet.getBigDecimal("UnitPrice"),
                        resultSet.getInt("QtyOnHand")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ProductEntity> findAll() {
        List<ProductEntity> productList = new ArrayList<>();
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM Product");
            while (resultSet.next()) {
                productList.add(new ProductEntity(
                        resultSet.getString("ProductID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Category"),
                        resultSet.getString("Size"),
                        resultSet.getBigDecimal("UnitPrice"),
                        resultSet.getInt("QtyOnHand")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    @Override
    public Optional<String> getLastProductId() {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT productId FROM Product ORDER BY productId DESC LIMIT 1");
            if (resultSet.next()) {
                return Optional.of(resultSet.getString("productId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
