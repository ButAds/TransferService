package nl.butads.challenge.transferservice.model.dbo;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Data;

/**
 * Account.
 */
@Entity(name = "account")
@Builder
@Data
public class AccountDbo {

    @Id
    private String id;

    @Column(length = 140, unique = true)
    private String name;

    @Column(precision = 18, scale = 2)
    private BigDecimal balance;
}