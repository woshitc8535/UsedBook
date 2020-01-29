package com.laioffer.usedbook.Adapaters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.laioffer.usedbook.Entity.User;
import com.laioffer.usedbook.MessageActivity;
import com.laioffer.usedbook.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private boolean isChat;

    public UserAdapter(Context mContext, List<User> mUsers, boolean isChat) {
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {

        final User user = mUsers.get(position);
        holder.username.setText(user.getUsername());
        if (user.getImageURL().equals("default")) {
            holder.profile.setImageResource(R.mipmap.ic_launcher);
        }
        else {
            Glide.with(mContext).load(user.getImageURL())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.profile);
        }


        if(isChat) {
            if (user.getStatus().equals("online")) {
                holder.online.setVisibility(View.VISIBLE);
                holder.offline.setVisibility(View.GONE);
            }
            else {
                holder.online.setVisibility(View.GONE);
                holder.offline.setVisibility((View.GONE));
            }
        }
        else {
            holder.online.setVisibility(View.GONE);
            holder.offline.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView profile;
        private ImageView online;
        private ImageView offline;


        public ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile = itemView.findViewById(R.id.profile);
            online = itemView.findViewById(R.id.online);
            offline = itemView.findViewById(R.id.offline);
        }
    }
}
