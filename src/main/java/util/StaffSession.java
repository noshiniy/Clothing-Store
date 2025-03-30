package util;

import entity.StaffEntity;

public class StaffSession {
    private static StaffSession instance;
    private StaffEntity staff;

    private StaffSession() {}

    public static StaffSession getInstance() {
        return instance == null ? instance = new StaffSession() : instance;
    }

    public void setStaff(StaffEntity staff) {
        this.staff = staff;
    }

    public StaffEntity getStaff() {
        return staff;
    }
}
