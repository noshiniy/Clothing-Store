package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @Column(nullable = false, length = 10)
    private String orderId;

    @Column(nullable = false)
    private BigDecimal netTotal;

    @Column(nullable = false)
    private BigDecimal totalDiscount;

    @Column(nullable = false)
    private LocalDateTime dateAndTime;

    @Column(nullable = false, length = 10)
    private String staffId;

    @Column(nullable = false, length = 100)
    private String customerEmail;

    @Column(length = 10)
    private String customerPhoneNumber;

    @OneToMany(mappedBy = "order")
    private List<OrderDetailEntity> orderDetails;

    public OrderEntity(String orderId, BigDecimal netTotal, BigDecimal totalDiscount,
                       LocalDateTime dateAndTime, String staffId,
                       String customerEmail, String customerPhoneNumber) {
        this.orderId = orderId;
        this.netTotal = netTotal;
        this.totalDiscount = totalDiscount;
        this.dateAndTime = dateAndTime;
        this.staffId = staffId;
        this.customerEmail = customerEmail;
        this.customerPhoneNumber = customerPhoneNumber;
    }
}
