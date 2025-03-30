package service.custom;

import entity.AdminEntity;
import service.SuperService;

public interface AdminService extends SuperService {
    boolean validateAdminLogin(String username, String password);
    boolean usernameExists(String username);
    boolean updateAdminDetails(String text, String text1, String text2, String text3);
    String getAdminIdByUsername(String text);
    AdminEntity findAdminById(String adminId);
    AdminEntity findAdminByUsername(String username);
}
