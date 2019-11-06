package com.hoingmarry.travelchat.data.chat;

public class ImageChat extends Chat {
    private String imageUrl;
    private String placeLink;

    public ImageChat(int msgType, String sender, String receiver, String message, String imageUrl, String placeLink) {
        super(msgType, sender, receiver, message);
        this.imageUrl = imageUrl;
        this.placeLink = placeLink;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPlaceLink() {
        return placeLink;
    }

    public void setPlaceLink(String placeLink) {
        this.placeLink = placeLink;
    }
}
