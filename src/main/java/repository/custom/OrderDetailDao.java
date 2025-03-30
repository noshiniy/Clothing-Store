package repository.custom;

import entity.OrderDetailEntity;
import entity.OrderDetailId;
import repository.CrudRepository;

public interface OrderDetailDao extends CrudRepository<OrderDetailEntity> {
    boolean add(OrderDetailEntity entity);
    boolean update(OrderDetailEntity entity);
    boolean delete(OrderDetailId id);
    OrderDetailEntity findById(OrderDetailId id);
}
