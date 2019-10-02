package com.hoingmarry.travelchat;

public class ChatData{
    private String message;
    private String nickname;

    public ChatData()
    {
        this.message = "";
        this.nickname = "";
    }

    public ChatData(String message, String nickname)
    {
        this.message = message;
        this.nickname = nickname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


}
