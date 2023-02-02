package org.wf.sreesainath.domain.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class OMSAAA {
    private String iSIN;
    private int portfolioCode;
    private BigDecimal nominal;
    private String transactionType;
}
