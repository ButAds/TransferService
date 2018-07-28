package nl.butads.challenge.transferservice.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import nl.butads.challenge.transferservice.model.api.Account;
import nl.butads.challenge.transferservice.model.api.AccountListResult;
import nl.butads.challenge.transferservice.model.api.BadRequestException;
import nl.butads.challenge.transferservice.model.api.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Api(value = "accounts", description = "the accounts API")
public interface AccountsApi {

    @ApiOperation(value = "Create an account", nickname = "addAccount", notes = "Create an account", response = Account.class, tags = {
        "account",})
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Successfully created an account", response = Account.class),
        @ApiResponse(code = 400, message = "Invalid input")})
    @RequestMapping(value = "/accounts",
        produces = {"application/json"},
        method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    Account addAccount(
        @ApiParam(value = "Object used to create an account. The id field is optional.") @Valid @RequestBody Account account)
        throws BadRequestException;

    @ApiOperation(value = "Find account by ID", nickname = "getAccountById", notes = "Returns a single account", response = Account.class, tags = {
        "account",})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Get a single account by id", response = Account.class),
        @ApiResponse(code = 404, message = "Account not found")})
    @RequestMapping(value = "/accounts/{id}",
        produces = {"application/json"},
        method = RequestMethod.GET)
    Account getAccountById(
        @ApiParam(value = "ID of account to return", required = true) @PathVariable("id") String id)
        throws NotFoundException;

    @ApiOperation(value = "Gets the complete list of all accounts", nickname = "getAccounts", notes = "Gets the complete list of all accounts", response = AccountListResult.class, tags = {
        "account",})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Gets a list of all accounts known to the system", response = AccountListResult.class)})
    @RequestMapping(value = "/accounts",
        produces = {"application/json"},
        method = RequestMethod.GET)
    AccountListResult getAccounts();
}
