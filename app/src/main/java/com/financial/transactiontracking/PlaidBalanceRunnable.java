package com.financial.transactiontracking;

import java.io.IOException;
import java.util.HashMap;

public class PlaidBalanceRunnable implements Runnable {
    Item item;
    PlaidBalanceRunnable(Item item) {
        this.item = item;
    }

    @Override
    public void run() {
        HashMap<String, Object> request = new HashMap<String, Object>();
        request.put("header", "plaid get balances");
        request.put("access_token", item.getAccessToken());

        try {
            HomeSocket homeSocket = HomeSocket.buildSocket("10.0.2.2", 10000);
//            HomeSocket homeSocket = HomeSocket.buildSocket("73.6.148.194", 10000);
            homeSocket.send(request);

            String connectionResponse = homeSocket.getResponse();
            String response = homeSocket.getResponse();
            System.out.println("Response:\n" + response);
            item.handleCompletionState(CompletionState.PLAID_BALANCE_SUCCESS, response);
        } catch (IOException e) {
            System.out.println("Attempted to get balances");
            System.out.println("IOException:\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}
