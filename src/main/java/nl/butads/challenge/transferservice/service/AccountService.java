package nl.butads.challenge.transferservice.service;

import static java.util.stream.Collectors.toList;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import nl.butads.challenge.transferservice.model.api.Account;
import nl.butads.challenge.transferservice.model.api.BadRequestException;
import nl.butads.challenge.transferservice.model.api.NotFoundException;
import nl.butads.challenge.transferservice.model.dbo.AccountDbo;
import nl.butads.challenge.transferservice.repository.AccountCrudRepository;
import nl.butads.challenge.transferservice.repository.AccountReadonlyRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountCrudRepository accountCrudRepository;
    private final AccountReadonlyRepository accountReadonlyRepository;

    @Transactional(propagation = NOT_SUPPORTED)
    public List<Account> getAllAccounts() {
        log.debug("Loading all accounts from the database.");
        List<AccountDbo> accountDbos = accountReadonlyRepository.findAll();
        log.debug("Loaded all {} accounts from the database.", accountDbos.size());
        log.debug("Converting from AccountDbo to Account");

        return accountDbos
            .stream()
            .map(AccountService::fromDatabase)
            .collect(toList());
    }

    /**
     * Creates a new account with given details.
     *
     * @param requestedAccount The original request.
     * @return A new persisted account object.
     * @throws BadRequestException if the service already has a account with a certain name.
     */
    @Transactional(isolation = REPEATABLE_READ, propagation = REQUIRES_NEW)
    public Account createAccount(Account requestedAccount) throws BadRequestException {
        AccountDbo dboExample = AccountDbo.builder()
            .name(requestedAccount.getName())
            .build();

        List<AccountDbo> matchedResults = accountCrudRepository
            .findAll(Example.of(dboExample));
        if (!isEmpty(matchedResults)) {
            throw new BadRequestException(
                String.format("Account with name [%s] already exists.", requestedAccount.getName()));
        }

        AccountDbo newAccount = AccountDbo.builder()
            .id(UUID.randomUUID().toString())
            .name(requestedAccount.getName())
            .balance(requestedAccount.getBalance())
            .build();

        return fromDatabase(accountCrudRepository.save(newAccount));
    }

    /**
     * Searches for an {@link Account} with a given ID.
     *
     * @param id the id to search for.
     * @return Account never {@code null}
     * @throws NotFoundException when the account does not exist
     */
    @Transactional(propagation = NOT_SUPPORTED)
    public Account getAccountById(String id) throws NotFoundException {
        log.debug("Searching for account with id {}", id);
        AccountDbo accountDbo = accountReadonlyRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("Account with id [%s] does not exist", id)));
        log.debug("Found account: {}", accountDbo);
        return fromDatabase(accountDbo);
    }

    private static Account fromDatabase(AccountDbo dbo) {
        return Account.builder()
            .id(dbo.getId())
            .name(dbo.getName())
            .balance(dbo.getBalance())
            .build();
    }
}
