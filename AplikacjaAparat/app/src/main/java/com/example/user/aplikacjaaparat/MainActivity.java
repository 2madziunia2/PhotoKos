package com.example.user.aplikacjaaparat;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {

    private Button mUploadBtn;
    private ImageView mImageView;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private Uri mImageUri;
     private final int CAMERA_REQUEST_CODE = 1              ;




   // private static final int CAMERA_REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStorage = FirebaseStorage.getInstance().getReference();

        mUploadBtn =(Button) findViewById(R.id.upload);
        mImageView = (ImageView) findViewById(R.id.imageView);

        mProgress = new ProgressDialog(this);

        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);
          mImageUri  =data.getData();
         Bitmap bitmap = (Bitmap)data.getExtras().get("data");
         mImageView.setImageBitmap(bitmap);
         if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK ){
           super.onActivityResult(requestCode, resultCode, data);
           
           mProgress.setMessage("ładowanie zdjecia..");
            mProgress.show();

         Uri uri = data.getData();
             StorageReference filepath = mStorage.child("zdjecia").child(uri.getLastPathSegment());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                  //  mProgress.dismiss();
                    Toast.makeText(MainActivity.this,"załadowano...",Toast.LENGTH_LONG).show();
                }

            });
        }

    }
}
