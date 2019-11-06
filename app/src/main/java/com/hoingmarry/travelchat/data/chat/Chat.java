package com.hoingmarry.travelchat.data.chat;

public class Chat {
    protected int msgType;
    protected String sender;
    protected String receiver;
    protected String message;

    public Chat(int msgType, String sender, String receiver, String message) {
        this.msgType = msgType;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
