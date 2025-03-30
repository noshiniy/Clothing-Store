package service.custom.impl;

import entity.StaffEntity;
import repository.DaoFactory;
import repository.custom.StaffDao;
import service.custom.StaffService;
import util.DaoType;
import util.PasswordUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class StaffServiceImpl implements StaffService {
    private final StaffDao staffDao;

    public StaffServiceImpl() {
        this.staffDao = DaoFactory.getInstance().getDaoType(DaoType.STAFF_DAO);
    }

    @Override
    public boolean validateStaffLogin(String username, String password) {
        StaffEntity staff = staffDao.findByUsername(username);
        if (staff != null) {
            System.out.println("Entered Password: " + password);
            System.out.println("Stored Hashed Password: " + staff.getPassword());
            return PasswordUtil.verifyPassword(password, staff.getPassword());
        }
        return false;
    }


    @Override
    public boolean usernameExists(String username) {
        StaffEntity staff = staffDao.findByUsername(username);
        return staff != null;
    }

    @Override
    public boolean addStaff(String staffId, String email, String password, String fullName, String address, String contact,
                            String nic, LocalDate dob, Double salary) {
        String hashedPassword = PasswordUtil.hashPassword(password);
        StaffEntity staff = new StaffEntity(staffId, email, hashedPassword, fullName, address, contact, nic, dob, salary);
        return staffDao.add(staff);
    }


    @Override
    public boolean updateStaff(String staffId, String email, String password, String fullName, String address, String contact,
                               String nic, LocalDate dob,
                               Double salary) {
        StaffEntity existingStaff = staffDao.findById(staffId);
        if (existingStaff == null) {
            return false;
        }
        String hashedPassword = password != null && !password.isEmpty() ? PasswordUtil.hashPassword(password) : existingStaff.getPassword();
        StaffEntity updatedStaff = new StaffEntity(
                staffId, email, hashedPassword, fullName, address, contact, nic, dob, salary
        );
        return staffDao.update(updatedStaff);
    }

    @Override
    public boolean deleteStaff(String staffId) {
        return staffDao.delete(staffId);
    }

    @Override
    public StaffEntity findStaffById(String staffId) {
        return staffDao.findById(staffId);
    }

    @Override
    public String generateNewStaffId() {
        Optional<String> lastStaffId = staffDao.getLastStaffId();
        if (lastStaffId.isPresent()) {
            int newId = Integer.parseInt(lastStaffId.get().substring(1)) + 1;
            return String.format("S%04d", newId);
        }
        return "S0001";
    }

    @Override
    public List<StaffEntity> getAllStaffMembers() {
        return staffDao.findAll();
    }

    @Override
    public StaffEntity findStaffByUsername(String username) {
        return staffDao.findByUsername(username);
    }

    @Override
    public boolean updateStaffAccount(String staffId, String username, String password) {
        StaffEntity existingStaff = staffDao.findById(staffId);
        if (existingStaff != null) {
            String hashedPassword = password != null && !password.isEmpty() ? PasswordUtil.hashPassword(password) : existingStaff.getPassword();
            StaffEntity updatedStaff = new StaffEntity(
                    staffId,
                    username,
                    hashedPassword,
                    existingStaff.getFullName(),
                    existingStaff.getAddress(),
                    existingStaff.getPhoneNumber(),
                    existingStaff.getNic(),
                    existingStaff.getDob(),
                    existingStaff.getSalary()
            );

            return staffDao.update(updatedStaff);
        }
        return false;
    }
}
