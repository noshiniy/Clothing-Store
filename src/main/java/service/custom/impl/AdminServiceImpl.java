package service.custom.impl;

import entity.AdminEntity;
import repository.DaoFactory;
import repository.custom.AdminDao;
import service.custom.AdminService;
import util.DaoType;

public class AdminServiceImpl implements AdminService {
    private final AdminDao adminDao;

    public AdminServiceImpl() {
        this.adminDao = DaoFactory.getInstance().getDaoType(DaoType.ADMIN_DAO);
    }

    @Override
    public boolean validateAdminLogin(String username, String password) {
        AdminEntity admin = adminDao.findByUsername(username);
        if (admin != null) {
            return admin.getPassword().equals(password);
        }
        return false;
    }

    @Override
    public boolean usernameExists(String username) {
        AdminEntity admin = adminDao.findByUsername(username);
        return admin != null;
    }

    @Override
    public boolean updateAdminDetails(String adminId, String username, String password, String contactNumber) {
        AdminEntity admin = new AdminEntity(adminId, username, password, contactNumber);
        return adminDao.update(admin);
    }

    @Override
    public String getAdminIdByUsername(String username) {
        AdminEntity admin = adminDao.findByUsername(username);
        return admin != null ? admin.getAdminId() : null;
    }

    @Override
    public AdminEntity findAdminById(String adminId) {
        return adminDao.findById(adminId);
    }

    @Override
    public AdminEntity findAdminByUsername(String username) {
        return adminDao.findByUsername(username);
    }
}
