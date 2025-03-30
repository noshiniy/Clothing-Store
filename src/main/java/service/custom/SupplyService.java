package service.custom;

import entity.SupplyEntity;
import service.SuperService;

import java.util.List;

public interface SupplyService extends SuperService {
    boolean addSupply(SupplyEntity supply);
    boolean updateSupply(SupplyEntity supply);
    boolean deleteSupply(String supplyId, String productId, String supplierId);
    SupplyEntity findSupplyById(String supplyId, String productId, String supplierId);
    List<SupplyEntity> findAllSupplies();
    String generateSupplyId();
}
