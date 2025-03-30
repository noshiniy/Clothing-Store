package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    private String orderId;
    private String productId;
    private Double unitPrice;
    private Integer qty;
    private Double discount;
    private Double total;
}
