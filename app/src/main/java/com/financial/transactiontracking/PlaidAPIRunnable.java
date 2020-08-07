package com.financial.transactiontracking;

import java.net.HttpURLConnection;

public class PlaidAPIRunnable implements Runnable {
    PlaidAPIConnection plaidAPIConnection;


    PlaidAPIRunnable(PlaidAPIConnection connection) {
        this.plaidAPIConnection = connection;
    }

    @Override
    public void run() {
        plaidAPIConnection.getItemBalance("/accounts/balance/get");
    }
}
