package nl.butads.challenge.transferservice.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

/**
 * AccountExchange
 */
@Validated
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AccountExchange {

    @ApiModelProperty(required = true, example = "43ecde6b-f3a8-4687-8d45-ef95f238df30")
    @NotNull
    @JsonProperty("fromAccount")
    @Pattern(regexp = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")
    private String fromAccount;

    @ApiModelProperty(required = true, example = "43ecde6b-f3a8-4687-8d45-ef95f238df30")
    @NotNull
    @JsonProperty("toAccount")
    @Pattern(regexp = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")
    private String toAccount;

    @ApiModelProperty(required = true, example = "100.00")
    @JsonProperty("amount")
    @NotNull
    private BigDecimal amount;
}

