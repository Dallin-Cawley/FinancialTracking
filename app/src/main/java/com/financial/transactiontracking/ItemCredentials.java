package com.financial.transactiontracking;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ItemCredentials implements Serializable {
    @SerializedName("access_token")
    String accessToken;

    @SerializedName("item_id")
    String itemId;

    @SerializedName("request_id")
    String requestId;

}
