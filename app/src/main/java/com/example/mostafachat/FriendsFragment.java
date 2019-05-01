package com.example.mostafachat;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {
    private static RecyclerView recyclerView_friend;
    private static FirebaseAuth mAuth;
    private static View myview;
    private static String currentuser;
    private static DatabaseReference friend_data_base;
        private static DatabaseReference user_list_ref;
    private static FirebaseRecyclerAdapter<Friends, viewHolderfriend> adapter_friend;
    private static FirebaseRecyclerOptions<Friends> options;

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_friends, container, false);
        recyclerView_friend = (RecyclerView) myview.findViewById(R.id.recycle_friend);
        mAuth = FirebaseAuth.getInstance();
        currentuser = mAuth.getCurrentUser().getUid();
        friend_data_base = FirebaseDatabase.getInstance().getReference().child("Friend").child(currentuser);
        friend_data_base.keepSynced(true);
        recyclerView_friend.setLayoutManager(new LinearLayoutManager(getContext()));
        user_list_ref=FirebaseDatabase.getInstance().getReference().child("Users");
        user_list_ref.keepSynced(true);
        options = new FirebaseRecyclerOptions.Builder<Friends>().setQuery(friend_data_base, Friends.class).build();
        adapter_friend = new FirebaseRecyclerAdapter<Friends, viewHolderfriend>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final viewHolderfriend holder, int position, @NonNull final Friends model) {

                final String list_user_id=getRef(position).getKey();
                user_list_ref.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String user_name=dataSnapshot.child("Name").getValue().toString();
                        String user_status =dataSnapshot.child("status").getValue().toString();
                        final String user_image=dataSnapshot.child("Image_thumb").getValue().toString();
                        holder.friend_name.setText(user_name);
                        holder.status_friend.setText(user_status);
                        Picasso.get().load(user_image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.mipmap.default_profile)
                                .into(holder.Image_thumb_friend, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception e)
                                    {
                                        Picasso.get().load(user_image).placeholder(R.mipmap.default_profile).into(holder.Image_thumb_friend);

                                    }
                                });

                        holder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence[] option=new CharSequence[]
                                        {
                                                user_name+"Profile",
                                                "Send Message"

                                        };
                                AlertDialog.Builder builder =new AlertDialog.Builder(getContext());
                                builder.setTitle("Select options");
                                builder.setItems(option, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int position) {
                                        if(position == 0)
                                        {
                                            profileactivity(list_user_id);
                                        }
                                        if(position == 1)
                                        {
                                            Chatactivity(list_user_id,user_name);
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @NonNull
            @Override
            public viewHolderfriend onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rectclerviewuser,viewGroup,false);
                return new viewHolderfriend(view);
            }
        };
        recyclerView_friend.setAdapter(adapter_friend);
        return myview;
    }

    private void Chatactivity(String list_user_id, String user_name) {
        Intent chatactivity=new Intent(getContext(),ChatActivity.class);
        chatactivity.putExtra("user_id",list_user_id);
        chatactivity.putExtra("user_name",user_name);
        startActivity(chatactivity);
    }

    private void profileactivity(String list_user_id) {
        Intent profileintent=new Intent(getContext(),ProfileActivity.class);
        profileintent.putExtra("user_id",list_user_id);
        startActivity(profileintent);
    }



    @Override
    public void onStart() {
        super.onStart();
        adapter_friend.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter_friend.stopListening();
    }

}
