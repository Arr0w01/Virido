package com.dnext.virido;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dnext.virido.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static com.dnext.virido.MapActivity.lang;
import static com.dnext.virido.MapActivity.lat;

public class SigninActivity extends AppCompatActivity {

    EditText getNumber;
    Button sendOtp;
    String verificationId;
    private static final int RC_SIGN_IN = 123;
    public  static GoogleSignInClient mGoogleSignInClient;
    ImageView gsign, fbsign ;
    ProgressBar progressBar;
    public static FirebaseAuth mAuth;
    public static User user ;
    public static FirebaseUser userMain;
    public static Boolean isLoggedin;
    CardView skipLogin,progressCard;
// ...



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
     getNumber = findViewById(R.id.signin_setPhone);
     sendOtp = findViewById(R.id.signin_getOtp);
     progressCard = findViewById(R.id.progress_card);

     sendOtp.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             if (getNumber.getText().toString().length() ==10){
                 String number = "+91"+getNumber.getText().toString();
                 PhoneSignin(number);
             }
             else {
                 Snackbar.make(sendOtp, "Please enter valid phone number!", Snackbar.LENGTH_SHORT).show();
             }
         }
     });

        try {



            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.red));
        }catch (Exception e){
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }

        skipLogin = findViewById(R.id.skipLogin);
        skipLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(SigninActivity.this, MainActivity.class);
                isLoggedin = false;
                startActivity(intent);
            }
        });

        progressBar = findViewById(R.id.progressBar);
// Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        userMain =  mAuth.getCurrentUser();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        gsign = findViewById(R.id.google_Sign);
            gsign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signIn();
                }
            });
        fbsign = findViewById(R.id.fb_sign);
        fbsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SigninActivity.this, "fb clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void PhoneSignin(String mobile) {
        progressCard.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber( mobile,60, TimeUnit.SECONDS,
                SigninActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                        Snackbar.make(,"Mobile Number Verified!",Snackbar.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getApplicationContext(), VerifiedActivity.class);
//                        startActivity(intent);
//                        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
//                                    Log.d("success", "signInWithCredential:success");
//                                    FirebaseUser user = mAuth.getCurrentUser();
//                                    updateUI(user);

//                                } else {
//                                     If sign in fails, display a message to the user.
//                                    Log.w("failure", "signInWithCredential:failure", task.getException());
//                                    updateUI(null);
//                                }
//                            }
//                        });
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        progressCard.setVisibility(View.GONE);
                        verificationId = s;
                        Intent intent = new Intent(getApplicationContext(), OtpActivity.class);
                        intent.putExtra("vID",verificationId);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSigninResult(task);

        }
    }

    private void handleSigninResult(Task<GoogleSignInAccount> task) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.d( "success", "firebaseAuthWithGoogle:" + account.getId());
            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            // Google Sign In failed, update UI appropriately
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("success", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("failure", "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    public void updateUI(FirebaseUser firebaseUser) {
        if (firebaseUser != null){
            if (firebaseUser.getPhotoUrl() != null) {
                user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl().toString(), firebaseUser.getEmail(), firebaseUser.getPhoneNumber());
            }else {
                user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), "", firebaseUser.getEmail(), firebaseUser.getPhoneNumber());
            }
            UserDao userDao = new UserDao();
            userDao.addUser(user);
            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
            isLoggedin = true;

            finish();
        }else {
            progressBar.setVisibility(View.GONE);

        }
    }

}