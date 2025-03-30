package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Staff {
    private String staffId;
    private String username;
    private String password;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String nic;
    private LocalDate dob;
    private Double salary;
}
