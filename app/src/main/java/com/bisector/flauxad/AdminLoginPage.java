package com.bisector.flauxad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//import com.bisector.daradmin.R;

public class AdminLoginPage extends AppCompatActivity {
EditText editText;
Button btn;
ProgressBar progressBar;
FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login_page);
        editText=findViewById(R.id.Admin_Unique_Code);
        btn=findViewById(R.id.Admin_Login);
        progressBar=findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email="bisector0@gmail.com";
                String uniqueCode= editText.getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE);
                if (!uniqueCode.equals("")){
                  mAuth.signInWithEmailAndPassword(Email,uniqueCode).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent i = new Intent(AdminLoginPage.this, Home.class);
                            startActivity(i);
                            editText.setText("");
                            progressBar.setVisibility(View.INVISIBLE);
                        } else{
                            editText.setText("");
                           Toast.makeText(AdminLoginPage.this, "Failed to log In. Check Your credentials", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
                    } else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(AdminLoginPage.this, "Unique Code is wrong", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
//    {
//        l_pw=LoginPassword.getText().toString().trim();
//        if (!l_em.equals("")) {
//            if (!l_pw.equals("")) {
//                loading.setVisibility(View.VISIBLE);
//                mainrelative.setVisibility(View.GONE);
//                mAuth.signInWithEmailAndPassword(l_em,l_pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()){
//                            Intent i = new Intent(Register_SignIn.this, MainActivity.class);
//                            startActivity(i);
//                            loading.setVisibility(View.GONE);
//                            mainrelative.setVisibility(View.VISIBLE);
//                        } else{
//                            Toast.makeText(Register_SignIn.this, "Failed to log In. Check Your credentials", Toast.LENGTH_SHORT).show();
//                            loading.setVisibility(View.GONE);
//                            mainrelative.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });
//            }else {
//                Password.setError("Password Required");
//            }
//        }else {
//            Email.setError("Email Required");
//        }
//    }
}