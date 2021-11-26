package com.dnext.virido;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dnext.virido.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import static com.dnext.virido.SigninActivity.mAuth;
import static com.dnext.virido.SigninActivity.user;

public class OtpActivity extends AppCompatActivity {
    Button verify;
    ProgressBar progressBar;
    ImageView back;
    EditText otp1, otp2, otp3, otp4, otp5, otp6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        this.getWindow().setStatusBarColor(getResources().getColor(R.color.light_yellow));

        back = findViewById(R.id.otp_back);
        verify = findViewById(R.id.otp_verify);
        otp1 = findViewById(R.id._otp1);
        otp2 = findViewById(R.id._otp2);
        otp3 = findViewById(R.id._otp3);
        otp4 = findViewById(R.id._otp4);
        otp5 = findViewById(R.id._otp5);
        otp6 = findViewById(R.id._otp6);
        progressBar = findViewById(R.id.otp_verifyProgress);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

                otp1.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 1){
                    otp2.requestFocus();
                }
            }
        });

        otp2.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 1){
                    otp3.requestFocus();
                }

            }
        });
        otp2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_DEL ){
                    otp1.requestFocus();
                }
                return false;
            }
        });
        otp3.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 1){
                    otp4.requestFocus();
                }

            }
        });
        otp3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_DEL ){
                    otp2.requestFocus();
                }
                return false;
            }
        });
        otp4.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 1){
                    otp5.requestFocus();
                }

            }
        });
        otp4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_DEL ){
                    otp3.requestFocus();
                }
                return false;
            }
        });
        otp5.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 1){
                    otp6.requestFocus();
                }


            }
        });
        otp5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_DEL ){
                    otp4.requestFocus();
                }
                return false;
            }
        });
        otp6.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() == 1){
                    otp6.clearFocus();
                }


            }
        });
        otp6.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_DEL ){
                    otp5.requestFocus();
                }
                return false;
            }
        });

        Intent intent = getIntent();

        String verificationId = intent.getStringExtra("vID");

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                verify.setVisibility(View.INVISIBLE);
                String code = otp1.getText().toString()+otp2.getText().toString()+otp3.getText().toString()
                        + otp4.getText().toString()+otp5.getText().toString()+otp6.getText().toString();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                if (verificationId !=null) {
//
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                            verificationId,
                            code
                    );
                    try {

                        auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("success", "signInWithCredential:success");
                                    FirebaseUser firebaseUser = auth.getCurrentUser();
                                    progressBar.setVisibility(View.GONE);
                                    user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), "", firebaseUser.getEmail(), firebaseUser.getPhoneNumber());
                                    UserDao userDao = new UserDao();
                                    userDao.addUser(user);

                                    Intent mainActivity = new Intent(getApplicationContext(), VerifiedActivity.class);
                                    startActivity(mainActivity);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("failure", "signInWithCredential:failure", task.getException());
                                    Toast.makeText(OtpActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
                    }catch (Exception e){
                        Toast.makeText(OtpActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                    }



                }else {
                    Toast.makeText(OtpActivity.this, "null"+verificationId, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void updateUI(FirebaseUser firebaseUser) {
        if (firebaseUser != null){

               user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl().toString(), firebaseUser.getEmail(), firebaseUser.getPhoneNumber());
               UserDao userDao = new UserDao();
               userDao.addUser(user);



            finish();
        }else {


        }
    }
}