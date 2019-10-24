package com.hoingmarry.travelchat.chat;

public class ImageThumnChat extends ImageChat {
    private String imageComment;

    public ImageThumnChat(int msgType, String sender, String receiver,
                          String message, String imageUrl, String imageComment) {
        super(msgType, sender, receiver, message, imageUrl);
        this.imageComment = imageComment;
    }

    public String getImageComment() {
        return imageComment;
    }

    public void setImageComment(String imageComment) {
        this.imageComment = imageComment;
    }
}
