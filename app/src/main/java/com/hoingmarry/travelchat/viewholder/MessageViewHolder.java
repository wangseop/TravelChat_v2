package com.hoingmarry.travelchat.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hoingmarry.travelchat.R;

public class MessageViewHolder extends RecyclerView.ViewHolder{
    public TextView show_message;
    public ImageView profile_image;

    public MessageViewHolder(View itemView){
        super(itemView);

        show_message = itemView.findViewById(R.id.show_message);
        profile_image = itemView.findViewById(R.id.profile_image);
    }
}