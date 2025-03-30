package service.custom;

import entity.OrderDetailEntity;
import service.SuperService;

import java.util.List;

public interface OrderDetailService extends SuperService {
    boolean addOrderDetail(OrderDetailEntity orderDetail);
    boolean updateOrderDetail(OrderDetailEntity orderDetail);
    boolean deleteOrderDetail(String orderId, String productId);
    OrderDetailEntity findOrderDetailById(String orderId, String productId);
    List<OrderDetailEntity> findAllOrderDetails();
}
