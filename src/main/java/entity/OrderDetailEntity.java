package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orderdetail")
public class OrderDetailEntity {

    @EmbeddedId
    private OrderDetailId id;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private Integer qty;

    private BigDecimal discount;

    private BigDecimal total;

    @ManyToOne
    private OrderEntity order;

    public OrderDetailEntity(OrderDetailId id, BigDecimal unitPrice, Integer qty, BigDecimal discount, BigDecimal total) {
        this.id = id;
        this.unitPrice = unitPrice;
        this.qty = qty;
        this.discount = discount;
        this.total = total;
    }
}
