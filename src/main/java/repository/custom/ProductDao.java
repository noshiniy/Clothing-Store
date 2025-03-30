package repository.custom;

import entity.ProductEntity;
import repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductDao extends CrudRepository<ProductEntity> {
    boolean add(ProductEntity product);
    boolean update(ProductEntity product);
    boolean delete(String productId);
    ProductEntity findById(String productId);
    List<ProductEntity> findAll();
    Optional<String> getLastProductId();
}
