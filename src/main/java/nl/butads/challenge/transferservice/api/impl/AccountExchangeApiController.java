package nl.butads.challenge.transferservice.api.impl;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import nl.butads.challenge.transferservice.api.AccountExchangeApi;
import nl.butads.challenge.transferservice.model.api.AccountExchange;
import nl.butads.challenge.transferservice.model.api.BadRequestException;
import nl.butads.challenge.transferservice.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Log4j2
@RequiredArgsConstructor
public class AccountExchangeApiController implements AccountExchangeApi {

    private final TransactionService transactionService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void processExchange(@Valid @RequestBody AccountExchange exchangeOperation) throws BadRequestException {
        log.debug("Got transfer request: {}", exchangeOperation);
        transactionService.transferFunds(exchangeOperation);
    }
}
