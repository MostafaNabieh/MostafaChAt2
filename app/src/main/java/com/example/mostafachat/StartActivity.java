package com.example.mostafachat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartActivity extends AppCompatActivity {

    @BindView(R.id.bu_login)
    Button buLogin;
    @BindView(R.id.bu_register)
    Button buRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        FirebaseApp.initializeApp(StartActivity.this);

    }

    @OnClick({R.id.bu_login, R.id.bu_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bu_login:
                login();
                break;
            case R.id.bu_register:
                register();
                break;
        }
    }

    private void register() {
        Intent re_intent=new Intent(this,RegisterActivity.class);
        startActivity(re_intent);
    }

    private void login() {
        Intent login_intent=new Intent(this,loginActivity.class);
        startActivity(login_intent);
    }

}
