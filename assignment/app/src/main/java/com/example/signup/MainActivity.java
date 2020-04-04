package com.example.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
private TextView signup,resetpassword;
private static final int Google_SignIn=123;

FirebaseAuth firebaseAuth;
private Button googlebtn,facebookbtn;

GoogleSignInClient googleSignInClient;
CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    intialize();
    }
    public void intialize(){
        signup = findViewById(R.id.TV4);
        signup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                moveToSignUP();
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
        firebaseAuth =FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

    resetpassword =findViewById(R.id.TV5);
    resetpassword.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Intent intent = new Intent(getApplicationContext(),ResetPassword.class);
            startActivity(intent);
            return false;
        }
    });
    facebookbtn=findViewById(R.id.FacebookLoginBT);
    facebookbtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            facebook();
        }
    });
    }
    public void moveToSignUP(){
       Intent i = new Intent(getApplicationContext(),SignUp.class);
       startActivity(i);
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
        callbackManager.onActivityResult(requestCode,resultCode,data);
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
public void facebook(){
    callbackManager = CallbackManager.Factory.create();
    LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
               handleFacebookAccessToken(loginResult.getAccessToken());
        }
        @Override
        public void onCancel() {
        }
        @Override
        public void onError(FacebookException error) {
            Toast.makeText(getApplicationContext(), "Error in Login Facebook"+error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
}
    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                      Toast.makeText(getApplicationContext(),"Login successfully",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(),"Login not successfully",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
