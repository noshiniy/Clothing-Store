package service.custom;

import entity.ProductEntity;
import service.SuperService;

import java.util.List;

public interface ProductService extends SuperService {
    boolean addProduct(ProductEntity product);
    boolean updateProduct(ProductEntity product);
    boolean deleteProduct(String productId);
    ProductEntity findProductById(String productId);
    List<ProductEntity> findAllProducts();
    String generateProductId();
}
