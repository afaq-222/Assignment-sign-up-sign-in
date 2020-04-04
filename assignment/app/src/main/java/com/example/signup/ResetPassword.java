package com.example.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {
private EditText Email;
private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
    Email = findViewById(R.id.Email);
    button= findViewById(R.id.LoginBT);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           String email= Email.getText().toString().trim();
            if(Email.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(),"Please Enter Your Email",Toast.LENGTH_SHORT).show();
            }
            else {
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Please check you Email",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        }
    });

    }
}
