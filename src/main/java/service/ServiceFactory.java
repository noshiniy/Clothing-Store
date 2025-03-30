package service;

import service.custom.impl.*;
import util.ServiceType;

public class ServiceFactory {

    private static ServiceFactory instance;

    private ServiceFactory() {}

    public static ServiceFactory getInstance() {
        return instance == null ? instance = new ServiceFactory() : instance;
    }

    public <T extends SuperService>T getServiceType(ServiceType type) {
        switch (type) {
            case ADMIN_SERVICE:return (T) new AdminServiceImpl();
            case ORDER_SERVICE:return (T) new OrderServiceImpl();
            case PRODUCT_SERVICE:return (T) new ProductServiceImpl();
            case STAFF_SERVICE:return (T) new StaffServiceImpl();
            case SUPPLIER_SERVICE:return (T) new SupplierServiceImpl();
            case SUPPLY_SERVICE:return (T) new SupplyServiceImpl();
            case ORDER_DETAIL_SERVICE:return (T) new OrderDetailServiceImpl();
        }
        return null;
    }
}
