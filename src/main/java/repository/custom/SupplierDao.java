package repository.custom;

import entity.SupplierEntity;
import repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SupplierDao extends CrudRepository<SupplierEntity> {
    boolean add(SupplierEntity supplier);
    boolean update(SupplierEntity supplier);
    boolean delete(String supplierId);
    SupplierEntity findById(String supplierId);
    List<SupplierEntity> findAll();
    Optional<String> getLastSupplierId();
}
