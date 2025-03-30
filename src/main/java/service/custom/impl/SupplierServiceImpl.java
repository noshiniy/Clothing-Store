package service.custom.impl;

import entity.SupplierEntity;
import repository.DaoFactory;
import repository.custom.SupplierDao;
import service.custom.SupplierService;
import util.DaoType;

import java.util.List;
import java.util.Optional;

public class SupplierServiceImpl implements SupplierService {
    private final SupplierDao supplierDao;

    public SupplierServiceImpl() {
        this.supplierDao = DaoFactory.getInstance().getDaoType(DaoType.SUPPLIER_DAO);
    }

    @Override
    public boolean addSupplier(SupplierEntity supplier) {
        return supplierDao.add(supplier);
    }

    @Override
    public boolean updateSupplier(SupplierEntity supplier) {
        return supplierDao.update(supplier);
    }

    @Override
    public boolean deleteSupplier(String supplierId) {
        return supplierDao.delete(supplierId);
    }

    @Override
    public SupplierEntity findSupplierById(String supplierId) {
        return supplierDao.findById(supplierId);
    }

    @Override
    public List<SupplierEntity> findAllSuppliers() {
        return supplierDao.findAll();
    }

    @Override
    public String generateSupplierId() {
        Optional<String> lastSupplierId = supplierDao.getLastSupplierId();
        if (lastSupplierId.isPresent()) {
            int newId = Integer.parseInt(lastSupplierId.get().substring(3)) + 1;
            return String.format("SUP%04d", newId);
        }
        return "SUP0001";
    }
}
