package service.custom;

import entity.SupplierEntity;
import service.SuperService;

import java.util.List;

public interface SupplierService extends SuperService {
    boolean addSupplier(SupplierEntity supplier);
    boolean updateSupplier(SupplierEntity supplier);
    boolean deleteSupplier(String supplierId);
    SupplierEntity findSupplierById(String supplierId);
    List<SupplierEntity> findAllSuppliers();
    String generateSupplierId();
}
