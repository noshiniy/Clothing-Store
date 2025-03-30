package service.custom.impl;

import entity.OrderDetailEntity;
import entity.OrderEntity;
import entity.ProductEntity;
import repository.DaoFactory;
import repository.custom.OrderDao;
import repository.custom.OrderDetailDao;
import repository.custom.ProductDao;
import service.custom.OrderService;
import util.DaoType;

import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final OrderDetailDao orderDetailDao;
    private final ProductDao productDao;

    public OrderServiceImpl() {
        this.orderDao = DaoFactory.getInstance().getDaoType(DaoType.ORDER_DAO);
        this.orderDetailDao = DaoFactory.getInstance().getDaoType(DaoType.ORDER_DETAIL_DAO);
        this.productDao = DaoFactory.getInstance().getDaoType(DaoType.PRODUCT_DAO);
    }

    @Override
    public boolean addOrder(OrderEntity order) {
        if (order == null || order.getOrderDetails() == null || order.getOrderDetails().isEmpty()) {
            throw new IllegalArgumentException("Invalid order details.");
        }

        boolean isOrderAdded = orderDao.add(order);

        if (isOrderAdded) {
            for (OrderDetailEntity orderDetail : order.getOrderDetails()) {
                boolean isOrderDetailAdded = orderDetailDao.add(orderDetail);

                if (!isOrderDetailAdded || !updateProductStock(orderDetail.getId().getProductId(), -orderDetail.getQty())) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean updateOrder(OrderEntity order) {
        if (order == null || order.getOrderDetails() == null || order.getOrderDetails().isEmpty()) {
            throw new IllegalArgumentException("Invalid order details.");
        }

        OrderEntity oldOrder = orderDao.findById(order.getOrderId());

        if (oldOrder != null) {
            boolean isOrderUpdated = orderDao.update(order);

            if (isOrderUpdated) {
                for (OrderDetailEntity newDetail : order.getOrderDetails()) {
                    Optional<OrderDetailEntity> oldDetailOpt = oldOrder.getOrderDetails().stream()
                            .filter(od -> od.getId().getProductId().equals(newDetail.getId().getProductId()))
                            .findFirst();

                    if (oldDetailOpt.isPresent()) {
                        int oldQty = oldDetailOpt.get().getQty();
                        int newQty = newDetail.getQty();
                        int qtyDifference = newQty - oldQty;

                        boolean isStockUpdated = updateProductStock(newDetail.getId().getProductId(), -qtyDifference);

                        if (!isStockUpdated || !orderDetailDao.update(newDetail)) {
                            return false;
                        }
                    } else {
                        if (!orderDetailDao.add(newDetail) || !updateProductStock(newDetail.getId().getProductId(), -newDetail.getQty())) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean deleteOrder(String orderId) {
        OrderEntity order = orderDao.findById(orderId);

        if (order != null) {
            for (OrderDetailEntity orderDetail : order.getOrderDetails()) {
                boolean isStockUpdated = updateProductStock(orderDetail.getId().getProductId(), orderDetail.getQty());

                if (!isStockUpdated || !orderDetailDao.delete(orderDetail.getId())) {
                    return false;
                }
            }

            return orderDao.delete(orderId);
        }

        return false;
    }

    @Override
    public OrderEntity findOrderById(String orderId) {
        return orderDao.findById(orderId);
    }

    @Override
    public List<OrderEntity> findAllOrders() {
        return orderDao.findAll();
    }

    @Override
    public String generateOrderId() {
        Optional<String> lastOrderIdOpt = orderDao.getLastOrderId();
        if (lastOrderIdOpt.isPresent()) {
            String lastOrderId = lastOrderIdOpt.get();
            String numericPart = lastOrderId.substring(2);
            int newIdNumber = Integer.parseInt(numericPart) + 1;
            return String.format("OD%04d", newIdNumber);
        }
        return "OD0001";
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
