package nl.butads.challenge.transferservice.service;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import nl.butads.challenge.transferservice.model.api.AccountExchange;
import nl.butads.challenge.transferservice.model.api.BadRequestException;
import nl.butads.challenge.transferservice.model.dbo.AccountDbo;
import nl.butads.challenge.transferservice.repository.AccountCrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountCrudRepository accountCrudRepository;

    /**
     * Performs an exchange between two parties.
     *
     * @param exchangeOperation operation containing the details.
     * @throws BadRequestException if there is invalid data in the requested operation.
     */
    @Transactional(isolation = SERIALIZABLE, propagation = REQUIRES_NEW)
    public void transferFunds(AccountExchange exchangeOperation) throws BadRequestException {
        log.debug("Handling transfer request: {}", exchangeOperation);
        AccountDbo fromAccount = accountCrudRepository.findById(exchangeOperation.getFromAccount())
            .orElseThrow(() -> new BadRequestException("Specified fromAccount invalid."));
        AccountDbo toAccount = accountCrudRepository.findById(exchangeOperation.getToAccount())
            .orElseThrow(() -> new BadRequestException("Specified toAccount invalid."));

        BigDecimal fromAccountBalance = fromAccount.getBalance();
        log.debug("Checking balance of account {} to deduct", fromAccount, exchangeOperation.getAmount());

        if (fromAccountBalance.compareTo(exchangeOperation.getAmount()) < 0) {
            throw new BadRequestException("Insufficient funds.");
        }
        updateFunds(fromAccount, toAccount, exchangeOperation.getAmount());
    }

    private void updateFunds(AccountDbo fromAccount, AccountDbo toAccount, BigDecimal amount) {
        log.debug("Transferring {} funds  {} -> {}", amount, fromAccount, toAccount);
        BigDecimal updatedFromBalance = fromAccount.getBalance().subtract(amount);
        BigDecimal updatedToBalance = toAccount.getBalance().add(amount);
        fromAccount.setBalance(updatedFromBalance);
        toAccount.setBalance(updatedToBalance);
        accountCrudRepository.save(fromAccount);
        accountCrudRepository.save(toAccount);
        log.debug("Finished saving funds {} funds  {} -> {}", amount, fromAccount, toAccount);
    }
}
