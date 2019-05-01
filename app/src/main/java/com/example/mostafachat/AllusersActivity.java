package com.example.mostafachat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllusersActivity extends AppCompatActivity {

    @BindView(R.id.recycler_allusers)
    RecyclerView recyclerAllusers;
    private static DatabaseReference myRef;
    private FirebaseAuth myAuth;
    private FirebaseRecyclerOptions<all_users_model> Options;
    private FirebaseRecyclerAdapter<all_users_model, FirebaseViewHodler> Adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allusers);
        ButterKnife.bind(this);
        myRef=FirebaseDatabase.getInstance().getReference().child("Users");
        myRef.keepSynced(true);
        Options= new FirebaseRecyclerOptions.Builder<all_users_model>().setQuery(myRef,all_users_model.class).build();
        Adapter=new FirebaseRecyclerAdapter<all_users_model, FirebaseViewHodler>(Options) {
            @Override
            protected void onBindViewHolder(@NonNull final FirebaseViewHodler holder, final int position, @NonNull final all_users_model model) {
                holder.Name.setText(model.getName());
                holder.status.setText(model.getStatus());
                Picasso.get().load(model.getImage_thumb()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.mipmap.default_profile)
                        .into(holder.Image_thumb, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e)
                            {
                                Picasso.get().load(model.getImage_thumb()).placeholder(R.mipmap.default_profile).into(holder.Image_thumb);

                            }
                        });

                holder.view_use_vist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String user_visit_id=getRef(position).getKey();
                        Profileactivity(user_visit_id);
                    }
                });
            }

            @NonNull
            @Override
            public FirebaseViewHodler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rectclerviewuser,viewGroup,false);
                FirebaseViewHodler firebaseViewHodler=new FirebaseViewHodler(view);
                return firebaseViewHodler;
            }
        };
        recyclerAllusers.setLayoutManager(new LinearLayoutManager(this));
        recyclerAllusers.setAdapter(Adapter);
    }

    private void Profileactivity(String user_id) {
        Intent profile_activity=new Intent(AllusersActivity.this,ProfileActivity.class);
        profile_activity.putExtra("user_id",user_id);
        startActivity(profile_activity);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Adapter.stopListening();
    }
}


