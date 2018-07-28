package nl.butads.challenge.transferservice.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import nl.butads.challenge.transferservice.model.api.Account;
import nl.butads.challenge.transferservice.model.api.BadRequestException;
import nl.butads.challenge.transferservice.model.api.NotFoundException;
import nl.butads.challenge.transferservice.model.dbo.AccountDbo;
import nl.butads.challenge.transferservice.repository.AccountCrudRepository;
import nl.butads.challenge.transferservice.repository.AccountReadonlyRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Example;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private AccountCrudRepository accountCrudRepository;

    @Mock
    private AccountReadonlyRepository accountReadonlyRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void testConvertsAllAccounts() {
        AccountDbo accountDbo = AccountDbo.builder().id("1").name("Test").balance(BigDecimal.ONE).build();
        when(accountReadonlyRepository.findAll()).thenReturn(
            Collections.singletonList(accountDbo));

        List<Account> allAccounts = accountService.getAllAccounts();

        assertThat(allAccounts, hasSize(1));
        assertThat(allAccounts.get(0).getName(), is(accountDbo.getName()));
        assertThat(allAccounts.get(0).getId(), is(accountDbo.getId()));
        assertThat(allAccounts.get(0).getBalance(), is(accountDbo.getBalance()));

        verifyZeroInteractions(accountCrudRepository);
    }

    @Test
    public void testGetAccountByIdConvertsObject() throws NotFoundException {
        AccountDbo accountDbo = AccountDbo.builder().id("1").name("Test").balance(BigDecimal.ONE).build();
        when(accountReadonlyRepository.findById("1")).thenReturn(Optional.of(accountDbo));

        Account accountById = accountService.getAccountById("1");

        assertThat(accountById.getName(), is(accountDbo.getName()));
        assertThat(accountById.getId(), is(accountDbo.getId()));
        assertThat(accountById.getBalance(), is(accountDbo.getBalance()));

        verifyZeroInteractions(accountCrudRepository);
    }

    @Test
    public void testGetAccountByIdThrowsExceptionWhenNotFound() throws NotFoundException {
        when(accountReadonlyRepository.findById("1")).thenReturn(Optional.empty());

        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage("Account with id [1] does not exist");

        accountService.getAccountById("1");
    }

    @Test
    public void testCreateAccountHappyFlow() throws BadRequestException {
        Account accountCreateRequest = Account.builder().id("1").name("Test").balance(BigDecimal.ONE).build();

        when(accountCrudRepository.save(argThat(argument -> argument.getName().equals("Test"))))
            .then(returnsFirstArg());

        Account createdAccount = accountService.createAccount(accountCreateRequest);

        assertThat(createdAccount.getId(), is(not(accountCreateRequest.getId())));
        assertThat(createdAccount.getName(), is(accountCreateRequest.getName()));
        assertThat(createdAccount.getBalance(), is(accountCreateRequest.getBalance()));

        verifyZeroInteractions(accountReadonlyRepository);
    }

    @Test
    public void testCreateAccountAlreadyExistIsChecked() throws BadRequestException {
        Account accountCreateRequest = Account.builder().id("1").name("Test").balance(BigDecimal.ONE).build();

        when(accountCrudRepository.findAll(any(Example.class)))
            .thenReturn(Collections.singletonList(AccountDbo.builder().name("Test").build()));

        expectedException.expect(BadRequestException.class);
        expectedException.expectMessage("Account with name [Test] already exists");

        accountService.createAccount(accountCreateRequest);
    }
}