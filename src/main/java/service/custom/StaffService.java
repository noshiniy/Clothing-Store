package service.custom;

import entity.StaffEntity;
import service.SuperService;

import java.time.LocalDate;
import java.util.List;

public interface StaffService extends SuperService {
    boolean validateStaffLogin(String username, String password);
    boolean usernameExists(String username);
    boolean addStaff(String staffId, String email, String password, String fullName, String address, String contact,
                     String nic, LocalDate dob,
                     Double salary);
    boolean updateStaff(String staffId, String email, String password, String fullName, String address, String contact,
                        String nic, LocalDate dob,
                        Double salary);
    boolean deleteStaff(String staffId);
    StaffEntity findStaffById(String staffId);
    String generateNewStaffId();
    List<StaffEntity> getAllStaffMembers();
    StaffEntity findStaffByUsername(String username);
    boolean updateStaffAccount(String staffId, String username, String password);
}
