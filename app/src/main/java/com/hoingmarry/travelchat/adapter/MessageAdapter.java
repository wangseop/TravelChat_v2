package com.hoingmarry.travelchat.adapter;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hoingmarry.travelchat.activity.MapSearchActivity;
import com.hoingmarry.travelchat.activity.PlaceLinkActivity;
import com.hoingmarry.travelchat.data.chat.Chat;
import com.hoingmarry.travelchat.R;
import com.hoingmarry.travelchat.data.chat.ImageChat;
import com.hoingmarry.travelchat.data.chat.ImageThumbChat;
import com.hoingmarry.travelchat.data.chat.MapChat;
import com.hoingmarry.travelchat.viewholder.ImageViewHolder;
import com.hoingmarry.travelchat.viewholder.MessageImageThumbViewHolder;
import com.hoingmarry.travelchat.viewholder.MessageImageViewHolder;
import com.hoingmarry.travelchat.viewholder.MessageMapViewHolder;
import com.hoingmarry.travelchat.viewholder.MessageViewHolder;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.LocationOverlay;

import java.util.List;

import static com.hoingmarry.travelchat.contracts.StringContract.MessageType.*;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnMapReadyCallback {


    private Context mContext;
    private List<Chat> mChat;
    private String imageurl;
    private String myNickName;

    public MessageAdapter(Context mContext, List<Chat> mChat, String imageurl, String myNickName){
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageurl = imageurl;
        this.myNickName = myNickName;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Log.d("Enter", "onCreateViewHolder");
        RecyclerView.ViewHolder viewHolder = null;
        // viewType에 따라 생성할 layout, holder 선택
        switch(viewType)
        {
            case MSG_RIGHT: {
                View view = LayoutInflater.from(mContext).inflate(R.layout.message_single_layout_right, parent, false);
                viewHolder = new MessageViewHolder(view);
            }break;

            case MSG_LEFT: {
//                View view = LayoutInflater.from(mContext).inflate(R.layout.message_single_layout_left, parent, false);
//                viewHolder = new MessageViewHolder(view);
                View view = LayoutInflater.from(mContext).inflate(R.layout.message_single_layout_left, parent, false);
                viewHolder = new MessageViewHolder(view);


            }break;
            case IMG_RIGHT:{
                View view = LayoutInflater.from(mContext).inflate(R.layout.image_layout_right, parent, false);
                viewHolder = new ImageViewHolder(view);
            }break;
            case MSG_IMG_LEFT:{
                View view = LayoutInflater.from(mContext).inflate(R.layout.message_image_layout_left, parent, false);
                viewHolder = new MessageImageViewHolder(view);
            }break;
            case MSG_IMG_THUMB_LEFT:{
                View view = LayoutInflater.from(mContext).inflate(R.layout.message_imagethumb_layout_left, parent, false);
                viewHolder = new MessageImageThumbViewHolder(view);
            }break;
            case MSG_MAP_LEFT: {
//                View view = LayoutInflater.from(mContext).inflate(R.layout.message_single_layout_left, parent, false);
//                viewHolder = new MessageViewHolder(view);
                View view = LayoutInflater.from(mContext).inflate(R.layout.message_map_btn_layout_left, parent, false);
                viewHolder = new MessageMapViewHolder(view);

            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        Log.d("Enter", "onBindViewHolder");
        Log.d("Holder", Integer.toString(holder.getItemViewType()));
        Chat chat = mChat.get(position);
//        ((MessageViewHolder)holder).show_message.setText(chat.getMessage());
//        ((MessageViewHolder)holder).nick.setText(chat.getSender());
        switch(holder.getItemViewType())
        {

            case MSG_LEFT:
            {
                ((MessageViewHolder)holder).show_message.setText(chat.getMessage());
                ((MessageViewHolder)holder).nick.setText(chat.getSender());
                if(imageurl.equals("default")){
                    ((MessageViewHolder)holder).profile_image.setImageResource(R.drawable.tan);

                }else{
                    Glide.with(mContext).load(imageurl).into(((MessageMapViewHolder)holder).profile_image);
                }
            }break;
            case MSG_RIGHT:
            {
                ((MessageViewHolder)holder).show_message.setText(chat.getMessage());
                ((MessageViewHolder)holder).nick.setText(chat.getSender());
            }break;
            case IMG_RIGHT:
            {
                ((ImageViewHolder)holder).nick.setText(chat.getSender());
                ((ImageViewHolder)holder).show_image.setImageURI(Uri.parse(((ImageChat)chat).getImageUrl()));
                GradientDrawable drawable=
                        (GradientDrawable)mContext.getDrawable(R.drawable.thumbnail_rounding);
                ((ImageViewHolder)holder).show_image.setBackground(drawable);
                ((ImageViewHolder)holder).show_image.setClipToOutline(true);
            }break;
                // chatbot의 채팅 내용에만 profile 이미지 추가
            case MSG_IMG_LEFT:
            {
                // 줄임문 테스트
//                String message = chat.getMessage();
//                Log.d("Message", ""+message.length());
//                int length = message.length() > 200 ? 200 : message.length();
//
//                ((MessageImageViewHolder)holder).show_message.setText(message.substring(0, length) + " ...");
                ((MessageImageViewHolder)holder).show_message.setText(chat.getMessage());
                ((MessageImageViewHolder)holder).nick.setText(chat.getSender());

                if(imageurl.equals("default")){
                    ((MessageImageViewHolder)holder).profile_image.setImageResource(R.drawable.tan);

                }else{
                    Glide.with(mContext).load(imageurl).into(((MessageImageViewHolder)holder).profile_image);
                }
                Glide.with(mContext).load(((ImageChat)chat).getImageUrl()).
                        into(((MessageImageViewHolder)holder).show_image);
                GradientDrawable drawable=
                        (GradientDrawable)mContext.getDrawable(R.drawable.thumbnail_rounding);
                ((MessageImageViewHolder)holder).show_image.setBackground(drawable);
                ((MessageImageViewHolder)holder).show_image.setClipToOutline(true);

                final String url = ((ImageChat)chat).getPlaceLink();
                ((MessageImageViewHolder)holder).show_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (url == null) return;
                        Intent intent = new Intent(mContext, PlaceLinkActivity.class);
                        intent.putExtra("link", url);
                        mContext.startActivity(intent);
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                        mContext.startActivity(intent);
                    }
                });


            }break;
            case MSG_IMG_THUMB_LEFT:
            {
                ((MessageImageThumbViewHolder)holder).show_message.setText(chat.getMessage());
                ((MessageImageThumbViewHolder)holder).nick.setText(chat.getSender());

                if(imageurl.equals("default")){
                    ((MessageImageThumbViewHolder)holder).profile_image.setImageResource(R.drawable.tan);

                }else{
                    Glide.with(mContext).load(imageurl).into(((MessageImageThumbViewHolder)holder).profile_image);
                }

                Glide.with(mContext).load(((ImageChat)chat).getImageUrl()).
                        into(((MessageImageThumbViewHolder)holder).show_image);
//                GradientDrawable drawable=
//                        (GradientDrawable)mContext.getDrawable(R.drawable.thumbnail_rounding);
//                ((MessageImageThumbViewHolder)holder).show_image.setBackground(drawable);
                ((LinearLayout)((MessageImageThumbViewHolder)holder).show_image.getParent()).setClipToOutline(true);

                final String url = ((ImageChat)chat).getPlaceLink();
                ((MessageImageThumbViewHolder)holder).show_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        mContext.startActivity(intent);
                    }
                });

                // 이미지 코멘트
                ((MessageImageThumbViewHolder)holder).showImageComment.setText(((ImageThumbChat)chat).getImageComment());


            }break;
            case MSG_MAP_LEFT:
            {
                final double lat = ((MapChat)chat).getLatitude();
                final double lng = ((MapChat)chat).getLongitude();
                ((MessageMapViewHolder)holder).show_message.setText(chat.getMessage());
                ((MessageMapViewHolder)holder).nick.setText(chat.getSender());
                ((MessageMapViewHolder)holder).showMapBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, MapSearchActivity.class);
                        intent.putExtra("latitude", lat);
                        intent.putExtra("longitude", lng);
                        mContext.startActivity(intent);
                    }
                });
                if(imageurl.equals("default")){
                    ((MessageMapViewHolder)holder).profile_image.setImageResource(R.drawable.tan);

                }else {
                    Glide.with(mContext).load(imageurl).into(((MessageMapViewHolder) holder).profile_image);
                }

                Glide.with(mContext).load(((ImageChat)chat).getImageUrl()).
                        into(((MessageMapViewHolder)holder).show_image);
//                GradientDrawable drawable=
//                        (GradientDrawable)mContext.getDrawable(R.drawable.thumbnail_rounding);
//                ((MessageImageThumbViewHolder)holder).show_image.setBackground(drawable);
                ((LinearLayout)((MessageMapViewHolder)holder).show_image.getParent()).setClipToOutline(true);

                final String url = ((ImageChat)chat).getPlaceLink();
                ((MessageMapViewHolder)holder).show_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PlaceLinkActivity.class);
                        intent.putExtra("link", url);
                        mContext.startActivity(intent);
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                        mContext.startActivity(intent);
                    }
                });


            }break;
            default:
                break;
        }

    }

    @Override
    public int getItemCount(){
        return mChat.size();
    }



    @Override
    public int getItemViewType(int position) {
        // return 값이 추가하는 holder의 type 결정해준다.
        Log.d("Enter", "GetItemViewType");

        int type = mChat.get(position).getMsgType();
        Log.d("Enter", "GetItemViewType :" + type);

        return type;
//        if(mChat.get(position).getSender().equals(this.myNickName)){
//            return MSG_RIGHT;
//        }
//        else{
//            return MSG_LEFT;
//        }
    }
    public void addChat(Chat chat){
        Log.d("Enter", "addChat");

        mChat.add(chat);
        notifyItemInserted(mChat.size()-1);      // 갱신
    }
    double lat = 37.563996959956604;
    double lng = 127.11477513394202;
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        locationOverlay.setVisible(true);
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(
                new LatLng(lat, lng));
        naverMap.moveCamera(cameraUpdate);
        locationOverlay.setPosition(new LatLng(lat, lng));

    }
}
