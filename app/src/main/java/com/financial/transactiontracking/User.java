package com.financial.transactiontracking;

import android.app.Activity;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class User implements Serializable {
    private String firstName;
    private String lastName;

    private List<Item> institutions;

    User() {
        firstName = "";
        lastName = "";
        institutions = new ArrayList<Item>();
    }

    public void addInstitution(Item newItem) {
        if (institutions.indexOf(newItem) < 0) {
            institutions.add(newItem);
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<Item> getInstitutions() {
        return institutions;
    }

    public void setFirstLastName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
