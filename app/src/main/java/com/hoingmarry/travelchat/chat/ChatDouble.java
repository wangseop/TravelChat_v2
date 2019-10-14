package com.hoingmarry.travelchat.chat;

public class ChatDouble extends Chat{
    private String message2;

    public ChatDouble(String sender, String receiver, String message, String message2) {
        super(sender, receiver, message);
        this.message2 = message2;
    }

    public String getMessage2() {
        return this.message2;
    }

    public void setMessage2(String message2) {
        this.message2 = message2;
    }
}
