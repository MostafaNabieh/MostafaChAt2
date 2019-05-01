package com.example.mostafachat;

import android.app.ProgressDialog;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;

public class loginActivity extends AppCompatActivity implements View.OnClickListener {


    EditText edEmailLogin;
    EditText edPasswordLogin;
    Button buLogin;
    private FirebaseAuth mAuth;
    private ProgressDialog loading;
    private String email, password;
    private DatabaseReference userref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();

    }

    private void initialize() {
        FirebaseApp.initializeApp(loginActivity.this);
        mAuth = FirebaseAuth.getInstance();
        loading = new ProgressDialog(this);
        buLogin=findViewById(R.id.bu_login);
        buLogin.setOnClickListener(this);
        edEmailLogin=findViewById(R.id.ed_email_login);
        edPasswordLogin=findViewById(R.id.ed_password_login);
        userref= FirebaseDatabase.getInstance().getReference().child("Users");

    }


    private void signIn() {
         email=edEmailLogin.getText().toString();
          password=edPasswordLogin.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please Enter Your Email ", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter Your Password ", Toast.LENGTH_SHORT).show();
        } else {
            loading.setTitle("Login");
            loading.setMessage("waiting");
            loading.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String user_online_id=mAuth.getCurrentUser().getUid();
                                String user_token= FirebaseInstanceId.getInstance().getToken();
                                userref.child(user_online_id).child("User_token").setValue(user_token)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Mainactivity();
                                            }
                                        });
                            } else {
                                String error = task.getException().toString();
                                Toast.makeText(loginActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
            loading.dismiss();

        }
    }

    private void Mainactivity() {
        Intent main_intent = new Intent(loginActivity.this, MainActivity.class);
        startActivity(main_intent);
    }

    @Override
    public void onClick(View v) {
        signIn();
    }
}
