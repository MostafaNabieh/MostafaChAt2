package com.example.mostafachat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.image_defult)
    ImageView imageDefult;
    @BindView(R.id.user_visit_name)
    TextView userVisitName;
    @BindView(R.id.user_visit_status)
    TextView userVisitStatus;
    @BindView(R.id.user_send_request)
    Button userSendRequest;
    @BindView(R.id.user_decline_request)
    Button userDeclineRequest;
    private static DatabaseReference myReference;
    private static DatabaseReference FirendRequestRef;
    private static String friend_status = "not_friend";
    private static FirebaseAuth mAuth;
    private static String id;
    private static String current_user_id;
    private static DatabaseReference friendRequest;
    private static String currentdata;
    private static DatabaseReference notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialize();
        getdatabase();


    }


    private void getdatabase() {

        myReference = FirebaseDatabase.getInstance().getReference().child("Users");
        myReference.keepSynced(true);
        id = getIntent().getExtras().get("user_id").toString();
        userDeclineRequest.setVisibility(View.INVISIBLE);
        userDeclineRequest.setEnabled(false);
        if(!current_user_id.equals(id)) {


            userSendRequest.setOnClickListener(this);


        }
        else
        {
            userDeclineRequest.setVisibility(View.INVISIBLE);
            userSendRequest.setVisibility(View.INVISIBLE);
        }
        myReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("Image_thumb").getValue().toString();
                userVisitName.setText(name);
                userVisitStatus.setText(status);
                Picasso.get().load(image).placeholder(R.mipmap.default_profile).into(imageDefult);
                FirendRequestRef.child(current_user_id)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    if (dataSnapshot.hasChild(id)) {
                                        String message_request = dataSnapshot.child(id).child("request_type").getValue().toString();
                                        if (message_request.equals("sent")) {
                                            friend_status = "request_sent";
                                            userSendRequest.setText("Cancel Friend Request");
                                            userDeclineRequest.setVisibility(View.INVISIBLE);
                                            userDeclineRequest.setEnabled(false);
                                        }
                                        else if (message_request.equals("Received")) {
                                            friend_status = "request_receive";
                                            userSendRequest.setText("Accept Friend");
                                            userDeclineRequest.setVisibility(View.VISIBLE);
                                            userDeclineRequest.setEnabled(true);
                                            userDeclineRequest.setOnClickListener(ProfileActivity.this);
                                        }


                                    }
                                }
                                    else
                                    {
                                        friendRequest.child(current_user_id)
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                            if(dataSnapshot.hasChild(id))
                                                            {
                                                                friend_status = "friends";
                                                                userSendRequest.setText("Un Friend ");
                                                            }

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                    }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initialize() {

        FirebaseApp.initializeApp(ProfileActivity.this);
        ButterKnife.bind(this);
        FirendRequestRef = FirebaseDatabase.getInstance().getReference().child("Friend_Request");
        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        friendRequest = FirebaseDatabase.getInstance().getReference().child("Friend");
        FirendRequestRef.keepSynced(true);
        friendRequest.keepSynced(true);
        notification=FirebaseDatabase.getInstance().getReference().child("Notification");
        notification.keepSynced(true);


    }

    @Override
    public void onClick(View v) {
        userSendRequest.setEnabled(false);
        if(!current_user_id.equals(id)) {
            if (v.getId() == R.id.user_send_request) {
                if (friend_status.equals("request_sent")) {
                    CancelFriendRequest();
                }
                if (friend_status.equals("not_friend")) {
                    SendFriendRequest();

                }
                if (friend_status.equals("request_receive")) {
                    AcceptFiriendRequest();


                }
                if (friend_status.equals("friends")) {
                    unFriend();

                }
            }

        }
        if(v.getId() == R.id.user_decline_request )
        {
            Declinerequest();
        }



    }

    private void Declinerequest() {

        FirendRequestRef.child(current_user_id).child(id).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {

                                               if (task.isSuccessful()) {
                                                   FirendRequestRef.child(id).child(current_user_id)
                                                           .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {
                                                           if (task.isSuccessful()) {
                                                               userSendRequest.setEnabled(true);
                                                               friend_status = "not_friend";
                                                               userSendRequest.setText("Send Friebd Request");
                                                               userDeclineRequest.setVisibility(View.INVISIBLE);
                                                               userDeclineRequest.setEnabled(false);
                                                           }
                                                       }
                                                   });
                                               }
                                           }
                                       }
                );

    }

    private void unFriend() {
        friendRequest.child(current_user_id).child(id).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            friendRequest.child(id).child(current_user_id).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                userSendRequest.setEnabled(true);
                                                friend_status = "request_sent";
                                                userSendRequest.setText("Send Friebd Request");
                                                userDeclineRequest.setVisibility(View.INVISIBLE);
                                                userDeclineRequest.setEnabled(false);

                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    private void AcceptFiriendRequest() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
        currentdata = simpleDateFormat.format(calendar.getTime());
        friendRequest.child(current_user_id).child(id).child("data").setValue(currentdata)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            friendRequest.child(id).child(current_user_id).child("data").setValue(currentdata)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                FirendRequestRef.child(current_user_id).child(id).removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                   @Override
                                                                                   public void onComplete(@NonNull Task<Void> task) {

                                                                                       if (task.isSuccessful()) {
                                                                                           FirendRequestRef.child(id).child(current_user_id)
                                                                                                   .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                               @Override
                                                                                               public void onComplete(@NonNull Task<Void> task) {
                                                                                                   if (task.isSuccessful()) {
                                                                                                       userSendRequest.setEnabled(true);
                                                                                                       friend_status = "friend";
                                                                                                       userSendRequest.setText("Un Friend");
                                                                                                       userDeclineRequest.setVisibility(View.INVISIBLE);
                                                                                                       userDeclineRequest.setEnabled(false);
                                                                                                   }
                                                                                               }
                                                                                           });
                                                                                       }
                                                                                   }
                                                                               }
                                                        );
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    private void CancelFriendRequest() {

        FirendRequestRef.child(current_user_id).child(id).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {

                                               if (task.isSuccessful()) {
                                                   FirendRequestRef.child(id).child(current_user_id)
                                                           .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {
                                                           if (task.isSuccessful()) {
                                                               userSendRequest.setEnabled(true);
                                                               friend_status = "not_friend";
                                                               userSendRequest.setText("Send Friebd Request");
                                                               userDeclineRequest.setVisibility(View.INVISIBLE);
                                                               userDeclineRequest.setEnabled(false);
                                                           }
                                                       }
                                                   });
                                               }
                                           }
                                       }
                );
    }

    private void SendFriendRequest() {

        FirendRequestRef.child(current_user_id).child(id).child("request_type")
                .setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirendRequestRef.child(id).child(current_user_id).child("request_type")
                            .setValue("Received").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                HashMap <String,String> notificationdata=new HashMap<>();
                                notificationdata.put("from",current_user_id);
                                notificationdata.put("type","request");
                                notification.child(id).push().setValue(notificationdata)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    userSendRequest.setEnabled(true);
                                                    friend_status = "request_sent";
                                                    userSendRequest.setText("Cancel Friend Request");
                                                    userDeclineRequest.setVisibility(View.INVISIBLE);
                                                    userDeclineRequest.setEnabled(false);
                                                }
                                            }
                                        });


                            } else {
                                String error = task.getException().toString();
                                Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    String error = task.getException().toString();
                    Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
