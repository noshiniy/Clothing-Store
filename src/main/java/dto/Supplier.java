package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    private String supplierId;
    private String supplierName;
    private String company;
    private String email;
    private String phoneNumber;
}
