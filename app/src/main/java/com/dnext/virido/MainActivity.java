package com.dnext.virido;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
      public static  FloatingActionButton cartButton;
      public static com.google.android.material.bottomappbar.BottomAppBar bottomAppBar;
      public static ArrayList<String> AvailablePincodes =  new ArrayList<String>();
      public static CardView cartCountCard;
      public static TextView cartCountview;
      FirebaseUser firebaseUser;
      String userID;
      FirebaseDatabase db;
      DatabaseReference dr;
      public static Long cartCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Id Assign
        BottomNavigationView navView = findViewById(R.id.nav_view);
        cartButton = findViewById(R.id.CartButton);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        cartCountCard = findViewById(R.id.cartCountView);
        cartCountview = findViewById(R.id.cartCount);

        //        Bottom Menu Design
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        navView.setBackground(null);

//        Cart Activity
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cart = new Intent(MainActivity.this, CartActivity.class);
                startActivity(cart);
            }
        });


        FirebaseAuth auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        userID = firebaseUser.getUid();
        db = FirebaseDatabase.getInstance();
        dr = db.getReference("Cart").child(userID);



//        Available Areas pincodes for app

        AvailablePincodes.add("700104");
        AvailablePincodes.add("700103"); // Narendrapur //Nepalgange
        AvailablePincodes.add("700154"); // boral
        AvailablePincodes.add("700084"); //Garia
        AvailablePincodes.add("700141"); // Maheshtala
        AvailablePincodes.add("700039"); //Picnic Garden, tiljala, topsia
        AvailablePincodes.add("700093"); // Purba Putiyari
        AvailablePincodes.add("700100"); //Vip Nagar
        AvailablePincodes.add("700193"); // Vivekananda Polly
        AvailablePincodes.add("700027"); // Alipore
        AvailablePincodes.add("700007"); // BaraBazar
        AvailablePincodes.add("700020"); // Ajc bose Road
        AvailablePincodes.add("700027"); // Alipore Bodygurd line
        AvailablePincodes.add("700086"); // Baghajatin
        AvailablePincodes.add("700019"); // Ballygunge
        AvailablePincodes.add("700008"); // Barisha
        AvailablePincodes.add("700034"); // Behala
        AvailablePincodes.add("700041"); // Haridevpur
        AvailablePincodes.add("700025"); // Bhawanipore
        AvailablePincodes.add("700042"); // Bosepukur
        AvailablePincodes.add("700032"); // Bijaygarh
        AvailablePincodes.add("700088"); // Brace Bridge
        AvailablePincodes.add("700027"); // Chetla
        AvailablePincodes.add("700031"); // Dhakuriya
        AvailablePincodes.add("700029"); // Lake Kalibari Doverlane
        AvailablePincodes.add("700107"); // EKT
        AvailablePincodes.add("700047"); // Garia BT

    }

    @Override
    protected void onStart() {
        super.onStart();
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartCount  = snapshot.getChildrenCount();

                if (cartCount == 0){
                    cartCountCard.setVisibility(View.GONE);
                }

                else if (cartCount != 0 && cartCount != null){
                    cartCountCard.setVisibility(View.VISIBLE);
                    cartCountview.setText(cartCount.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}