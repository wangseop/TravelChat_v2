package com.hoingmarry.travelchat.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hoingmarry.travelchat.R;

public class TestViewHolder extends RecyclerView.ViewHolder {
    public TextView show_message;
    public TextView show_message2;
    public ImageView profile_image;
    public TestViewHolder(@NonNull View itemView) {
        super(itemView);

        show_message = itemView.findViewById(R.id.show_message1);
        show_message2 = itemView.findViewById(R.id.show_message2);
        profile_image = itemView.findViewById(R.id.profile_image);
    }
}
