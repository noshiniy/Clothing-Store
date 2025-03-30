package util;

import entity.AdminEntity;

public class AdminSession {
    private static AdminSession instance;
    private AdminEntity admin;

    private AdminSession() {}

    public static AdminSession getInstance() {
        return instance == null ? instance = new AdminSession() : instance;
    }

    public void setAdmin(AdminEntity admin) {
        this.admin = admin;
    }

    public AdminEntity getAdmin() {
        return admin;
    }
}
