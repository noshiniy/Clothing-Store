package repository;

import repository.custom.impl.*;
import util.DaoType;

public class DaoFactory {

    private static DaoFactory instance;

    private DaoFactory() {}

    public static DaoFactory getInstance() {
        return instance == null ? instance = new DaoFactory() : instance;
    }

    public <T extends SuperDao>T getDaoType(DaoType type) {
        switch (type) {
            case ADMIN_DAO:return (T) new AdminDaoImpl();
            case ORDER_DAO:return (T) new OrderDaoImpl();
            case PRODUCT_DAO:return (T) new ProductDaoImpl();
            case STAFF_DAO:return (T) new StaffDaoImpl();
            case SUPPLIER_DAO:return (T) new SupplierDaoImpl();
            case SUPPLY_DAO:return (T) new SupplyDaoImpl();
            case ORDER_DETAIL_DAO:return (T) new OrderDetailDaoImpl();
        }
        return null;
    }
}
