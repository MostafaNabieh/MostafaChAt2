package com.example.mostafachat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatusActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.ed_status)
    EditText edStatus;
    @BindView(R.id.bu_change_Status)
    Button buChangeStatus;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        initialize();
        String old_status= getIntent().getExtras().get("status").toString();
        edStatus.setText(old_status);


    }

    private void initialize() {
        ButterKnife.bind(this);
        FirebaseApp.initializeApp(StatusActivity.this);
        buChangeStatus.setOnClickListener(this);
        mAuth=FirebaseAuth.getInstance();




    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bu_change_Status)
        {
            String new_status=edStatus.getText().toString();
            ChangeStatus(new_status);
            settingsactivity();
        }

    }

    private void ChangeStatus(String new_status)
    {
        String user_id=mAuth.getCurrentUser().getUid();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        if(TextUtils.isEmpty(new_status))
        {
            Toast.makeText(this, "Please Enter Your Status", Toast.LENGTH_SHORT).show();
        }
        else {
            databaseReference.child("status").setValue(new_status)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(StatusActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String error=task.getException().toString();
                                Toast.makeText(StatusActivity.this, ""+error, Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }

    }
    private void settingsactivity() {
        Intent status_intent = new Intent(StatusActivity.this, SettingActivity.class);
        startActivity(status_intent);
    }
}
