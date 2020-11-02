package com.indiaactive.vehicle.datamodels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RootMessage {
    @SerializedName("ErrorCode")
    public String errorCode;
    @SerializedName("ErrorMessage")
    public String errorMessage;
    @SerializedName("JobId")
    public String jobId;
    @SerializedName("MessageData")
    public List<MessageData> messageData;

    public RootMessage(){}

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getJobId() {
        return jobId;
    }

    public List<MessageData> getMessageData() {
        return messageData;
    }
}
