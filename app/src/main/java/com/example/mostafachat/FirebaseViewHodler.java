package com.example.mostafachat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

 class FirebaseViewHodler extends RecyclerView.ViewHolder {
    public TextView Name,status;
    public CircleImageView Image_thumb;
    public View view_use_vist;
    public FirebaseViewHodler(@NonNull View itemView) {
        super(itemView);
        view_use_vist=itemView;
        Name=(TextView)view_use_vist.findViewById(R.id.all_users_name);
        status=(TextView)view_use_vist.findViewById(R.id.all_users_status);
        Image_thumb=(CircleImageView)view_use_vist.findViewById(R.id.all_users_umage);
    }
}
