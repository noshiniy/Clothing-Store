package service.custom.impl;

import entity.OrderDetailEntity;
import entity.OrderDetailId;
import repository.DaoFactory;
import repository.custom.OrderDetailDao;
import service.custom.OrderDetailService;
import util.DaoType;

import java.math.BigDecimal;
import java.util.List;

public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailDao orderDetailDao;

    public OrderDetailServiceImpl() {
        this.orderDetailDao = DaoFactory.getInstance().getDaoType(DaoType.ORDER_DETAIL_DAO);
    }

    @Override
    public boolean addOrderDetail(OrderDetailEntity orderDetail) {
        if (orderDetail == null || orderDetail.getQty() <= 0 || orderDetail.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid order detail.");
        }
        return orderDetailDao.add(orderDetail);
    }

    @Override
    public boolean updateOrderDetail(OrderDetailEntity orderDetail) {
        if (orderDetail == null || orderDetail.getQty() < 0 || orderDetail.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invalid order detail.");
        }
        return orderDetailDao.update(orderDetail);
    }

    @Override
    public boolean deleteOrderDetail(String orderId, String productId) {
        OrderDetailId id = new OrderDetailId(orderId, productId);
        return orderDetailDao.delete(id);
    }

    @Override
    public OrderDetailEntity findOrderDetailById(String orderId, String productId) {
        OrderDetailId id = new OrderDetailId(orderId, productId);
        return orderDetailDao.findById(id);
    }

    @Override
    public List<OrderDetailEntity> findAllOrderDetails() {
        return orderDetailDao.findAll();
    }
}
