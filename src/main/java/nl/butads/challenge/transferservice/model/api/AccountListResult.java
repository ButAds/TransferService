package nl.butads.challenge.transferservice.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

/**
 * AccountListResult
 */
@Validated
@Getter
@Setter
@Builder
@ToString
public class AccountListResult {

    @JsonProperty("accounts")
    @ApiModelProperty(required = true)
    @Valid
    @Builder.Default
    private List<Account> accounts = new ArrayList<>();
}

