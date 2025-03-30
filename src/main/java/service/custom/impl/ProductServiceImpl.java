package service.custom.impl;

import entity.ProductEntity;
import repository.DaoFactory;
import repository.custom.ProductDao;
import service.custom.ProductService;
import util.DaoType;

import java.util.List;
import java.util.Optional;

public class ProductServiceImpl implements ProductService {
    private final ProductDao productDao;

    public ProductServiceImpl() {
        this.productDao = DaoFactory.getInstance().getDaoType(DaoType.PRODUCT_DAO);
    }

    @Override
    public boolean addProduct(ProductEntity product) {
        return productDao.add(product);
    }

    @Override
    public boolean updateProduct(ProductEntity product) {
        return productDao.update(product);
    }

    @Override
    public boolean deleteProduct(String productId) {
        return productDao.delete(productId);
    }

    @Override
    public ProductEntity findProductById(String productId) {
        return productDao.findById(productId);
    }

    @Override
    public List<ProductEntity> findAllProducts() {
        return productDao.findAll();
    }

    @Override
    public String generateProductId() {
        Optional<String> lastProductId = productDao.getLastProductId();
        if (lastProductId.isPresent()) {
            int newId = Integer.parseInt(lastProductId.get().substring(1)) + 1;
            return String.format("P%04d", newId);
        }
        return "P0001";
    }
}
