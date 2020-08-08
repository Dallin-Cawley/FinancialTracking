package com.financial.transactiontracking;

import java.io.IOException;

public class PlaidServerConnect {
    HomeSocket homeSocket;

    PlaidServerConnect() {
        try {
            this.homeSocket = new HomeSocket("10.0.2.2", 10000);
        }
        catch (IOException exception) {
            System.out.println("Attempted to create HomeSocket of PlaidBalanceRunnable");
            System.out.println("IOException:\n" + exception.getMessage());
        }
    }
}
