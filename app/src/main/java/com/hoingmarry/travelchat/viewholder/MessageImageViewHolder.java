package com.hoingmarry.travelchat.viewholder;

import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hoingmarry.travelchat.R;

public class MessageImageViewHolder extends RecyclerView.ViewHolder {
    public TextView show_message;
    public ImageView profile_image;
    public TextView nick;
    public ImageView show_image;

    public MessageImageViewHolder(View itemView){
        super(itemView);
        nick = itemView.findViewById(R.id.nick);
        show_message = itemView.findViewById(R.id.show_message);
        profile_image = itemView.findViewById(R.id.profile_image);
        show_image = itemView.findViewById(R.id.show_image);

    }
}
