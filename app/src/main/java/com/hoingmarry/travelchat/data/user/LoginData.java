package com.hoingmarry.travelchat.data.user;

public class LoginData {
    private String id;
    private String password;
    private boolean autoChecked;

    public LoginData(String id, String password, boolean autoChecked) {
        this.id = id;
        this.password = password;
        this.autoChecked = autoChecked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAutoChecked() {
        return autoChecked;
    }

    public void setAutoChecked(boolean autoChecked) {
        this.autoChecked = autoChecked;
    }
}
