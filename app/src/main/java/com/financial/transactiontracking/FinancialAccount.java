package com.financial.transactiontracking;

import com.google.gson.annotations.SerializedName;
import com.plaid.link.result.LinkAccount;
import java.io.Serializable;

public class FinancialAccount implements Serializable {
    @SerializedName("accountId")
    String accountId;

    @SerializedName("accountName")
    String accountName;

    @SerializedName("accountNumber")
    String accountNumber;

    @SerializedName("accountType")
    String accountType;

    @SerializedName("accountSubType")
    String accountSubType;

    Balances balance;
    String accountOfficialName;
    String mask;

    FinancialAccount(LinkAccount account) {
        this.accountId = account.accountId;
        this.accountName = account.accountName;
        this.accountNumber = account.accountNumber;
        this.accountType = account.accountType;
        this.accountSubType = account.accountSubType;
    }

    public void addBalance (Account balance) {
        this.balance = balance.balances;
        this.mask = balance.mask;
        this.accountOfficialName = balance.accountOfficialName;
    }
}
