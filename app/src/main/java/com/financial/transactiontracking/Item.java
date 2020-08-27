package com.financial.transactiontracking;

import com.google.gson.Gson;
import com.plaid.link.result.LinkAccount;
import com.plaid.link.result.LinkSuccess;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Item implements Serializable {
    private ItemCredentials credentials;
    String institutionId;
    String institutionName;
    String linkSessionId;
    List<FinancialAccount> accounts;
    ArrayList<FinancialAccount> preferredAccounts;

    public Item(LinkSuccess linkSuccess) throws Exception {
        this.accounts = new ArrayList<>();
        this.institutionName = linkSuccess.metadata.institutionName;
        this.institutionId = linkSuccess.metadata.institutionId;
        this.linkSessionId = linkSuccess.metadata.linkSessionId;
        this.preferredAccounts = new ArrayList<>();

        for (LinkAccount account : linkSuccess.metadata.accounts) {
            System.out.println("Account subType: " + account.accountSubType);
            FinancialAccount newAccount = new FinancialAccount(account);
            this.accounts.add(newAccount);

            if (account.accountSubType == null) {
                continue;
            }
            if (account.accountSubType.equals("checking")) {
                preferredAccounts.add(newAccount);
            }
            if (account.accountSubType.equals("savings")) {
                preferredAccounts.add(newAccount);
            }
        }
    }

    public String getAccessToken() {
        return credentials.accessToken;
    }

    public void setItemCredentials(ItemCredentials credentials) {
        this.credentials = credentials;
    }

    private void setAccountBalances(String balances) {
        PlaidBalanceResponseObject plaidBalanceResponseObject =  new Gson().fromJson(balances, PlaidBalanceResponseObject.class);
        ArrayList<Account> accountResponse = plaidBalanceResponseObject.accounts;

        for (Account account : accountResponse) {
            for (int i = 0; i < accounts.size(); i++) {
                if (account.accountId.equals(accounts.get(i).accountId)) {
                    System.out.println("Found account");
                    accounts.get(i).addBalance(account);
                    break;
                }
            }
        }
    }


    public void handleCompletionState(CompletionState completionState, String object) throws IOException {
        switch (completionState){
            case PLAID_BALANCE_SUCCESS:
                setAccountBalances(object);
                PlaidHandler.getPlaidHandlerInstance().handleState(this, completionState);
                break;
            default:
                PlaidHandler.getPlaidHandlerInstance().handleState(this, completionState);
        }
    }
}
