package com.hoingmarry.travelchat.chat;

public class MapChat extends ImageChat {
    private double latitude;
    private double longitude;

    public MapChat(int msgType, String sender, String receiver, String message,
                   String imageUrl, String placeLink, double latitude, double longitude) {
        super(msgType, sender, receiver, message, imageUrl, placeLink);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
