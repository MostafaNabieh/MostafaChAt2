package com.example.mostafachat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.viewpager_main)
    ViewPager viewpagerMain;
    @BindView(R.id.main_tab)
    TabLayout mainTab;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private managerfargments managerfargments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();


    }

    private void initialize() {
        ButterKnife.bind(this);
        FirebaseApp.initializeApp(MainActivity.this);
        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.include_tool);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MostafaChAt");
        managerfargments = new managerfargments(getSupportFragmentManager());
        viewpagerMain.setAdapter(managerfargments);
        mainTab.setupWithViewPager(viewpagerMain);

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseApp.initializeApp(MainActivity.this);
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Startactivity();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.m_logout) {
            mAuth.signOut();
            Startactivity();
        }
        if(item.getItemId() == R.id.m_Account)
        {
            settingactivity();
        }
        if(item.getItemId() == R.id.m_alluser)
        {
            allusersactivity();
        }
        return true;
    }

    private void Startactivity() {
        Intent start_intent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(start_intent);
    }
    private void settingactivity() {
        Intent setting_intent = new Intent(MainActivity.this,SettingActivity.class);
        startActivity(setting_intent);
    }
    private void allusersactivity() {
        Intent alluser_intent = new Intent(MainActivity.this,AllusersActivity.class);
        startActivity(alluser_intent);
    }
}
