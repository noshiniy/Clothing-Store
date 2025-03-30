package repository.custom.impl;

import entity.StaffEntity;
import repository.custom.StaffDao;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StaffDaoImpl implements StaffDao {
    @Override
    public StaffEntity findByUsername(String username) {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM Staff WHERE username = ?", username);

            if (resultSet.next()) {
                return new StaffEntity(
                        resultSet.getString("staffId"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("fullName"),
                        resultSet.getString("address"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("nic"),
                        resultSet.getDate("dob").toLocalDate(),
                        resultSet.getDouble("salary")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean add(StaffEntity staff) {
        try {
            return CrudUtil.execute("INSERT INTO Staff (StaffID, Username, Password, FullName, Address, PhoneNumber, NIC, DOB, Salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    staff.getStaffId(), staff.getUsername(), staff.getPassword(), staff.getFullName(), staff.getAddress(),
                    staff.getPhoneNumber(), staff.getNic(), staff.getDob(), staff.getSalary());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(StaffEntity staff) {
        try {
            return CrudUtil.execute("UPDATE Staff SET Username = ?, Password = ?, FullName = ?, Address = ?, PhoneNumber = ?, NIC = ?, DOB = ?, Salary = ? WHERE StaffID = ?",
                    staff.getUsername(), staff.getPassword(), staff.getFullName(), staff.getAddress(), staff.getPhoneNumber(),
                    staff.getNic(), staff.getDob(), staff.getSalary(), staff.getStaffId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String staffId) {
        try {
            return CrudUtil.execute("DELETE FROM Staff WHERE StaffID = ?", staffId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public StaffEntity findById(String staffId) {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM Staff WHERE StaffID = ?", staffId);
            if (resultSet.next()) {
                return new StaffEntity(
                        resultSet.getString("StaffID"),
                        resultSet.getString("Username"),
                        resultSet.getString("Password"),
                        resultSet.getString("FullName"),
                        resultSet.getString("Address"),
                        resultSet.getString("PhoneNumber"),
                        resultSet.getString("NIC"),
                        resultSet.getDate("DOB").toLocalDate(),
                        resultSet.getDouble("Salary")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<String> getLastStaffId() {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT StaffID FROM Staff ORDER BY StaffID DESC LIMIT 1");
            if (resultSet.next()) {
                return Optional.of(resultSet.getString("StaffID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<StaffEntity> findAll() {
        List<StaffEntity> staffList = new ArrayList<>();
        String sql = "SELECT * FROM Staff";
        try {
            ResultSet resultSet = CrudUtil.execute(sql);
            while (resultSet.next()) {
                StaffEntity staff = new StaffEntity(
                        resultSet.getString("StaffID"),
                        resultSet.getString("Username"),
                        resultSet.getString("Password"),
                        resultSet.getString("FullName"),
                        resultSet.getString("Address"),
                        resultSet.getString("PhoneNumber"),
                        resultSet.getString("NIC"),
                        resultSet.getDate("DOB").toLocalDate(),
                        resultSet.getDouble("Salary")
                );
                staffList.add(staff);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }
}
