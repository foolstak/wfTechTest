package org.wf.sreesainath.service;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.wf.sreesainath.domain.Security;
import org.wf.sreesainath.domain.Transaction;

import java.math.BigDecimal;

@SpringBatchTest
public class TransactionOMSAAConverterTest {

    @InjectMocks
    private TransactionOMSAAConverter transactionOMSAAConverter;
    @Mock
    private SecurityService securityService;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
        Security security = new Security();
        security.setSecurityId(1);
        security.setIsin("CS123");
        Mockito.when(securityService.getSecurity(1)).thenReturn(security);
    }

    @Test
    public void whenTransactionTypeIsAAAThenConversionIsDone() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setTransactionType("BUY");
        transaction.setOms("AAA");
        transaction.setSecurityId(1);
        transaction.setNominal(new BigDecimal(12000000));
        transaction.setPortfolioId(1);

        Assertions.assertEquals(transactionOMSAAConverter.process(transaction).getISIN(), "CS123");
    }

}