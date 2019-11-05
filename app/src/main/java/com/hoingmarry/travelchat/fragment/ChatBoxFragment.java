package com.hoingmarry.travelchat.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hoingmarry.travelchat.R;

public class ChatBoxFragment extends Fragment {

    public ChatBoxFragment(){
        Log.d("Call", "ChatBoxFragment Constructor");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("Call", "ChatBoxFragment Method : onCreateView");

        return inflater.inflate(R.layout.bottom_message_fragment, container, false);
    }
}


