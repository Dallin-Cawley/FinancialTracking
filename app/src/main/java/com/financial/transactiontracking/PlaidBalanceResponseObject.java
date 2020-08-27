package com.financial.transactiontracking;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PlaidBalanceResponseObject {
    @SerializedName("accounts")
    ArrayList<Account> accounts;
}
