package com.hoingmarry.travelchat.utils;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

public class ViewUtil {
    public static <T extends View> T findById(@NonNull View parent, @IdRes int resId) {
        return (T) parent.findViewById(resId);
    }
}
