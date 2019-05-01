package com.example.mostafachat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {
    @BindView(R.id.select_image)
    ImageButton selectImage;
    @BindView(R.id.ed_chat)
    EditText edChat;
    @BindView(R.id.send_message)
    ImageButton sendMessage;
    private String user_id;
    private String user_name;
    private Toolbar chat_tool_bar;
    private FirebaseAuth mAuth;
    private String messageid;
    private static String messagereceiveid;
    private static String messagesenderid;
    private static String messagetext;
    private DatabaseReference root_message;
    private RecyclerView message_recycler;
    private final List<message>messagelist=new ArrayList<>();
    private MessageAdapter messageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        FirebaseApp.initializeApp(ChatActivity.this);
        ButterKnife.bind(this);
        mAuth=FirebaseAuth.getInstance();
        message_recycler=(RecyclerView)findViewById(R.id.message_recycler_chat);
        messagesenderid=mAuth.getCurrentUser().getUid();

        messageid=mAuth.getCurrentUser().getUid();
        user_id = getIntent().getExtras().get("user_id").toString();
        user_name = getIntent().getExtras().get("user_name").toString();
        messagereceiveid=user_id;
        messagetext=edChat.getText().toString();
        messageAdapter=new MessageAdapter(messagelist);
        message_recycler.setLayoutManager(new LinearLayoutManager(this));
        message_recycler.setHasFixedSize(true);
        message_recycler.setAdapter(messageAdapter);

        chat_tool_bar = (Toolbar) findViewById(R.id.include_tool_chat);
        ActionBar actionBar = getSupportActionBar();
        setSupportActionBar(chat_tool_bar);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessage();
            }
        });




    }

    private void getmessage() {
        root_message.child("Messages").child(messagesenderid).child(messagereceiveid)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        message message=dataSnapshot.getValue(message.class);
                        messagelist.add(message);
                        messageAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void SendMessage() {
        String message=edChat.getText().toString();
        root_message=FirebaseDatabase.getInstance().getReference();
        if(TextUtils.isEmpty(message))
        {
            Toast.makeText(ChatActivity.this, "Enter your message", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String message_send_id="Message/"+messagesenderid+"/"+messagereceiveid;
            String message_recvier_id="Message/"+messagereceiveid+"/"+messagesenderid;
            DatabaseReference user_message_id=root_message.child("Messages").child(messagesenderid)
                    .child(messagereceiveid).push();

            String message_push_id=user_message_id.getKey();
            Map messagebody=new HashMap();
            messagebody.put("message",message);
            messagebody.put("seen",false);
            messagebody.put("type","text");
            messagebody.put("time", ServerValue.TIMESTAMP);
            Map messagedetails=new HashMap();
            messagedetails.put(message_send_id+"/"+message_push_id,messagebody);
            messagedetails.put(message_recvier_id+"/"+message_push_id,messagebody);
            root_message.updateChildren(messagedetails, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if(databaseError !=null)
                    {

                    }
                }
            });


        }
    }


}
