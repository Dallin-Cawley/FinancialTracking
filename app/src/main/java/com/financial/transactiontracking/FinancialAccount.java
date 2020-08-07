package com.financial.transactiontracking;

import com.plaid.link.result.LinkAccount;
import java.io.Serializable;

public class FinancialAccount implements Serializable {
    String accountId;
    String accountName;
    String accountNumber;
    String accountType;
    String accountSubType;

    FinancialAccount(LinkAccount account) {
        this.accountId = account.accountId;
        this.accountName = account.accountName;
        this.accountNumber = account.accountNumber;
        this.accountType = account.accountType;
        this.accountSubType = account.accountSubType;
    }
}
