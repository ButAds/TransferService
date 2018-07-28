package nl.butads.challenge.transferservice.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import nl.butads.challenge.transferservice.model.api.AccountExchange;
import nl.butads.challenge.transferservice.model.api.BadRequestException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Api(value = "accountexchange", description = "the accountexchange API")
public interface AccountExchangeApi {

    @ApiOperation(value = "Exchanges currency between 2 accounts", nickname = "processExchange", notes = "Exchanges currency across 2 accounts", tags = {
        "exchange",})
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Successfully transferred funds."),
        @ApiResponse(code = 400, message = "Invalid request. See response details.")})
    @RequestMapping(value = "/accountexchange", produces = {"application/json"}, method = RequestMethod.POST)
    void processExchange(
        @ApiParam(value = "Invalid input, see details of the message") @Valid @RequestBody AccountExchange exchangeOperation)
        throws BadRequestException;

}