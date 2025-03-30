package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admin")
public class AdminEntity {

    @Id
    @Column(nullable = false, unique = true, length = 10)
    private String adminId;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 30)
    private String password;

    @Column(length = 15)
    private String phoneNumber;
}
