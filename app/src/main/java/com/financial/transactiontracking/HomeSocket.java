package com.financial.transactiontracking;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

import com.google.gson.Gson;

public class HomeSocket extends Socket {
    OutputStreamWriter outputStreamWriter;
    InputStreamReader inputStreamReader;
    Gson json;

    HomeSocket(String host, int port) throws IOException {
        super(host, port);

        outputStreamWriter = new OutputStreamWriter(getOutputStream());
        inputStreamReader = new InputStreamReader(getInputStream());
        json = new Gson();
    }

    void send(Object message) throws IOException {
        //Prepare message for sending
        String json_message = json.toJson(message);

        HashMap<String, Object> sizeBody = new HashMap<String, Object>();
        sizeBody.put("size", json_message.getBytes().length);
        sizeBody.put("buffer", "");

        //Make sure the sizeBody message is a fixed length of 50bytes
        try {
            String sizeBodyString = sizeBody.toString();
            String buffer = "";
            while (sizeBodyString.getBytes(StandardCharsets.UTF_8).length < 50) {
                buffer += " ";
                sizeBody.replace("buffer", buffer);
                sizeBodyString = json.toJson(sizeBody);
            }
        }
        catch (NullPointerException exception) {
            System.out.println("NullPointerException: " + exception.getMessage());
            System.out.println("Aborting");
        }

        //Send out both messages
        outputStreamWriter.write(json.toJson(sizeBody));
        outputStreamWriter.flush();
        outputStreamWriter.write(json_message);
        outputStreamWriter.flush();
    }

    String getResponse() throws IOException {

        //Receive the size of the message (The header message will be exactly 50bytes)
        char [] charBuffer = new char[50];
        int numRead = 0;
        while (numRead < 50) {
            numRead += inputStreamReader.read(charBuffer, 0, 50);
        }
        HomeSocketResponseSize sizeBody = json.fromJson(new String(charBuffer), HomeSocketResponseSize.class);


        //Read from the InputStream until the message is completely received
        char[] message = new char[sizeBody.size];
        numRead = 0;
        while (numRead < sizeBody.size) {
            numRead = inputStreamReader.read(message, 0, sizeBody.size);
        }

        return new String(message);
    }

    public static HomeSocket buildSocket(String ip, int port) throws IOException {
        return new HomeSocket(ip, port);
    }
}
