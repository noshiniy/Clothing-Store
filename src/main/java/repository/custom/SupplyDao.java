package repository.custom;

import entity.SupplierEntity;
import entity.SupplyEntity;
import repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SupplyDao extends CrudRepository<SupplyEntity> {
    boolean add(SupplyEntity supply);
    boolean update(SupplyEntity supply);
    boolean delete(String supplyId);
    SupplyEntity findById(String supplyId);
    List<SupplyEntity> findAll();
    Optional<String> getLastSupplyId();
}
