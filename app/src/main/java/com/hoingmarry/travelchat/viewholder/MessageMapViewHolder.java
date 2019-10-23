package com.hoingmarry.travelchat.viewholder;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hoingmarry.travelchat.R;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;

public class MessageMapViewHolder extends RecyclerView.ViewHolder {
    public TextView show_message;
    public ImageView profile_image;
    public TextView nick;
    public MapView mapView;

    public MessageMapViewHolder(View itemView){
        super(itemView);
        nick = itemView.findViewById(R.id.nick);
        show_message = itemView.findViewById(R.id.show_message);
        profile_image = itemView.findViewById(R.id.profile_image);

        mapView = itemView.findViewById(R.id.message_map).findViewById(R.id.map);
    }

}
