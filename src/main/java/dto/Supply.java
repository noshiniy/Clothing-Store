package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supply {
    private String supplyId;
    private String productId;
    private String supplierId;
    private Double unitCost;
    private Integer qty;
    private Double total;
    private LocalDateTime dateAndTime;
}
