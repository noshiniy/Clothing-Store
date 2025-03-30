package service.custom.impl;

import entity.ProductEntity;
import entity.SupplyEntity;
import repository.DaoFactory;
import repository.custom.ProductDao;
import repository.custom.SupplyDao;
import service.custom.SupplyService;
import util.DaoType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class SupplyServiceImpl implements SupplyService {
    private final SupplyDao supplyDao;
    private final ProductDao productDao;

    public SupplyServiceImpl() {
        this.supplyDao = DaoFactory.getInstance().getDaoType(DaoType.SUPPLY_DAO);
        this.productDao = DaoFactory.getInstance().getDaoType(DaoType.PRODUCT_DAO);
    }

    @Override
    public boolean addSupply(SupplyEntity supply) {
        if (supply == null || supply.getQty() <= 0) {
            throw new IllegalArgumentException("Invalid supply details.");
        }

        boolean isSupplyAdded = supplyDao.add(supply);

        if (isSupplyAdded) {
            return updateProductStock(supply.getId().getProductId(), supply.getQty());
        }
        return false;
    }

    @Override
    public boolean updateSupply(SupplyEntity supply) {
        if (supply == null || supply.getQty() < 0) {
            throw new IllegalArgumentException("Invalid supply details.");
        }

        SupplyEntity oldSupply = supplyDao.findById(supply.getId().getSupplyId());

        if (oldSupply != null) {
            int oldQty = oldSupply.getQty();
            int newQty = supply.getQty();
            int quantityDifference = newQty - oldQty;

            boolean isProductStockUpdated = updateProductStock(supply.getId().getProductId(), quantityDifference);

            if (isProductStockUpdated) {
                supply.setTotal(supply.getUnitCost().multiply(BigDecimal.valueOf(newQty)));
                return supplyDao.update(supply);
            }
        }

        return false;
    }


    @Override
    public boolean deleteSupply(String supplyId, String productId, String supplierId) {
        SupplyEntity supply = supplyDao.findById(supplyId);

        if (supply != null) {
            boolean isProductUpdated = updateProductStock(productId, -supply.getQty());

            if (isProductUpdated) {
                return supplyDao.delete(supplyId);
            }
        }
        return false;
    }

    @Override
    public SupplyEntity findSupplyById(String supplyId, String productId, String supplierId) {
        return supplyDao.findById(supplyId);
    }

    @Override
    public List<SupplyEntity> findAllSupplies() {
        return supplyDao.findAll();
    }

    @Override
    public String generateSupplyId() {
        Optional<String> lastSupplyIdOpt = supplyDao.getLastSupplyId();

        if (lastSupplyIdOpt.isPresent()) {
            String lastSupplyId = lastSupplyIdOpt.get();
            String numericPart = lastSupplyId.substring(2);
            int newIdNumber = Integer.parseInt(numericPart) + 1;
            return String.format("SP%04d", newIdNumber);
        }
        return "SP0001";
    }

    private boolean updateProductStock(String productId, int quantityChange) {
        ProductEntity product = productDao.findById(productId);
        if (product != null) {
            int newQty = product.getQtyOnHand() + quantityChange;
            product.setQtyOnHand(Math.max(newQty, 0));
            return productDao.update(product);
        }
        return false;
    }

}
