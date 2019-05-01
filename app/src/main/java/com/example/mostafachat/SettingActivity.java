package com.example.mostafachat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.circle_image)
    CircleImageView circleImage;
    @BindView(R.id.tx_name)
    TextView txName;
    @BindView(R.id.tx_status)
    TextView txStatus;
    @BindView(R.id.bu_imange)
    Button buImange;
    @BindView(R.id.bu_status)
    Button buStatus;
    private static DatabaseReference database;
    private FirebaseAuth mAuth;
    private String user_id;
    private String name;
    private String status;
    private String image;
    private static final int gallery = 1;
    private StorageReference storageReference;
    private Bitmap thumb_bitmap=null;
    private StorageReference Thumbstrogeref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initialize();
        get_datat();

    }

    private void initialize() {
        ButterKnife.bind(this);
        FirebaseApp.initializeApp(SettingActivity.this);
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("Profile");
        buImange.setOnClickListener(SettingActivity.this);
        buStatus.setOnClickListener(this);
        Thumbstrogeref=FirebaseStorage.getInstance().getReference().child("Image_thumb");



    }

    private void get_datat() {
        user_id = mAuth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        database.keepSynced(true);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = dataSnapshot.child("Name").getValue().toString();
                status = dataSnapshot.child("status").getValue().toString();
                image = dataSnapshot.child("image").getValue().toString();
                txName.setText(name);
                txStatus.setText(status);
                Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.mipmap.default_profile).into(circleImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(image).placeholder(R.mipmap.default_profile).into(circleImage);

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bu_imange) {
            Intent set_image = new Intent();
            set_image.setAction(Intent.ACTION_GET_CONTENT);
            set_image.setType("image/*");
            startActivityForResult(set_image, gallery);
        }
        if (v.getId() == R.id.bu_status) {

            Statusactivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == gallery && resultCode == RESULT_OK && data != null) {
            Uri gallery_data = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                final File thumb_file_path=new File(resultUri.getPath());
                try {
                    thumb_bitmap=new Compressor(this)
                            .setMaxHeight(200)
                            .setMaxHeight(200)
                            .setQuality(50)
                            .compressToBitmap(thumb_file_path);
                }
                catch (IOException e)
                {

                }
                ByteArrayOutputStream byteArrayOutputStream =new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
                final byte[] thumb_byte=byteArrayOutputStream.toByteArray();

                final String path = mAuth.getCurrentUser().getUid();
                final StorageReference riversRef = storageReference.child(path + ".jpg");
                final StorageReference thumb_file =Thumbstrogeref.child(path+".jgp");
                riversRef.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SettingActivity.this, "Successful", Toast.LENGTH_SHORT).show();


                            final String dowland = task.getResult().getDownloadUrl().toString();
                            UploadTask uploadTask =thumb_file.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task_thumb) {
                                    String dowlanduri=task_thumb.getResult().getDownloadUrl().toString();
                                    if(task_thumb.isSuccessful())
                                    {
                                        Map update_user_data=new HashMap();
                                        update_user_data.put("image",dowland);
                                        update_user_data.put("Image_thumb",dowlanduri);
                                        database.updateChildren(update_user_data)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(SettingActivity.this, "Updateing", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            String error = task.getException().toString();
                                                            Toast.makeText(SettingActivity.this, "" + error, Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });
                                    }
                                    else
                                    {
                                        String error = task_thumb.getException().toString();
                                        Toast.makeText(SettingActivity.this, "" + error, Toast.LENGTH_SHORT).show();


                                    }
                                }
                            });



                        } else {
                            String error = task.getException().toString();
                            Toast.makeText(SettingActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }

    private void Statusactivity() {
        String old_status = txStatus.getText().toString();
        Intent status_intent = new Intent(SettingActivity.this, StatusActivity.class);
        status_intent.putExtra("status",old_status);
        startActivity(status_intent);
    }
}
