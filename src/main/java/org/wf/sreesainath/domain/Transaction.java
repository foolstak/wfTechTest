package org.wf.sreesainath.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@NoArgsConstructor
@Setter
public class Transaction {
    private  int securityId;
    private  int portfolioId;
    private  BigDecimal nominal;
    private  String oms;
    private  String transactionType;
}
