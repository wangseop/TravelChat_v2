package com.hoingmarry.travelchat.contracts;

public class StringContract {

    public static class MessageType{
        public static final int MSG_LEFT = 0;
        public static final int MSG_RIGHT = 1;
        public static final int IMG_LEFT = 2;
        public static final int IMG_RIGHT = 3;
        public static final int MSG_IMG_LEFT = 4;
        public static final int MSG_IMG_RIGHT = 5;
        public static final int MSG_IMG_THUMB_LEFT = 6;
        public static final int MSG_IMG_THUMB_RIGHT = 7;
        public static final int MSG_MAP_LEFT = 8;
        public static final int MSG_MAP_RIGHT = 9;
    }

    public static class ResultCode{
        public static final int LOGIN_OK = 1;
        public static final int LOGIN_FAIL = 2;
    }
}