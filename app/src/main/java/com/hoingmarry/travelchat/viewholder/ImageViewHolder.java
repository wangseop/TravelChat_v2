package com.hoingmarry.travelchat.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hoingmarry.travelchat.R;

public class ImageViewHolder extends RecyclerView.ViewHolder {
    public TextView nick;
    public ImageView show_image;

    public ImageViewHolder(View itemView){
        super(itemView);
        nick = itemView.findViewById(R.id.nick);
        show_image = itemView.findViewById(R.id.show_image);

    }
}
