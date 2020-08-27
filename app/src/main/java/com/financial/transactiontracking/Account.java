package com.financial.transactiontracking;

import com.google.gson.annotations.SerializedName;

public class Account {

    @SerializedName("account_id")
    String accountId;

    @SerializedName("balances")
    Balances balances;

    @SerializedName("mask")
    String mask;

    @SerializedName("name")
    String accountName;

    @SerializedName("official_name")
    String accountOfficialName;

    @SerializedName("subtype")
    String accountSubType;

    @SerializedName("type")
    String accountType;
}
