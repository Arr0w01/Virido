package com.dnext.virido.fragment;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dnext.virido.Adapter.CategoryAdapter;
import com.dnext.virido.Adapter.FeaturedProductAdapter;
import com.dnext.virido.Adapter.SliderAdapter;
import com.dnext.virido.MapActivity;
import com.dnext.virido.ProfileActivities.ViridoProActivity;
import com.dnext.virido.R;
import com.dnext.virido.SearchActivity;
import com.dnext.virido.model.ProductModel;
import com.dnext.virido.model.SliderData;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.smarteist.autoimageslider.SliderView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;
import static com.dnext.virido.MainActivity.bottomAppBar;
import static com.dnext.virido.MainActivity.cartButton;
import static com.dnext.virido.MainActivity.cartCount;
import static com.dnext.virido.MainActivity.cartCountCard;
import static com.dnext.virido.MainActivity.cartCountview;
import static com.dnext.virido.MapActivity.Address;
import static com.dnext.virido.MapActivity.pinCode;
import static com.dnext.virido.MapActivity.subAdd;
import static com.dnext.virido.SigninActivity.user;


public class HomeFragment extends Fragment {
     ConstraintLayout mainLayout, noInternetLayout;
     public static String finalAddress, finalPincode, finalSubadd, finalCity;
     ScrollView scrollView;
     ImageView viridoPro;
     EditText searchBar;
     TextView location;
     String homeLat, homeLang;
     SliderView sliderView;
     Boolean isScrolling = false;
     RecyclerView featuredRV, categoryRV, offerRV, occasionalRV;
     FeaturedProductAdapter featuredAdapter, offerAdapter, occasionalAdapter;
     CategoryAdapter categoryAdapter;
     String url1 = "https://firebasestorage.googleapis.com/v0/b/virido-4a084.appspot.com/o/sliderImg%2Fvirido_slide.png?alt=media&token=770fef47-26d8-4403-88bc-24b8b0c9330b";
     String url2 = "https://firebasestorage.googleapis.com/v0/b/virido-4a084.appspot.com/o/sliderImg%2Fpng_20211031_015403_0000.png?alt=media&token=3927a26a-3b3c-4544-906c-619847d0c013";
     String url3 = "https://firebasestorage.googleapis.com/v0/b/virido-4a084.appspot.com/o/sliderImg%2Fvirido_slide.png?alt=media&token=770fef47-26d8-4403-88bc-24b8b0c9330b";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        Assign ID

        scrollView = view.findViewById(R.id.sv);
        searchBar = view.findViewById(R.id.home_searchBar);
        location = view.findViewById(R.id.home_location);
        sliderView = view.findViewById(R.id.product_slider);
        featuredRV = view.findViewById(R.id.h_featuredRV);
        categoryRV = view.findViewById(R.id.h_categoryRv);
        offerRV = view.findViewById(R.id.h_offersRV);
        occasionalRV = view.findViewById(R.id.h_occasionalRV);
        viridoPro = view.findViewById(R.id.viridopro_banner);

//      Scroll hide Bottom NAvigation
        scrollView.setHorizontalScrollBarEnabled(false);
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int x, int y, int oldx, int oldy) {
                    if (y > 0 & 0 < oldy) {
                        bottomAppBar.setVisibility(View.GONE);
                        cartButton.setVisibility(View.GONE);
                        if (cartCount != null && cartCount != 0) {
                            cartCountCard.setVisibility(View.GONE);
                        }
                        if (oldy > y) {
                            bottomAppBar.setVisibility(View.VISIBLE);
                            cartButton.setVisibility(View.VISIBLE);
                            if (cartCount != null && cartCount !=0) {
                                cartCountCard.setVisibility(View.VISIBLE);
                            }
                        }
                    }
//                    else if (){}
                    else {
                        bottomAppBar.setVisibility(View.VISIBLE);
                        cartButton.setVisibility(View.VISIBLE);
                        if (cartCount !=null && cartCount != 0) {
                            cartCountCard.setVisibility(View.VISIBLE);
                        }
                    }

            }
        });


//        if (!isScrolling){
//            bottomAppBar.setVisibility(View.GONE);
//        }



//        SearchBar
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

//        Location Section
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(user.getUid()).collection("location").document("location");

// Source can be CACHE, SERVER, or DEFAULT.
            Source source = Source.CACHE;

// Get the document, forcing the SDK to use the offline cache
            docRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        // Document found in the offline cache
                        DocumentSnapshot document = task.getResult();
                        homeLat = document.getString("lat");
                        homeLang = document.getString("lang");
                        try {
                            getLocation(Double.parseDouble(homeLat), Double.parseDouble(homeLang));
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();
                        }

                        Log.d(TAG, "Cached document data: " + document.getData());
                    } else {
                        Log.d(TAG, "Cached get failed: ", task.getException());
                    }
                }
            });
        }catch (Exception e){

            location.setText("Set Delivery Location");
        }


//            if (Address != null) {
//                location.setText(Address);
//            } else {
//                location.setText("Set Delivery Location");
//            }
            location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), MapActivity.class);
                    startActivity(intent);
                }
            });
//        Virido Pro

        viridoPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viridoPro = new Intent(getContext(), ViridoProActivity.class);
                startActivity(viridoPro);
            }
        });

//            Category Recyclerview


        categoryRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
        FirebaseRecyclerOptions<ProductModel> options1 =
                new FirebaseRecyclerOptions.Builder<ProductModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Categories"), ProductModel.class)
                        .build();
        categoryAdapter = new CategoryAdapter(options1);
        categoryRV.setAdapter(categoryAdapter);
        categoryRV.getBackground().setAlpha(30);
//        FirebaseDatabase scoresRef = FirebaseDatabase.getInstance();
//        scoresRef.getReference().child("Products").orderByValue().addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
//                Toast.makeText(getContext(), ""+"The " + dataSnapshot.getKey() + " score is " + dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
//            }

            // ...
//        });
//            Slider section

        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
        sliderDataArrayList.add(new SliderData(url1));
        sliderDataArrayList.add(new SliderData(url2));
        sliderDataArrayList.add(new SliderData(url3));

        SliderAdapter adapter = new SliderAdapter(getContext(), sliderDataArrayList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

//        Featured RecyclerView

            featuredRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            FirebaseRecyclerOptions<ProductModel> options =
                    new FirebaseRecyclerOptions.Builder<ProductModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Products"), ProductModel.class)
                            .build();

            featuredAdapter = new FeaturedProductAdapter(options);
            featuredRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            featuredRV.setAdapter(featuredAdapter);
            featuredRV.setNestedScrollingEnabled(false);

        //        Offer RecyclerView

        offerRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        FirebaseRecyclerOptions<ProductModel> offerOptions =
                new FirebaseRecyclerOptions.Builder<ProductModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("isdiscounted").equalTo("true"), ProductModel.class)
                        .build();

        offerAdapter = new FeaturedProductAdapter(offerOptions);
        offerRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        offerRV.setAdapter(offerAdapter);
        offerRV.setNestedScrollingEnabled(false);
        offerRV.getBackground().setAlpha(30);


//        Occasional RecyclerView
        occasionalRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        FirebaseRecyclerOptions<ProductModel> occasionalOptions =
                new FirebaseRecyclerOptions.Builder<ProductModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("category").equalTo("Occasional"), ProductModel.class)
                        .build();

        occasionalAdapter = new FeaturedProductAdapter(occasionalOptions);
        occasionalRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        occasionalRV.setAdapter(occasionalAdapter);
        occasionalRV.setNestedScrollingEnabled(false);
            return view;
        }





    @Override
    public void onStart() {
        super.onStart();
        featuredAdapter.startListening();
        categoryAdapter.startListening();
        offerAdapter.startListening();
        occasionalAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        featuredAdapter.stopListening();
        categoryAdapter.stopListening();
        offerAdapter.stopListening();
        occasionalAdapter.stopListening();
    }

    private void getLocation(Double latitude, Double longitude) {
        try {
            Geocoder geo = new Geocoder(getContext(), Locale.getDefault());
            List<android.location.Address> addresses = null;
            try {
                addresses = geo.getFromLocation(latitude, longitude, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }

//            Address =  addresses.get(0).getSubLocality();
            location.setText(addresses.get(0).getSubLocality());
            finalAddress = addresses.get(0).getAddressLine(0);
            finalPincode = addresses.get(0).getPostalCode();
            finalSubadd = addresses.get(0).getSubLocality();
            finalCity = addresses.get(0).getSubAdminArea();
        } catch (Exception e) {
//
        }
    }
}
