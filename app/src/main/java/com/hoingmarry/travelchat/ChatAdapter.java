package com.hoingmarry.travelchat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder>{

    private List<ChatData> mDataset;
    private String myNickName;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView  tv_nickname;
        public TextView tv_msg;
        public View rootView;
        public MyViewHolder(View v){
            super(v);
            Log.d("MyViewHolder Construct", "Enter");
            tv_nickname = v.findViewById(R.id.tv_nickname);
            tv_msg = v.findViewById(R.id.tv_msg);
            rootView = v;
        }
    }

    public ChatAdapter(List<ChatData> myDataset, Context context, String myNickName){
        Log.d("ChatAdapter Construct", "Enter");
        mDataset = myDataset;
        this.myNickName = myNickName;
    }
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 새로운 뷰 생성
        Log.d("onCreateViewHolder", "Enter");
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_chat, parent, false);

        ChatAdapter.MyViewHolder vh = new ChatAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ChatData chat = mDataset.get(position);

        // 내 채팅, 상대방 채팅 구분 필요
        Log.d("onBindViewHolder", "Enter");

        holder.tv_nickname.setText(chat.getNickname());
        holder.tv_msg.setText(chat.getMessage());

        if(chat.getNickname().equals(this.myNickName)){
            holder.tv_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.tv_nickname.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        }
        else{
            holder.tv_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.tv_nickname.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

    public ChatData getChat(int position){
        Log.d("ChatAdapter->getChat", "Enter");

        return mDataset != null ? mDataset.get(position) : null;
    }
    public void addChat(ChatData chat){
        Log.d("ChatAdapter->addChat", "Enter");

        mDataset.add(chat);
        notifyItemInserted(mDataset.size()-1);      // 갱신
    }

}
