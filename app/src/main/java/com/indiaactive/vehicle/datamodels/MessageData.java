package com.indiaactive.vehicle.datamodels;

import com.google.gson.annotations.SerializedName;

public class MessageData {

    @SerializedName("Number")
    public String number;
    @SerializedName("MessageId")
    public String messageId;

    public MessageData(){}

    public String getNumber() {
        return number;
    }

    public String getMessageId() {
        return messageId;
    }
}
