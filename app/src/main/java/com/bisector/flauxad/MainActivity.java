package com.bisector.flauxad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

//import com.bisector.daradmin.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
Uri Videouri;
EditText movieTittele;
Button SelectVideo;
String videocategory, Videoname,currentuid,selected,selected2;
StorageManager storageManager;
StorageReference sReference;
StorageTask storageTask;
DatabaseReference databaseReference;
EditText movieDescription;
MediaController mediaController;
VideoView videoView;
UploadTask uploadTask;
VideoUploadDetail videoUploadDetail;
private static final int PICK=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieDescription=findViewById(R.id.videoDescription);
        movieTittele=findViewById(R.id.title);
        SelectVideo=findViewById(R.id.Selectmovie);
        videoView=findViewById(R.id.videovew);
        videoView.setZOrderOnTop(true);
        mediaController= new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.start();;
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Videos");
        sReference= FirebaseStorage.getInstance().getReference().child("Videos");
        Spinner spinner= findViewById(R.id.to);
        Spinner spinner2= findViewById(R.id.to2);
        ArrayList<String> category = new ArrayList<>();
        category.add("Select Movie Category");
        category.add("Action");
        category.add("Adventure");
        category.add("Comedy");
        category.add("Crime");
        category.add("Drama");
        category.add("Fantasy");
        category.add("History");
        category.add("Horror");
        category.add("Musical");
        category.add("Mystery");
        category.add("Romance");
        category.add("Sci-Fi");
        category.add("Thriller");
        category.add("War");
        category.add("Western");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,category);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner2.setAdapter(adapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected2 = parent.getItemAtPosition(position).toString();
                if (selected2.trim().equals("")){

                    return;
                } else {

                    Toast.makeText(MainActivity.this, "u selected " + selected2 , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = parent.getItemAtPosition(position).toString();
                if (selected.trim().equals("")){

                    return;
                } else {

                    Toast.makeText(MainActivity.this, "u selected " + selected, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
SelectVideo.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        OpenVideos();
    }
});

    }

    private void OpenVideos() {

        Intent i = new Intent();
        i.setType("video/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,PICK);

    }
    private String Choosevideo(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK || resultCode==RESULT_OK
         || data.getData() != null){
            try {
                Videouri = data.getData();
                Toast.makeText(this, Videouri.toString(), Toast.LENGTH_SHORT).show();
                videoView.setVideoURI(Videouri);

            }catch (Exception e){
                Log.e("Error", "Error with setting the Video.");
                e.printStackTrace();
            }


                }


    }



    public void Upload(View view) {
       // Videoname=movieTittele.getText().toString();
        if (!selected.equals("Select Movie Category")){
            if (!movieTittele.getText().toString().trim().equals("")){
                if (!movieDescription.getText().toString().trim().equals("")){
                    Uploadvideo();
                } else {
                    movieDescription.setError("Movie Description required");
                }
            }else {
                Toast.makeText(this, "Please Select A movie", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please select A valid movie Category", Toast.LENGTH_LONG).show();

        }
    }
    public void Uploadvideo(){
        if (Videouri!=null){
            ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Darling's TV");
            final StorageReference storageReference=sReference.child(System.currentTimeMillis() + "." + Choosevideo(Videouri));
            uploadTask=storageReference.putFile(Videouri);

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return storageReference.getDownloadUrl();
                }

            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloaduri=task.getResult();
                        Toast.makeText(MainActivity.this, "Data Saved", Toast.LENGTH_SHORT).show();
                        VideoUploadDetail videoUploadDetail= new VideoUploadDetail("","","",
                                    downloaduri.toString(),movieTittele.getText().toString(),movieDescription.getText().toString(),selected,selected2);
                           String uploadsid= databaseReference.push().getKey();
                           databaseReference.child(uploadsid).setValue(videoUploadDetail);
                    currentuid=uploadsid;
                        progressDialog.dismiss();
                        if (currentuid.equals(uploadsid)) {
                            Addthumnail();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                    }
                }

            });
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    progressDialog.setMessage("     ");
                    progressDialog.setMessage("uploaded   "+(int) progress+"%...");
                    progressDialog.show();
                }
            });
            //            storageTask=storageReference.putFile(Videouri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            String Videouri=uri.toString();
//                            VideoUploadDetail videoUploadDetail= new VideoUploadDetail("","","",
//                                    Videouri,videoTittle,movieDescription.getText().toString(),selected);
//                            String uploadsid= databaseReference.push().getKey();
//                            databaseReference.child(uploadsid).setValue(videoUploadDetail);
//                            currentuid=uploadsid;
//                            progressDialog.dismiss();
//                        }
//                    });
//                }
//            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
            //double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

//                    double progress=(100.0 *snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
//
//                }
//            });
//

        } else {
            Toast.makeText(this, "no video Selected", Toast.LENGTH_SHORT).show();
        }

    }
    public void Addthumnail(){
        Intent intent=new Intent(this,UploadingThumbail.class);
        intent.putExtra("currentuid",currentuid);
        intent.putExtra("thumbnailsName",movieTittele.getText().toString());
        startActivity(intent);
        Toast.makeText(this, "Video Uploaded Sucessfully now upload Thumbnail", Toast.LENGTH_LONG).show();
    }
}