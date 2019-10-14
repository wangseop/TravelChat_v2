package com.hoingmarry.travelchat.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hoingmarry.travelchat.chat.Chat;
import com.hoingmarry.travelchat.R;
import com.hoingmarry.travelchat.chat.ChatDouble;
import com.hoingmarry.travelchat.viewholder.MessageViewHolder;
import com.hoingmarry.travelchat.viewholder.TestViewHolder;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
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
            case MSG_TYPE_RIGHT: {
                View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
                viewHolder = new MessageViewHolder(view);
            }break;

            case MSG_TYPE_LEFT: {
                View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
                viewHolder = new MessageViewHolder(view);
            }break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        Log.d("Enter", "onBindViewHolder");
        Log.d("Holder", Integer.toString(holder.getItemViewType()));
        Chat chat = mChat.get(position);
        ((MessageViewHolder)holder).show_message.setText(chat.getMessage());

        switch(holder.getItemViewType())
        {
            // chatbot의 채팅 내용에만 profile 이미지 추가
           case MSG_TYPE_LEFT:
            {
                if(imageurl.equals("default")){
                    ((MessageViewHolder)holder).profile_image.setImageResource(R.mipmap.ic_launcher);

                }else{
                    Glide.with(mContext).load(imageurl).into(((MessageViewHolder)holder).profile_image);
                }
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
        if(mChat.get(position).getSender().equals(this.myNickName)){
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }
    public void addChat(Chat chat){
        Log.d("Enter", "addChat");

        mChat.add(chat);
        notifyItemInserted(mChat.size()-1);      // 갱신
    }

}
