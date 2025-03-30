package entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SupplyCartEntity {
    private String productId;
    private String supplierId;
    private String name;
    private String category;
    private String size;
    private BigDecimal unitCost;
    private int quantity;
    private BigDecimal total;

    public void calculateTotal() {
        this.total = this.unitCost.multiply(BigDecimal.valueOf(quantity));
    }
}
