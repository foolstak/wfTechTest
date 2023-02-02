package org.wf.sreesainath.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Security {
    private int securityId;
    private String isin;
    private String ticker;
    private String cusip;
}
