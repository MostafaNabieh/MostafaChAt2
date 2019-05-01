package com.example.mostafachat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.viewHoldermessage> {
    private List<message>list_message;

    public MessageAdapter(List<message> list_message) {
        this.list_message = list_message;
    }

    @NonNull
    @Override
    public viewHoldermessage onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_user,null);
        viewHoldermessage viewholder =new viewHoldermessage(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHoldermessage viewHoldermessage, int i) {
        message message=list_message.get(i);
        viewHoldermessage.message_text.setText(message.getMessage());

    }

    @Override
    public int getItemCount() {
        return list_message.size();
    }

    public class viewHoldermessage extends RecyclerView.ViewHolder
    {   CircleImageView circleImageView;
        TextView message_text;
        public viewHoldermessage(@NonNull View itemView) {
            super(itemView);
            circleImageView=(CircleImageView)itemView.findViewById(R.id.circle_image_chat);
            message_text=(TextView)itemView.findViewById(R.id.message_chat);
        }
    }

}
