package com.laioffer.usedbook.Adapaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.laioffer.usedbook.Entity.ChatList;
import com.laioffer.usedbook.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<ChatList> mChat;
    private String imagurl;
    private String currentImageUrl;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context mContext, List<ChatList> mChat, String imagurl, String currentImageUrl) {
        this.mChat = mChat;
        this.mContext = mContext;
        this.imagurl = imagurl;
        this.currentImageUrl = currentImageUrl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //get left or right result
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_itemright, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_itemleft, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        ChatList chatList = mChat.get(position);

        holder.show_msg.setText(chatList.getMessage());

        if (imagurl.equals("default")) {
            holder.profile.setImageResource(R.mipmap.ic_launcher);
        }
        else {
            if (mChat.get(position).getSender().equals(firebaseUser.getUid())) {
                Glide.with(mContext).
                        load(currentImageUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(holder.profile);
            }
            else {
                Glide.with(mContext).load(imagurl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(holder.profile);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_msg ;
        public ImageView profile;


        public ViewHolder(View itemView) {
            super(itemView);

            show_msg = itemView.findViewById(R.id.show_message);
            profile = itemView.findViewById(R.id.profile);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }
}
