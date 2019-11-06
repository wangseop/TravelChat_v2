package com.hoingmarry.travelchat.data.chat;

public class ImageThumbChat extends ImageChat {
    private String imageComment;

    public ImageThumbChat(int msgType, String sender, String receiver, String message,
                          String imageUrl, String placeLink, String imageComment) {
        super(msgType, sender, receiver, message, imageUrl, placeLink);
        this.imageComment = imageComment;
    }

    public String getImageComment() {
        return imageComment;
    }

    public void setImageComment(String imageComment) {
        this.imageComment = imageComment;
    }
}
