package com.financial.transactiontracking;

import android.app.Application;
import com.plaid.link.Plaid;

public class PlaidLinkApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Plaid.initialize(this);
    }
}
