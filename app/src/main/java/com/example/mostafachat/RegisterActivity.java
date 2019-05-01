package com.example.mostafachat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.ed_email)
    EditText edEmail;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.bu_register)
    Button buRegister;
    private String name, email, password;
    private FirebaseAuth mAuth;
    private ProgressDialog loading;
    private DatabaseReference databaseReference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialize();


    }

    private void initialize() {
        ButterKnife.bind(this);
        FirebaseApp.initializeApp(RegisterActivity.this);
        mAuth = FirebaseAuth.getInstance();
        loading = new ProgressDialog(this);

    }

    private void getString() {
        email = edEmail.getText().toString();
        password = edPassword.getText().toString();
        name = edName.getText().toString();
        signup(email, password, name);
    }

    @OnClick(R.id.bu_register)
    public void onViewClicked() {
        getString();
    }

    private void signup(final String email, String password, final String name) {

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
        } else {
            loading.setTitle("Create Account");
            loading.setMessage("waiting");
            loading.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                userId=mAuth.getCurrentUser().getUid();
                                String user_token= FirebaseInstanceId.getInstance().getToken();

                                databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                                databaseReference.child("Name").setValue(name);
                                databaseReference.child("status").setValue("Welcome in mostafachAt");
                                databaseReference.child("Image_thumb").setValue("new_thumb");
                                databaseReference.child("user_token").setValue(user_token);
                                databaseReference.child("image").setValue("https://firebasestorage.googleapis.com/v0/b/mostafachatproject.appspot.com/o/Profile%2Fdefault_profile.jpg?alt=media&token=adcd9ed3-cf19-41d9-8aff-303ebce72391");
                                databaseReference.child("Email").setValue(email)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    login();
                                                }
                                                else{
                                                    String error = task.getException().toString();
                                                    Toast.makeText(RegisterActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });


                            } else {
                                String error = task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            loading.dismiss();
        }


    }

    private void login() {

        Intent login_intent = new Intent(this, loginActivity.class);
        startActivity(login_intent);

    }

}
