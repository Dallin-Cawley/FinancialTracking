package com.financial.transactiontracking;

import android.os.Looper;
import android.os.Handler;
import android.os.Message;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PlaidHandler {
    private static PlaidHandler plaidHandlerInstance = null;
    final BlockingQueue<Runnable> threadQueue;
    private final ThreadPoolExecutor threadPoolExecutor;

    private static final int CORE_POOL_SIZE = 1;
    private static final int MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final int KEEP_ALIVE_TIME = 1;

    private final Handler plaidHandler;
    private FinancialHomeActivity financialHomeActivity = null;


    static {
        plaidHandlerInstance = new PlaidHandler();
    }


    private PlaidHandler() {
        plaidHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NotNull Message message) {
                switch (message.what) {
                    case 1: //CompletionState.CREDENTIAL_SUCCESS
                        Item item = (Item) message.obj;
                        financialHomeActivity.addItem(item);
                    case 2:
                        financialHomeActivity.printPlaidResult((String) message.obj);
                }

            }
        };
        threadQueue = new LinkedBlockingQueue<Runnable>();
        threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, threadQueue);
    }


    public void executeTask(Runnable runnable, FinancialHomeActivity activity) {
        financialHomeActivity = activity;
        threadPoolExecutor.execute(runnable);
    }

    public void executeTask(Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }

    public static PlaidHandler getPlaidHandlerInstance() {
        return plaidHandlerInstance;
    }

    public void handleState (Object object, CompletionState completionState) throws IOException {
        switch (completionState) {
            case CREDENTIAL_SUCCESS:
                Item item = (Item) object;
                plaidHandler.obtainMessage(1, item).sendToTarget();
            case PLAID_SUCCESS:
                plaidHandler.obtainMessage(2, object).sendToTarget();

        }
    }


}
