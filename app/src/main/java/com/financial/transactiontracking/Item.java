package com.financial.transactiontracking;

import com.plaid.link.result.LinkAccount;
import com.plaid.link.result.LinkSuccess;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Item implements Serializable {
    private ItemCredentials credentials;
    String institutionId;
    String institutionName;
    String linkSessionId;
    List<FinancialAccount> accounts;

    public Item(LinkSuccess linkSuccess) throws Exception {
        System.out.println("Creating Local Item Class.");
        this.accounts = new ArrayList<FinancialAccount>();
        this.institutionName = linkSuccess.metadata.institutionName;
        this.institutionId = linkSuccess.metadata.institutionId;
        this.linkSessionId = linkSuccess.metadata.linkSessionId;

        for (LinkAccount account : linkSuccess.metadata.accounts) {
            this.accounts.add(new FinancialAccount(account));
        }

    }

    public String getAccessToken() {
        return credentials.accessToken;
    }

    public void setItemCredentials(ItemCredentials credentials) {
        this.credentials = credentials;
    }

    public void handleCompletionState(CompletionState completionState) throws IOException {
        if (completionState == CompletionState.CREDENTIAL_SUCCESS) {
            PlaidHandler.getPlaidHandlerIntstance().handleState(this, completionState);
        }
    }
}
