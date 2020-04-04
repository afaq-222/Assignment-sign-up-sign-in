package com.example.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignUp extends AppCompatActivity {
private TextView movesignin;
    private static final int Google_SignIn=123;

    FirebaseAuth firebaseAuth;
    private Button googlebtn;

    GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
   Intialize();
    }
    public void Intialize(){
        movesignin = findViewById(R.id.TV4);
        movesignin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setMovesignin();
                return false;
            }
        });
        googlebtn =findViewById(R.id.GoogleLoginBT);
        googlebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        googlebtn =findViewById(R.id.GoogleLoginBT);
        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);
    }
    public void setMovesignin(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, Google_SignIn);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Google_SignIn){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                if(googleSignInAccount!=null){
                    firebaseAuthWithGoogle(googleSignInAccount);
                }
            }
            catch (Exception e){
                Toast.makeText(this,"Error"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount googleSignInAccount) {
        Log.d("TAG","firebaseAuthWithGoogle"+googleSignInAccount.getId());
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(),null);

        firebaseAuth.signInWithCredential(authCredential).
                addOnCompleteListener(this,task->{
                    if(task.isSuccessful()){
                        Toast.makeText(this,"Sign In Successfully",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(this,"Sign In Failed",Toast.LENGTH_SHORT).show();
                    }

                });

    }
}
