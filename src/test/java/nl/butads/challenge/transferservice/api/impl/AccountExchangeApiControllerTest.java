package nl.butads.challenge.transferservice.api.impl;

import static nl.butads.challenge.transferservice.testutil.TestHelpers.asJsonString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import nl.butads.challenge.transferservice.model.api.AccountExchange;
import nl.butads.challenge.transferservice.service.TransactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountExchangeApiController.class)
public class AccountExchangeApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TransactionService service;

    @Test
    public void testExchangeServiceIsInvoked() throws Exception {
        AccountExchange exchange = AccountExchange.builder()
            .fromAccount("43ecde6b-f3a8-4687-8d45-ef95f238df30")
            .toAccount("43ecde6b-f3a8-4687-8d45-ef95f238df30")
            .amount(BigDecimal.TEN)
            .build();

        mvc.perform(post("/accountexchange")
            .content(asJsonString(exchange))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent());

        verify(service).transferFunds(argThat(argument -> argument.equals(exchange)));
    }

    @Test
    public void testExchangeServiceValidatesRequestObject() throws Exception {
        AccountExchange exchange = AccountExchange.builder()
            .fromAccount("invalid-id")
            .toAccount("43ecde6b-f3a8-4687-8d45-ef95f238df30")
            .amount(BigDecimal.TEN)
            .build();

        mvc.perform(post("/accountexchange")
            .content(asJsonString(exchange))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verifyZeroInteractions(service);
    }
}