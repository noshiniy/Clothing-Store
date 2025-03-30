package repository.custom;

import entity.OrderEntity;
import repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrderDao extends CrudRepository<OrderEntity> {
    boolean add(OrderEntity order);
    boolean update(OrderEntity order);
    boolean delete(String orderId);
    OrderEntity findById(String orderId);
    List<OrderEntity> findAll();
    Optional<String> getLastOrderId();
}
