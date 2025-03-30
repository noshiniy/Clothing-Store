package repository.custom.impl;

import db.DBConnection;
import entity.AdminEntity;
import repository.custom.AdminDao;
import util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AdminDaoImpl implements AdminDao {
    @Override
    public AdminEntity findByUsername(String username) {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM Admin WHERE username = ?", username);
            if (resultSet.next()) {
                return new AdminEntity(
                        resultSet.getString("adminId"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("phoneNumber")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean add(AdminEntity entity) {
        return false;
    }

    @Override
    public boolean update(AdminEntity admin) {
        try {
            String sql = "UPDATE Admin SET username = ?, phoneNumber = ?, password = ? WHERE adminId = ?";
            return CrudUtil.execute(sql, admin.getUsername(), admin.getPhoneNumber(), admin.getPassword(), admin.getAdminId());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public AdminEntity findById(String adminId) {
        AdminEntity admin = null;
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Admin WHERE adminId = ?");
            preparedStatement.setString(1, adminId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                admin = new AdminEntity(
                        resultSet.getString("adminId"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("phoneNumber")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
    }

    @Override
    public List<AdminEntity> findAll() {
        return List.of();
    }
}
