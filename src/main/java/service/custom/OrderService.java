package service.custom;

import entity.OrderEntity;
import service.SuperService;

import java.util.List;

public interface OrderService extends SuperService {
    boolean addOrder(OrderEntity order);
    boolean updateOrder(OrderEntity order);
    boolean deleteOrder(String orderId);
    OrderEntity findOrderById(String orderId);
    List<OrderEntity> findAllOrders();
    String generateOrderId();
}
