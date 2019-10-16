package com.hoingmarry.travelchat.chat;

public class ImageChat extends Chat {
    private String imageUrl;

    public ImageChat(int msgType, String sender, String receiver, String message, String imageUrl) {
        super(msgType, sender, receiver, message);
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
