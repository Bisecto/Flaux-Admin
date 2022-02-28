package com.bisector.flauxad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

//import com.bisector.daradmin.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadingThumbail extends AppCompatActivity {
Uri videoThumbnail;
String Thumbnail_url;
ImageView imageView;
Button selectthumb,uploadTofire;
StorageReference storageReference;
DatabaseReference databaseReference;
TextView selectedtext;
RadioGroup radioGroup;
RadioButton radioButton1,radioButton2,radioButton3,radioButton4;
    DatabaseReference updatedataref;
    private static final int PICK=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploading_thumbail);
        selectedtext=findViewById(R.id.selected);
        imageView=findViewById(R.id.imageView);
        uploadTofire=findViewById(R.id.Upload);
        uploadTofire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedtext.equals("No Thumbnail Selected")){
                    Toast.makeText(UploadingThumbail.this, "select an image", Toast.LENGTH_SHORT).show();

                } else {
                    Upload();
                }
            }
        });
        selectthumb=findViewById(R.id.selectThumb);
        radioGroup=findViewById(R.id.radiogroup);
        radioButton1=findViewById(R.id.one);
        radioButton2=findViewById(R.id.two);
        radioButton3=findViewById(R.id.three);
        radioButton4=findViewById(R.id.four);
        storageReference= FirebaseStorage.getInstance().getReference().child("VideoThumbnail");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Videos");
        String currentuid=getIntent().getExtras().getString("currentuid");
        updatedataref= FirebaseDatabase.getInstance().getReference("Videos").child(currentuid);
        selectthumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showimagechoser();
            }
        });
        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kids=radioButton1.getText().toString();
                updatedataref.child("videoType").setValue(kids);
                Toast.makeText(UploadingThumbail.this, "selected"+kids, Toast.LENGTH_SHORT).show();
            }
        });
        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recent=radioButton2.getText().toString();
                updatedataref.child("videoType").setValue(recent);
                Toast.makeText(UploadingThumbail.this, "selected"+recent, Toast.LENGTH_SHORT).show();
            }
        });
        radioButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String popular=radioButton3.getText().toString();
                updatedataref.child("videoType").setValue(popular);
                Toast.makeText(UploadingThumbail.this, "selected"+popular, Toast.LENGTH_SHORT).show();
            }
        });
        radioButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldmovies=radioButton4.getText().toString();
                updatedataref.child("videoType").setValue(oldmovies);
                Toast.makeText(UploadingThumbail.this, "selected"+oldmovies, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void  showimagechoser(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,PICK);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK || resultCode==RESULT_OK
                || data.getData() != null){
            try {
                videoThumbnail = data.getData();
                String thumbname=getFileName(videoThumbnail);
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),videoThumbnail);
                imageView.setImageURI(videoThumbnail);

            }catch (Exception e){
                Log.e("Error", "Error with setting the Video.");
                e.printStackTrace();
            }


        }


    }
    private String getFileName(Uri uri){
        String result=null;
        if (uri.getScheme().equals("content")){
            Cursor cursor=getContentResolver().query(uri,null,null,null,null);
            try {
                if (cursor!=null  && cursor.moveToFirst()){
                    result=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                }
            } finally {
                cursor.close();
            }
        }
        if (result==null){
            result = uri.getPath();
            int cut =result.lastIndexOf("/");
            if (cut != -1) {
                result=result.substring(cut+1);
            }
        }
        return result;
    }
    private void Upload(){
        if (videoThumbnail!=null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading....");
            progressDialog.show();
            String video_tittle= getIntent().getExtras().getString("thumbnailsName");
            StorageReference sref= storageReference.child(video_tittle+"."+ getExt(videoThumbnail));
            sref.putFile(videoThumbnail).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    sref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Thumbnail_url=uri.toString();
                            updatedataref.child("videoThumb").setValue(Thumbnail_url);
                            progressDialog.dismiss();
                            Toast.makeText(UploadingThumbail.this, "files Uplaoded", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadingThumbail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public String getExt(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}