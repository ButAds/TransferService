package nl.butads.challenge.transferservice.api.impl;

import static nl.butads.challenge.transferservice.testutil.TestHelpers.asJsonString;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Collections;
import nl.butads.challenge.transferservice.model.api.Account;
import nl.butads.challenge.transferservice.model.api.BadRequestException;
import nl.butads.challenge.transferservice.model.api.NotFoundException;
import nl.butads.challenge.transferservice.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountsApiController.class)
public class AccountsApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountService service;

    @Test
    public void testAddAccountReturnsServiceResult() throws Exception {
        Account account = Account.builder().name("test-name").balance(BigDecimal.TEN).build();

        when(service.createAccount(argThat(createCall -> createCall.getName().equals("test-name"))))
            .thenReturn(Account.builder().id("test-id").name(account.getName()).balance(account.getBalance()).build());

        mvc.perform(post("/accounts")
            .content(asJsonString(account))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id", is("test-id")))
            .andExpect(jsonPath("name", is(account.getName())))
            .andExpect(jsonPath("balance", is(account.getBalance().intValueExact())));
    }

    @Test
    public void testAddAccountHandlesBadRequestFromService() throws Exception {
        Account account = Account.builder().name("test-name").balance(BigDecimal.TEN).build();

        when(service.createAccount(argThat(createCall -> createCall.getName().equals("test-name"))))
            .thenThrow(new BadRequestException("test-name already exists."));

        mvc.perform(post("/accounts")
            .content(asJsonString(account))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddAccountHandlesMalformedRequest() throws Exception {
        Account account = Account.builder().name(null).balance(null).build();

        mvc.perform(post("/accounts")
            .content(asJsonString(account))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verifyZeroInteractions(service);
    }

    @Test
    public void testGetAccountByIdReturnsServiceResults() throws Exception {
        String testId = "test-id";
        Account account = Account.builder().id(testId).name("test-name").balance(BigDecimal.TEN).build();
        when(service.getAccountById(testId)).thenReturn(account);

        mvc.perform(get("/accounts/" + "test-id")
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("id", is(account.getId())))
            .andExpect(jsonPath("name", is(account.getName())))
            .andExpect(jsonPath("balance", is(account.getBalance().intValueExact())));
    }

    @Test
    public void testGetAccountByIdHandlesNotFound() throws Exception {
        when(service.getAccountById(any()))
            .thenThrow(new NotFoundException("Account with id [test-id] does not exist."));

        mvc.perform(get("/accounts/" + "test-id")
            .contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAccountsReturnsServiceResults() throws Exception {
        Account account = Account.builder().id("test-id").name("test-name").balance(BigDecimal.TEN).build();
        when(service.getAllAccounts()).thenReturn(Collections.singletonList(account));

        mvc.perform(get("/accounts")
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("accounts", hasSize(1)))
            .andExpect(jsonPath("accounts[0].id", is(account.getId())))
            .andExpect(jsonPath("accounts[0].name", is(account.getName())))
            .andExpect(jsonPath("accounts[0].balance", is(account.getBalance().intValueExact())));
    }
}