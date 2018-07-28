package nl.butads.challenge.transferservice.api.impl;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import nl.butads.challenge.transferservice.api.AccountsApi;
import nl.butads.challenge.transferservice.model.api.Account;
import nl.butads.challenge.transferservice.model.api.AccountListResult;
import nl.butads.challenge.transferservice.model.api.BadRequestException;
import nl.butads.challenge.transferservice.model.api.NotFoundException;
import nl.butads.challenge.transferservice.service.AccountService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequiredArgsConstructor
public class AccountsApiController implements AccountsApi {

    private final AccountService accountService;

    public Account addAccount(@Valid @RequestBody Account account) throws BadRequestException {
        log.debug("Got addAccount request: {}", account);
        return accountService.createAccount(account);
    }

    public Account getAccountById(@PathVariable("id") String id) throws NotFoundException {
        log.debug("Got getAccountById request: {}", id);
        return accountService.getAccountById(id);
    }

    public AccountListResult getAccounts() {
        log.debug("Got getAccounts request");
        List<Account> allAccounts = accountService.getAllAccounts();
        log.debug("Found {} accounts to return.", allAccounts.size());
        return AccountListResult.builder().accounts(allAccounts).build();
    }
}