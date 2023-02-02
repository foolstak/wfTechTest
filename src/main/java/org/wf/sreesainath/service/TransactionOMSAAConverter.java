package org.wf.sreesainath.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wf.sreesainath.domain.Transaction;
import org.wf.sreesainath.domain.output.OMSAAA;


import java.math.BigDecimal;

@Slf4j
@Service
public class TransactionOMSAAConverter implements ItemProcessor<Transaction, OMSAAA> {
//TODO: Move literal values to static string
//TODO: Move config bits to application.properties

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private SecurityService securityService;

    @Override
    public OMSAAA process(final Transaction transaction) throws Exception {
        if(transaction.getOms().equals("AAA")){
            final int securityId = transaction.getSecurityId();
            final int portfolioId = transaction.getPortfolioId();
            final BigDecimal nominal = transaction.getNominal();
            final String transactionType = transaction.getTransactionType();
            final String isin = securityService.getSecurity(securityId).getIsin();

            final OMSAAA omsaaa = new OMSAAA();
            omsaaa.setISIN(isin);
            omsaaa.setNominal(nominal);
            omsaaa.setTransactionType(transactionType);
            omsaaa.setPortfolioCode(portfolioId);
            log.info("Converting (" + transaction + ") into (" + omsaaa + ")");
            return omsaaa;
        }
            return null; // Process others in a separate step
    }
}