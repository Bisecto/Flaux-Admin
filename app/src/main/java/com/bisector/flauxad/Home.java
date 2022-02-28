package com.bisector.flauxad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

//import com.bisector.daradmin.R;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }

    public void upload_Movie(View view) {
        Intent i =new Intent(this,MainActivity.class);
        startActivity(i);
    }

    public void Advert(View view) {
        Intent i =new Intent(this,UploadAdvert.class);
        startActivity(i);
    }

    public void viewUsers(View view) {
        Intent i =new Intent(this,ViewUser.class);
        startActivity(i);
    }
}