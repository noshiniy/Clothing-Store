package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String orderId;
    private Double netTotal;
    private Double totalDiscount;
    private LocalDateTime dateAndTime;
    private String staffId;
    private String customerEmail;
    private String customerPhoneNumber;
    private List<OrderDetail> orderDetails;
}
