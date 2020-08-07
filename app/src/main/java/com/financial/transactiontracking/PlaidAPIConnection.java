package com.financial.transactiontracking;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;


public class PlaidAPIConnection {
    private final String baseURL = "https://sandbox.plaid.com";
    HttpsURLConnection connection;
    String response;


    PlaidAPIConnection() {
        this.connection = null;
    }

    private void establishConnection(String url) {
        try {
            connection = (HttpsURLConnection) new URL(baseURL + url).openConnection();
        }
        catch (MalformedURLException exception) {
            System.out.println("MalformedURLException:\n" + exception.toString());
        }
        catch (IOException exception) {
            System.out.println("IOException: \n" + exception.toString());
            System.out.println("New URL Creation");
            System.out.println(Arrays.toString(exception.getStackTrace()));
        }
    }

    private InputStreamReader getConnectionReader() {
        try {
            return new InputStreamReader(connection.getInputStream());
        }
        catch (IOException exception) {
            System.out.println("IOException: \n" + exception.toString());
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getErrorStream());
            StringBuilder stringBuilder = new StringBuilder();
            int charInt = 0;

            try {
                while ((charInt = inputStreamReader.read()) != -1) {
                    stringBuilder.append((char) charInt);
                }
            }
            catch (IOException except) {
                System.out.println("IOException reading connection Error Stream:\n" + except.getMessage());
            }

            System.out.println(stringBuilder.toString());
        }
        return null;
    }

    private void getResponse() {
        InputStreamReader inputStreamReader = getConnectionReader();

        if (inputStreamReader == null) {
            return;
        }

        //Used to convert buffer to string
        StringBuilder builder = new StringBuilder();
        int charInt = 0;

        //While there are lines in our connection_response, assign it to our string variable
        //and insert it into your StringBuffer.
        try {
            while ((charInt = inputStreamReader.read()) != -1) {
                builder.append((char) charInt);
            }
        }
        catch (IOException exception) {
            System.out.println("IOException:\n" + exception.toString());
            System.out.println(Arrays.toString(exception.getStackTrace()));
        }

        response = builder.toString();
    }

    public void getItemBalance(String endpointURL) {
        establishConnection(endpointURL);
        getResponse();
    }

    public int getResponseCode() {
        try {
            return connection.getResponseCode();
        }
        catch (IOException exception) {
            System.out.println("IOException:\n" + exception.toString());
        }
        return 0;
    }

    private void handleState(CompletionState completionState) throws IOException {
        if (completionState == CompletionState.PLAID_SUCCESS) {
            PlaidHandler.getPlaidHandlerIntstance().handleState(response, completionState);
        }
        else {
            System.out.println("Response Code: " + getResponseCode());
        }
    }
}
