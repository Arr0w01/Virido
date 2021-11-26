package com.dnext.virido;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.SetOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.dnext.virido.SigninActivity.isLoggedin;
import static com.dnext.virido.SigninActivity.user;

public class MapActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    ImageView back;
    EditText search;
    CardView setLoc_Layout, reset_button;
    public static CardView currentLocation;
    ProgressBar progressBar;
    TextView locName, locAddress;
    Button confirmLocation;
    public static String pinCode, Address, subAdd;
    public static LatLng latLng;
    public  static Double lat, lang;
    public static Boolean mapReady = false;
    Boolean alreadyExecuted  = false;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
//     Assign Id

        back = findViewById(R.id.loc_back);
        search = findViewById(R.id.search_searchbar);
        setLoc_Layout = findViewById(R.id.setLocation_layout);
        reset_button = findViewById(R.id.resetLocation);
        confirmLocation = findViewById(R.id.setLocationBTN);
        locAddress = findViewById(R.id.locationAddress);
        locName = findViewById(R.id.locationName);
        currentLocation = findViewById(R.id.currentLocation_Card);
        progressBar = findViewById(R.id.mapProgress);

//        ui design
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));


//        back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

//        current Location
        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrentLocation();
                requestLocation();



            }
        });
        

//        Search Function

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH){
                      getLocationFromAddress(textView.getText().toString());
                      finish();
                      startActivity(getIntent());
                    return true;
                }
                return false;
            }
        });

//        set location
         db = FirebaseFirestore.getInstance();

        if (mapReady){
            setLoc_Layout.setVisibility(View.VISIBLE);
            locAddress.setText(subAdd);
            locName.setText(Address);


        }else {
            setLoc_Layout.setVisibility(View.GONE);
        }
        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.requestFocus();
                setLoc_Layout.setVisibility(View.GONE);
            }
        });

        confirmLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (isLoggedin) {
                        updateLocationDB(lat, lang, getBaseContext(), db);
                        finish();
                        finishActivity(MainActivity.CONTEXT_INCLUDE_CODE);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }else {
                        Snackbar.make(setLoc_Layout, "Please Login to your account", Snackbar.LENGTH_SHORT).setAction("Later", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        }).show();
                    }
                }catch (Exception e){

                    Snackbar.make(setLoc_Layout, ""+e.getMessage(), Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        
//        Search AutoCompletion

//        String apiKey = getString(R.string.api_key);
//        if (!Places.isInitialized()) {
//            Places.initialize(getApplicationContext(), apiKey);
//        }
//
//         Create a new Places client instance.
//        PlacesClient placesClient = Places.createClient(this);
//
//         Initialize the AutocompleteSupportFragment.
//        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
//                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//        autocompleteFragment.setTypeFilter(TypeFilter.CITIES);
//        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS));
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
//                Toast.makeText(getApplicationContext(), place.getName(), Toast.LENGTH_SHORT).show();
//            }

        //    @Override
//            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
//                Toast.makeText(getApplicationContext(), status.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });

//   get location from pointer

    }

    private void requestLocation() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , REQUEST_CODE);
        }else {

            final LocationManager manager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                Handler handler = new Handler(Looper.getMainLooper());
//                 CurrentLocation();
//                finish();
//                startActivity(getIntent());
                progressBar.setVisibility(View.VISIBLE);


            }
            else {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        }
    }

    private void CurrentLocation() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , REQUEST_CODE);
        } else {

            final LocationManager manager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
//                        lat = location.getLatitude();
//                        lang = location.getLongitude();
                        mapReady = true;
                        getLocationFromLangLat(location.getLatitude(), location.getLongitude(), getApplicationContext());
                      if (!alreadyExecuted) {
                          progressBar.setVisibility(View.GONE);
                          finish();
                          startActivity(getIntent());
                          alreadyExecuted = true;
                      }
                    }
                });
            } else {
                Toast.makeText(this, "Turn on Gps", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);



            }
//

        }
    }


    public GeoPoint getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new GeoPoint((double) (location.getLatitude()),
                    (double) (location.getLongitude() ));

            getLocationFromLangLat(location.getLatitude(), location.getLongitude(),getBaseContext());
             latLng = new LatLng(location.getLatitude(),location.getLongitude());
        }catch (Exception e){
            Toast.makeText(this, "catch "+e, Toast.LENGTH_SHORT).show();

        }
           mapReady = true;

            return p1;
    }

    public static void getLocationFromLangLat(Double latitude, Double longitude, Context context){
        try {
            Geocoder geo = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geo.getFromLocation(latitude, longitude, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }

               Address =  addresses.get(0).getSubLocality();
               subAdd =  addresses.get(0).getAddressLine(0);
                pinCode =  addresses.get(0).getPostalCode();
                lat = latitude;
                lang = longitude;





        } catch (Exception e) {
//
        }
    }

    public static void updateLocationDB(double latitude, double longitude,  Context context, FirebaseFirestore db){
        Map<String, Object> data = new HashMap<>();
        data.put("lat", String.valueOf(latitude));
        data.put("lang", String.valueOf(longitude));

        db.collection("users")
                .document(user.getUid()).collection("location").document("location").set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context, "Address Update on Database", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}