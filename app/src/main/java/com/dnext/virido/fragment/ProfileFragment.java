package com.dnext.virido.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dnext.virido.MainActivity;
import com.dnext.virido.MapActivity;
import com.dnext.virido.ProfileActivities.AboutActivity;
import com.dnext.virido.ProfileActivities.AddressActivity;
import com.dnext.virido.ProfileActivities.BookmarkActivity;
import com.dnext.virido.ProfileActivities.HelpActivity;
import com.dnext.virido.ProfileActivities.MyOrdersActivity;
import com.dnext.virido.ProfileActivities.MyRatingActivity;
import com.dnext.virido.ProfileActivities.NotificationActivity;
import com.dnext.virido.ProfileActivities.PrivacyPolicyActivity;
import com.dnext.virido.ProfileActivities.RatingReviewActivity;
import com.dnext.virido.ProfileActivities.SettingsActivity;
import com.dnext.virido.ProfileActivities.TermsConditionsActivity;
import com.dnext.virido.ProfileActivities.ViridoProActivity;
import com.dnext.virido.R;
import com.dnext.virido.SigninActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.dnext.virido.MainActivity.bottomAppBar;
import static com.dnext.virido.MainActivity.cartButton;
import static com.dnext.virido.MainActivity.cartCount;
import static com.dnext.virido.MainActivity.cartCountCard;
import static com.dnext.virido.MapActivity.Address;
import static com.dnext.virido.MapActivity.subAdd;
import static com.dnext.virido.SigninActivity.isLoggedin;
import static com.dnext.virido.SigninActivity.mGoogleSignInClient;
import static com.dnext.virido.fragment.HomeFragment.finalPincode;
import static com.dnext.virido.fragment.HomeFragment.finalSubadd;


public class ProfileFragment extends Fragment {
    TextView location, userName,email, EditNum, mobileNum, editEmail;
    private  static int numberCount;
    String mobileNumber, verificationId;
    ImageView profilePic, editName;
    Button  signActivityLaunch;
    CardView changeLocation;
    CardView bookmark,notification,settings,address,viridoPro,myrating, myOrders, ratingReview, help, pPolicy, tnc, about,signout;
    ConstraintLayout signedIn, unSigned;
    public static FirebaseAuth auth;
    ScrollView scrollView;
    final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
//       UI
//       requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

       requireActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.pink));



//         Assign ID
        location = view.findViewById(R.id.profile_address);
        userName = view.findViewById(R.id.userName);
        editName = view.findViewById(R.id.editName);
        email = view.findViewById(R.id.email);
        editEmail = view.findViewById(R.id.editEmail);
        profilePic = view.findViewById(R.id.profile_img);
        signout = view.findViewById(R.id.signout_card);
        signedIn = view.findViewById(R.id.loggedin_layout);
        unSigned = view.findViewById(R.id.unLogged_layout);
        EditNum = view.findViewById(R.id.editNumber);
        mobileNum = view.findViewById(R.id.mobileNum);
        scrollView = view.findViewById(R.id.profile_scroll);
        bookmark = view.findViewById(R.id.bookmark_card);
        notification = view.findViewById(R.id.notification_card);
        settings = view.findViewById(R.id.settings_card);
        address = view.findViewById(R.id.address_card);
        viridoPro = view.findViewById(R.id.viridoPro);
        myrating = view.findViewById(R.id.my_rating);
        myOrders = view.findViewById(R.id.my_orders);
        ratingReview = view.findViewById(R.id.rating_review);
        help = view.findViewById(R.id.help);
        pPolicy = view.findViewById(R.id.privacy_policy);
        tnc = view.findViewById(R.id.terms_condition);
        about = view.findViewById(R.id.about);
        changeLocation = view.findViewById(R.id.changeLocation);



//      UI
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int x, int y, int oldx, int oldy) {
                if (y > 0 & 0 < oldy) {
                    bottomAppBar.setVisibility(View.GONE);
                    cartButton.setVisibility(View.GONE);
                    if (cartCount != null && cartCount != 0) {
                        cartCountCard.setVisibility(View.GONE);
                    }
                    if (oldy > y){
                        bottomAppBar.setVisibility(View.VISIBLE);
                        cartButton.setVisibility(View.VISIBLE);
                        if (cartCount != null && cartCount != 0) {
                            cartCountCard.setVisibility(View.VISIBLE);
                        }
                    }
                }
//                    else if (){}
                else {
                    bottomAppBar.setVisibility(View.VISIBLE);
                    cartButton.setVisibility(View.VISIBLE);
                    if (cartCount != null && cartCount != 0) {
                        cartCountCard.setVisibility(View.VISIBLE);
                    }

                }

            }
        });


        if (isLoggedin){
            signedIn.setVisibility(View.VISIBLE);
            unSigned.setVisibility(View.GONE);
            auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();

            if (finalSubadd != null && !finalSubadd.isEmpty()) {
                location.setText(finalSubadd+", "+ finalPincode);
            }else {
                location.setText("Set Location");
            }

            if (user.getDisplayName()!=null) {
                userName.setText(user.getDisplayName());
            }else {
                userName.setText("Unknown User");
            }
//            edit Username
            editName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 editName();
                }
            });

            if (user.getEmail() != null && !user.getEmail().isEmpty()){
                email.setText(user.getEmail());
                editEmail.setVisibility(View.GONE);
            }
            else {

                email.setVisibility(View.GONE);
                editEmail.setText("Add email");
            }
             editEmail.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                         AddEmail();

                 }
             });

            if (user.getPhotoUrl() != null) {

                Glide.with(getContext()).load(user.getPhotoUrl()).circleCrop().into(profilePic);
            }else {
                Glide.with(getContext()).load(R.drawable.profile_male).circleCrop().into(profilePic);
            }
//             User Mobile Number
            if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()){
                EditNum.setText("EDIT");
                mobileNum.setText(user.getPhoneNumber());
            }
            else {
                EditNum.setText("Add Number");
            }
            EditNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetNumber();
                }
            });
            
        }else {
            signedIn.setVisibility(View.GONE);
            unSigned.setVisibility(View.VISIBLE);
        }





        signActivityLaunch = view.findViewById(R.id.launch_signin);
        signActivityLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SigninActivity.class);
                startActivity(intent);
            }
        });



//        Activities

        changeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MapActivity.class);
                startActivity(intent);
            }
        });

        bookmark.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getContext(), BookmarkActivity.class);
                            startActivity(intent);
                            view.clearFocus();
                        }
                    },100);

                }
            }
        });

        notification.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getContext(), NotificationActivity.class);
                            startActivity(intent);
                            view.clearFocus();
                        }
                    },100);
                }
            }
        });

           settings.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getContext(), SettingsActivity.class);
                            startActivity(intent);
                            view.clearFocus();
                        }
                    },100);
                }
            }
        });

        address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getContext(), AddressActivity.class );
                            startActivity(intent);
                            view.clearFocus();
                        }
                    },100);
                }
            }
        });

        viridoPro.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           Intent intent = new Intent(getContext(), ViridoProActivity.class);
                           startActivity(intent);
                           view.clearFocus();
                        }
                    }, 400);
                }
            }
        });

        myrating.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getContext(), MyRatingActivity.class);
                            startActivity(intent);
                            view.clearFocus();
                        }
                    }, 400);
                }
            }
        });

        myOrders.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getContext(), MyOrdersActivity.class);
                            startActivity(intent);
                            view.clearFocus();
                        }
                    }, 400);
                }
            }
        });

        ratingReview.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getContext(), RatingReviewActivity.class);
                            startActivity(intent);
                            view.clearFocus();
                        }
                    }, 400);
                }
            }
        });

        help.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getContext(), HelpActivity.class);
                            startActivity(intent);
                            view.clearFocus();
                        }
                    }, 400);
                }
            }
        });

        pPolicy.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getContext(), PrivacyPolicyActivity.class);
                            startActivity(intent);
                            view.clearFocus();
                        }
                    }, 400);
                }
            }
        });

        tnc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getContext(), TermsConditionsActivity.class);
                            startActivity(intent);
                            view.clearFocus();
                        }
                    }, 400);
                }
            }
        });

        about.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getContext(), AboutActivity.class);
                            startActivity(intent);
                            view.clearFocus();
                        }
                    }, 400);
                }
            }
        });
        
        
        
        
        
        
        
        
//        Signout Function
        signout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

               if (b){
                   handler.postDelayed(new Runnable() {
                       @Override
                       public void run() {

                           AlertDialog newAlertDialog = new AlertDialog.Builder(getContext())
//                           setIcon(R.drawable.logout)
//                                   .setTitle("Logout Account")
                                           .setMessage("Are you sure want to logout?")
                                   .setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialogInterface, int i) {
                                           signOut();
                                           Snackbar.make(getContext(), getView(), "Account signout successfully!",Snackbar.LENGTH_LONG).show();
                                       }
                                   }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialogInterface, int i) {
                                           signout.clearFocus();
                                           dialogInterface.cancel();
                                       }
                                   }).show();


                       }
                   }, 400);

               }

            }
        });

        return view;
    }

    private void AddEmail() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(R.layout.edit_name);
        EditText getName;
        Button setName;
        ProgressBar updatenamePB;
        ImageView close;

        close = bottomSheetDialog.findViewById(R.id.editName_close);
        getName = bottomSheetDialog.findViewById(R.id.typeName);
        setName = bottomSheetDialog.findViewById(R.id.updateNameBtn);
        updatenamePB = bottomSheetDialog.findViewById(R.id.updateNamePB);
        setName.setEnabled(false);
        setName.setBackgroundColor(Color.GRAY);
        getName.requestFocus();
        getName.getShowSoftInputOnFocus();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
            }
        });

        getName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (getName.getText().toString().length() == 0){
                    setName.setBackgroundColor(Color.GRAY);
                    setName.setEnabled(false);
                }else {
                    setName.setEnabled(true);
                    setName.setBackgroundColor(getResources().getColor(R.color.pink));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//           user.verifyBeforeUpdateEmail("gamerarindam@gmail.com").addOnCompleteListener(new OnCompleteListener<Void>() {
//                         @Override
//                         public void onComplete(@NonNull Task<Void> task) {
//                             if (task.isSuccessful()){
//                                  Snackbar.make(editEmail,"Email Updated Successfully!",Snackbar.LENGTH_SHORT).show();
//                             }
//                             else {
//                                 Snackbar.make(editEmail,""+task.getException().getMessage(),Snackbar.LENGTH_SHORT).show();
//                             }
//                         }
//                     });

        setName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatenamePB.setVisibility(View.VISIBLE);
                setName.setVisibility(View.INVISIBLE);
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = auth.getCurrentUser();
                String newName = getName.getText().toString();
                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().
                        setDisplayName(newName).build();
                firebaseUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
//                            Toast.makeText(getContext(), "Name Updated Successfully!", Toast.LENGTH_SHORT).show();
                            Snackbar.make(editName, "Name Updated Successfully!", Snackbar.LENGTH_SHORT).show();
                            firebaseUser.reload();
                            userName.setText(firebaseUser.getDisplayName());
                            bottomSheetDialog.cancel();
                        }else {
                            Snackbar.make(editName, ""+task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


        bottomSheetDialog.show();
        bottomSheetDialog.getWindow().findViewById(R.id.bgmain).setBackgroundResource(android.R.color.transparent);

    }

    private void SetNumber() {

        ConstraintLayout mainLayout, otpLayout, verifiedLayout;
        Button getOtp;
        TextView resend, verifymsg;
        Button verifyBtn, done;
        EditText subNumber, otp1, otp2,otp3,otp4,otp5,otp6;
        ImageView tick,back, layoutbg1, layoutbg2, layoutbg3;
        ProgressBar otpProgress, verifyProgress;




        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(R.layout.set_number);

        otp1 = bottomSheetDialog.findViewById(R.id.otp1);
        otp2 = bottomSheetDialog.findViewById(R.id.otp2);
        otp3 = bottomSheetDialog.findViewById(R.id.otp3);
        otp4 = bottomSheetDialog.findViewById(R.id.otp4);
        otp5 = bottomSheetDialog.findViewById(R.id.otp5);
        otp6 = bottomSheetDialog.findViewById(R.id.otp6);
        back = bottomSheetDialog.findViewById(R.id.setNumber_back);
        mainLayout = bottomSheetDialog.findViewById(R.id.mainLayout);
        layoutbg1 = bottomSheetDialog.findViewById(R.id.bg1);
        otpLayout = bottomSheetDialog.findViewById(R.id.otpLayout);
        layoutbg2 = bottomSheetDialog.findViewById(R.id.bg2);
        verifiedLayout = bottomSheetDialog.findViewById(R.id.verifiedLayout);
        layoutbg3 = bottomSheetDialog.findViewById(R.id.bg3);
        getOtp = bottomSheetDialog.findViewById(R.id.getOtp);
        verifyBtn = bottomSheetDialog.findViewById(R.id.verifybtn);
        subNumber = bottomSheetDialog.findViewById(R.id.setNumber);
        resend = bottomSheetDialog.findViewById(R.id.resend_otp);
        verifymsg = bottomSheetDialog.findViewById(R.id.verifymsg);
//        subOTP = bottomSheetDialog.findViewById(R.id.submitOTP);
        tick = bottomSheetDialog.findViewById(R.id.tick);
        otpProgress = bottomSheetDialog.findViewById(R.id.otpProgress);
        verifyProgress = bottomSheetDialog.findViewById(R.id.verifyProgress);
        done = bottomSheetDialog.findViewById(R.id.doneBtn);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
            }
        });

        getOtp.setBackgroundColor(getResources().getColor(R.color.darkGrey));
        getOtp.setEnabled(false);

        subNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                numberCount =  subNumber.getText().length();
                if (numberCount == 10){
                    getOtp.setEnabled(true);
                    getOtp.setBackgroundColor(getResources().getColor(R.color.btn_blue));
                    tick.setVisibility(View.VISIBLE);
                }else {
                    getOtp.setEnabled(false);
                    getOtp.setBackgroundColor(getResources().getColor(R.color.darkGrey));
                    getOtp.setTextColor(Color.WHITE);
                    tick.setVisibility(View.GONE);
                }
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


        getOtp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mobileNumber = subNumber.getText().toString();
                otpProgress.setVisibility(View.VISIBLE);
//                getOtp.setVisibility(View.INVISIBLE);

                PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+ mobileNumber,60, TimeUnit.SECONDS,
                        getActivity(),
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Snackbar.make(verifiedLayout,"Mobile Number Verified!",Snackbar.LENGTH_SHORT).show();
                                mainLayout.setVisibility(View.GONE);
                                otpLayout.setVisibility(View.GONE);
                                layoutbg2.setVisibility(View.GONE);
                                layoutbg1.setVisibility(View.GONE);
                                verifiedLayout.setVisibility(View.VISIBLE);
                                layoutbg3.setVisibility(View.VISIBLE);
                                verifyProgress.setVisibility(View.GONE);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verifymsg.setText("Please type the verification code sent to +91 "+mobileNumber);
                                mainLayout.setVisibility(View.GONE);
                                layoutbg1.setVisibility(View.GONE);
                                otpLayout.setVisibility(View.VISIBLE);
                                otpProgress.setVisibility(View.GONE);
                                layoutbg2.setVisibility(View.VISIBLE);
                                verificationId = s;
                            }
                        });




                verifyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        verifyProgress.setVisibility(View.VISIBLE);
                        verifyBtn.setVisibility(View.INVISIBLE);
                        String code = otp1.getText().toString()+otp2.getText().toString()+otp3.getText().toString()
                                + otp4.getText().toString()+otp5.getText().toString()+otp6.getText().toString();
                        if (verificationId !=null){
//
                            getOtp.setVisibility(View.INVISIBLE);
                            subNumber.setVisibility(View.INVISIBLE);
                            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                                    verificationId,
                                    code
                            );
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            firebaseUser.updatePhoneNumber(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
//                                        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                        verifyProgress.setVisibility(View.GONE);
                                        firebaseUser.reload();
                                        mobileNum.setText(firebaseUser.getPhoneNumber());
                                        Snackbar.make(verifiedLayout,"Mobile Number Verified!",Snackbar.LENGTH_SHORT).show();
                                        mainLayout.setVisibility(View.GONE);
                                        otpLayout.setVisibility(View.GONE);
                                        layoutbg2.setVisibility(View.GONE);
                                        layoutbg1.setVisibility(View.GONE);
                                        verifiedLayout.setVisibility(View.VISIBLE);
                                        layoutbg3.setVisibility(View.VISIBLE);
                                        verifyProgress.setVisibility(View.GONE);
//                                        bottomSheetDialog.cancel();
                                    }else {
//
                                        Toast.makeText(getContext(), ""+task.getException(), Toast.LENGTH_LONG).show();
                                        Snackbar.make(getView(),""+task.getException().getMessage(),Snackbar.LENGTH_LONG).setAction("Retry" , new View.OnClickListener(){
                                            @Override
                                            public void onClick(View view) {
                                                bottomSheetDialog.cancel();
                                                bottomSheetDialog.show();
                                            }
                                        }).show();
                                        verifyProgress.setVisibility(View.GONE);
                                    }
                                }
                            });
//
//
                                                PhoneAuthCredential pc = PhoneAuthProvider.getCredential("+91"+mobileNumber,code);
                                                firebaseUser.updatePhoneNumber(pc);
//
                        }
                    }
                });
            }
        });
//
        bottomSheetDialog.show();
        bottomSheetDialog.getWindow().findViewById(R.id.bsMain).setBackgroundResource(android.R.color.transparent);

    }


    private void editName()
    {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(R.layout.edit_name);
        EditText getName;
        Button setName;
        ProgressBar updatenamePB;
        ImageView close;

        close = bottomSheetDialog.findViewById(R.id.editName_close);
        getName = bottomSheetDialog.findViewById(R.id.typeName);
        setName = bottomSheetDialog.findViewById(R.id.updateNameBtn);
        updatenamePB = bottomSheetDialog.findViewById(R.id.updateNamePB);
        setName.setEnabled(false);
        setName.setBackgroundColor(Color.GRAY);
        getName.requestFocus();
        getName.getShowSoftInputOnFocus();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
            }
        });

        getName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
              if (getName.getText().toString().length() == 0){
                  setName.setBackgroundColor(Color.GRAY);
                  setName.setEnabled(false);
              }else {
                  setName.setEnabled(true);
                  setName.setBackgroundColor(getResources().getColor(R.color.pink));
              }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        setName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatenamePB.setVisibility(View.VISIBLE);
                setName.setVisibility(View.INVISIBLE);
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = auth.getCurrentUser();
                String newName = getName.getText().toString();
                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().
                        setDisplayName(newName).build();
                firebaseUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
//                            Toast.makeText(getContext(), "Name Updated Successfully!", Toast.LENGTH_SHORT).show();
                            Snackbar.make(editName, "Name Updated Successfully!", Snackbar.LENGTH_SHORT).show();
                            firebaseUser.reload();
                            userName.setText(firebaseUser.getDisplayName());
                            bottomSheetDialog.cancel();
                        }else {
                            Snackbar.make(editName, ""+task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


        bottomSheetDialog.show();
        bottomSheetDialog.getWindow().findViewById(R.id.bgmain).setBackgroundResource(android.R.color.transparent);
    }

    @Override
    public void onDetach() {
        super.onDetach();
       requireActivity(). getWindow().setStatusBarColor(getResources().getColor(R.color.theme));
    }


    private void signOut() {
        auth.signOut();

        mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(),
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent login = new Intent(getContext(), SigninActivity.class);
                        login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(login);
                        getActivity().finish();

                    }
                });
    }
}