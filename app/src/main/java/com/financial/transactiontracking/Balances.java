package com.financial.transactiontracking;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Balances implements Serializable {

    @SerializedName("available")
    float availableBalance;

    @SerializedName("current")
    float currentBalance;

    @SerializedName("iso_currency_code")
    String isoCurrencyCode;

    @SerializedName("limit")
    String limit;

    @SerializedName("unofficial_currency_code")
    String unofficialCurrencyCode;
}
