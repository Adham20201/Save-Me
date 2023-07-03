package com.adham.saveme;

import java.util.Date;

public class ChatMessage {
    public String senderId, receiverID, message, dateTime;

    public Date dateObject;

    public ChatMessage() {
    }

    public ChatMessage(String senderId, String receiverID, String message, String dateTime) {
        this.senderId = senderId;
        this.receiverID = receiverID;
        this.message = message;
        this.dateTime = dateTime;
    }

    public ChatMessage(String senderId, String receiverID, String message, String dateTime, Date dateObject) {
        this.senderId = senderId;
        this.receiverID = receiverID;
        this.message = message;
        this.dateTime = dateTime;
        this.dateObject = dateObject;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Date getDateObject() {
        return dateObject;
    }

    public void setDateObject(Date dateObject) {
        this.dateObject = dateObject;
    }
}
