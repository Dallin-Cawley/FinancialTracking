package com.financial.transactiontracking;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class HomeSocketRunnable implements Runnable {
    HomeSocket socket;
    String publicToken;
    Item item;

    HomeSocketRunnable(String publicToken, Item item) {
        this.publicToken = publicToken;
        this.item = item;
    }

    @Override
    public void run() {
        System.out.println("Connecting to localhost");
        try {
            socket = new HomeSocket("10.0.2.2", 10000);
            socket.getResponse();

            HashMap<String, Object> requestBody = new HashMap<String, Object>();
            requestBody.put("header", "exchange public token");
            requestBody.put("public_token", publicToken);

            socket.send(requestBody);

            ItemCredentials itemCredentials = new Gson().fromJson(socket.getResponse(),
                    ItemCredentials.class);

            //Passes the ItemCredentials up the hierarchy.
            item.setItemCredentials(itemCredentials);

            //Passes the Completion State up the hierarchy.
            item.handleCompletionState(CompletionState.CREDENTIAL_SUCCESS);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
