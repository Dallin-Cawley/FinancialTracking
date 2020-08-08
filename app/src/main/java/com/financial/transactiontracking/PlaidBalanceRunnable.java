package com.financial.transactiontracking;

import java.io.IOException;
import java.util.HashMap;

public class PlaidBalanceRunnable extends PlaidServerConnect implements Runnable {
    Item item;
    PlaidBalanceRunnable(Item item) {
        super();
        this.item = item;
    }

    @Override
    public void run() {
        HashMap<String, Object> request = new HashMap<String, Object>();
        request.put("header", "plaid get balances");
        request.put("access_token", item.getAccessToken());

        try {
            homeSocket.send(request);

            String response = homeSocket.getResponse();
            System.out.println("Response:\n" + response);
        } catch (IOException e) {
            System.out.println("Attempted to get balances");
            System.out.println("IOException:\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}
