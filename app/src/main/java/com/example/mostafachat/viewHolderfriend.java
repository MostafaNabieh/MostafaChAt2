package com.example.mostafachat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class viewHolderfriend extends RecyclerView.ViewHolder {
    View view;
    public TextView status_friend,friend_name;
    public CircleImageView Image_thumb_friend;

    public viewHolderfriend(@NonNull View itemView) {
        super(itemView);
        view=itemView;
        status_friend=(TextView)view.findViewById(R.id.all_users_status);
        friend_name=(TextView)view.findViewById(R.id.all_users_name);
        Image_thumb_friend=(CircleImageView)view.findViewById(R.id.all_users_umage);

    }
}
