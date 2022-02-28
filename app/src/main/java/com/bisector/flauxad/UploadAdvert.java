package com.bisector.flauxad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

//import com.bisector.daradmin.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadAdvert extends AppCompatActivity {
    private static final int PICK=1;
    Uri AdpicUrl;
    ImageView imageView;
    EditText CompanyName,CompanyDescription,CompanyUrl,CompanyEmail;
    String Name,Description,Url,Email,currentuid;
    StorageReference sReference;
    DatabaseReference databaseReference;
    UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_advert);
        imageView=findViewById(R.id.AdvertImage);
        CompanyName=findViewById(R.id.Name_Company);
        CompanyDescription=findViewById(R.id.DescriptionAboutAdvert);
        CompanyUrl=findViewById(R.id.Website);
        CompanyEmail=findViewById(R.id.Email);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Advert");
        sReference= FirebaseStorage.getInstance().getReference().child("Advert");
    }
    private String Choosevideo(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadAd() {
        if (AdpicUrl != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading....");
            progressDialog.show();
            final StorageReference storageReference = sReference.child(System.currentTimeMillis() + "." + Choosevideo(AdpicUrl));
            uploadTask = storageReference.putFile(AdpicUrl);

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return storageReference.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloaduri = task.getResult();
                                Toast.makeText(UploadAdvert.this, "Data Uploaded", Toast.LENGTH_SHORT).show();
                              AdvertUploadAdapter advertUploadAdapter=new AdvertUploadAdapter(Name,Description,Url,Email,downloaduri.toString());


                                String uploadsid = databaseReference.push().getKey();
                                databaseReference.child(uploadsid).setValue(advertUploadAdapter);
                                currentuid = uploadsid;
                                Intent i= new Intent(UploadAdvert.this,Home.class);
                                startActivity(i);
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(UploadAdvert.this, "Failed", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

        } else {
            Toast.makeText(this, "no video Selected", Toast.LENGTH_SHORT).show();
        }

    }

    public void UploadAd(View view) {
        Name=CompanyName.getText().toString().trim();
        Description=CompanyDescription.getText().toString().trim();
        Url=CompanyUrl.getText().toString().trim();
        Email=CompanyEmail.getText().toString().trim();
        if (!Name.equals("")){
            if (!Description.equals("")){
                if (!Url.equals("")){
                    if (!Email.equals("")){
                        uploadAd();

                    } else {
                        Toast.makeText(this, "Company Email Required", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Company Website or contact Link " +
                            "Required", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Company Description Required", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Company Name Required", Toast.LENGTH_SHORT).show();
        }
    }


    public void selectAd(View view) {
        openImg();
    }
    public void  openImg(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,PICK);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK || resultCode == RESULT_OK
                || data.getData() != null) {
            try {
                AdpicUrl = data.getData();
                imageView.setImageURI(AdpicUrl);

            } catch (Exception e) {
                Log.e("Error", "Error with setting the Video.");
                e.printStackTrace();
            }


        }
    }



}