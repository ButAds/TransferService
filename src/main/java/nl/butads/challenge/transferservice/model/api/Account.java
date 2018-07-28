package nl.butads.challenge.transferservice.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

/**
 * Account
 */
@Validated
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @JsonProperty("id")
    @ApiModelProperty(example = "43ecde6b-f3a8-4687-8d45-ef95f238df30")
    @Pattern(regexp = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")
    private String id;

    @JsonProperty("balance")
    @ApiModelProperty(example = "100.00", required = true)
    @NotNull
    private BigDecimal balance;

    @JsonProperty("name")
    @Size(max = 140)
    @ApiModelProperty(example = "Mark Bertels", required = true)
    @NotNull
    private String name;
}

