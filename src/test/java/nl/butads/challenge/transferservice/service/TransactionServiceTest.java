package nl.butads.challenge.transferservice.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import nl.butads.challenge.transferservice.model.api.AccountExchange;
import nl.butads.challenge.transferservice.model.api.BadRequestException;
import nl.butads.challenge.transferservice.model.dbo.AccountDbo;
import nl.butads.challenge.transferservice.repository.AccountCrudRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private AccountCrudRepository accountCrudRepository;

    @InjectMocks
    private TransactionService transactionService;

    private AccountDbo fromAccount;
    private AccountDbo toAccount;

    @Test
    public void testTransferFundsHappyPath() throws BadRequestException {
        prepareSearchResult(BigDecimal.TEN, BigDecimal.TEN);
        AccountExchange accountExchange = AccountExchange.builder()
            .fromAccount(fromAccount.getId())
            .toAccount(toAccount.getId())
            .amount(BigDecimal.ONE).build();

        transactionService.transferFunds(accountExchange);

        assertThat(fromAccount.getBalance(), is(BigDecimal.valueOf(9)));
        assertThat(toAccount.getBalance(), is(BigDecimal.valueOf(11)));

        verify(accountCrudRepository).save(fromAccount);
        verify(accountCrudRepository).save(toAccount);
    }

    @Test
    public void testTransferFundsInsufficientFunds() throws BadRequestException {
        prepareSearchResult(BigDecimal.ONE, BigDecimal.TEN);
        AccountExchange accountExchange = AccountExchange.builder()
            .fromAccount(fromAccount.getId())
            .toAccount(toAccount.getId())
            .amount(BigDecimal.TEN).build();

        expectedException.expect(BadRequestException.class);
        expectedException.expectMessage("Insufficient funds");

        transactionService.transferFunds(accountExchange);
    }

    @Test
    public void testFromAccountNotFound() throws BadRequestException {
        prepareSearchResult(BigDecimal.ONE, BigDecimal.TEN);
        AccountExchange accountExchange = AccountExchange.builder()
            .fromAccount(fromAccount.getId())
            .toAccount(toAccount.getId())
            .amount(BigDecimal.TEN).build();

        when(accountCrudRepository.findById("1")).thenReturn(Optional.empty());

        expectedException.expect(BadRequestException.class);
        expectedException.expectMessage("Specified fromAccount invalid");

        transactionService.transferFunds(accountExchange);
    }

    @Test
    public void testToAccountNotFound() throws BadRequestException {
        prepareSearchResult(BigDecimal.ONE, BigDecimal.TEN);
        AccountExchange accountExchange = AccountExchange.builder()
            .fromAccount(fromAccount.getId())
            .toAccount(toAccount.getId())
            .amount(BigDecimal.TEN).build();

        when(accountCrudRepository.findById("2")).thenReturn(Optional.empty());

        expectedException.expect(BadRequestException.class);
        expectedException.expectMessage("Specified toAccount invalid");

        transactionService.transferFunds(accountExchange);
    }

    private void prepareSearchResult(BigDecimal from, BigDecimal to) {
        fromAccount = AccountDbo.builder().id("1").balance(from).build();
        toAccount = AccountDbo.builder().id("2").balance(to).build();
        when(accountCrudRepository.findById("1")).thenReturn(Optional.of(fromAccount));
        when(accountCrudRepository.findById("2")).thenReturn(Optional.of(toAccount));
    }
}