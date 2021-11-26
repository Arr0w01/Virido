package com.dnext.virido;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dnext.virido.Adapter.SliderAdapter;
import com.dnext.virido.ProfileActivities.ViridoProActivity;
import com.dnext.virido.model.CartModel;
import com.dnext.virido.model.SliderData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.dnext.virido.MainActivity.AvailablePincodes;
import static com.dnext.virido.MainActivity.cartCount;
import static com.dnext.virido.fragment.HomeFragment.finalPincode;

public class ProductView extends AppCompatActivity {

    ImageView back, search, viewCart, paymentIcon;
    TextView nameView,typeView, priceView, discount_priceView, discountView, fresh, ratingView, ratingCount_view,
    deliveryDate, cancelTime, deliveryPrice, paymentMode, pincode, cartCountView;
    CardView changePincode,viridoPro, detail, cartCountCard;
    SliderView sliderView;
    String name,type,category, quantity, image, image2,rating ,price, discountPrice,discount ,ratingCount,description ;
    Boolean isDiscount,isFresh,isPrepaid;
    Button saveForLater, addToCart;
    Calendar calForDate;
    FirebaseAuth auth;
    FirebaseUser user;
    CartModel cartModel;
    String userID, cartid;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SimpleDateFormat currentDate, currentTime;
    String saveCurrentDate,saveCurrentTIme, saveNextDate;
    Handler handler = new Handler(Looper.getMainLooper());
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);
//        UI
        this.getWindow().setStatusBarColor(getResources().getColor(R.color.productTheme));

//        Id Assign

        back = findViewById(R.id.product_back);
        search = findViewById(R.id.product_search);
        viewCart = findViewById(R.id.product_cart);
        sliderView = findViewById(R.id.product_slider);
        nameView = findViewById(R.id.product_name);
        typeView = findViewById(R.id.product_type);
        priceView = findViewById(R.id.product_price);
        discount_priceView = findViewById(R.id.product_discountPrice);
        discountView = findViewById(R.id.product_discount);
        ratingView = findViewById(R.id.product_rating);
        ratingCount_view = findViewById(R.id.product_ratingCount);
        fresh = findViewById(R.id.product_fresh);
        saveForLater  =findViewById(R.id.product_SFL);
        addToCart = findViewById(R.id.product_addCart);
        deliveryDate = findViewById(R.id.deliveryDate);
        cancelTime = findViewById(R.id.cancel_time);
        deliveryPrice = findViewById(R.id.deliveryPrice);
        changePincode = findViewById(R.id.change_pincode);
        paymentMode = findViewById(R.id.payment_mode);
        paymentIcon = findViewById(R.id.payment_icon);
        viridoPro = findViewById(R.id.product_viridoproCard);
        detail = findViewById(R.id.details_card);
        pincode = findViewById(R.id.product_pincode);
        cartCountView = findViewById(R.id.product_cartCountView);
        cartCountCard = findViewById(R.id.product_cartCount_Card);
        //        top bar
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

//        Cart
//        cartCountView.setText(cartCount.toString());
        viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
            }
        });

//        Intent Data
        Intent intent = getIntent();
        image =  intent.getStringExtra("Image");
        image2 = intent.getStringExtra("Image2");
        name = intent.getStringExtra("Name");
        price = intent.getStringExtra("Price");
        discountPrice = intent.getStringExtra("Discount Price");
        discount = intent.getStringExtra("Discount");
        isDiscount = intent.getBooleanExtra("isDiscount",false);
        isFresh = intent.getBooleanExtra("isFresh", false);
        isPrepaid = intent.getBooleanExtra("isPrepaid", false);
        rating = intent.getStringExtra("Rating");
        ratingCount = intent.getStringExtra("RatingCount");
        description = intent.getStringExtra("Description");
        type = intent.getStringExtra("Type");
        category = intent.getStringExtra("Category");
        quantity = intent.getStringExtra("Quantity");

//       Image slider
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
        sliderDataArrayList.add(new SliderData(image));
        if (image2 == null) {
            sliderDataArrayList.add(new SliderData(image));
        }else {
            sliderDataArrayList.add(new SliderData(image2));
        }
        //        sliderDataArrayList.add(new SliderData(url3));
        SliderAdapter adapter = new SliderAdapter(getApplicationContext(), sliderDataArrayList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setAutoCycle(false);

//        textview Assign
        nameView.setText(name);
        typeView.setText(type);

        if (isDiscount){
            discountView.setVisibility(View.VISIBLE);
            priceView.setVisibility(View.VISIBLE);
            priceView.setPaintFlags(priceView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            discount_priceView.setText("₹"+discountPrice);
            priceView.setText("₹"+price);
            discountView.setText(discount+"% off");
        }else {
            discountView.setVisibility(View.INVISIBLE);
            priceView.setText("₹"+price);
            discount_priceView.setVisibility(View.GONE);
        }

        if(isFresh){
            fresh.setText("Fresh");
            fresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(fresh,"Deliver direct from the market", Snackbar.LENGTH_LONG).show();
                }
            });
        }else {
            fresh.setText("Stored");
            fresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(fresh,"Stocks are stored, deliver Within hours! ", Snackbar.LENGTH_LONG).show();
                }
            });
        }




//        Rating
         ratingCount_view.setText(ratingCount+" Ratings");
         ratingView.setText(rating);


//       Change pincode
          changePincode.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
               Intent intent1 = new Intent(getApplicationContext(), MapActivity.class);
               startActivity(intent1);
               finish();
              }
          });
          pincode.setText(finalPincode);
          if (AvailablePincodes.contains(finalPincode)){

          }else if (finalPincode == null){
              Snackbar.make(pincode,"Your Location is not set, Please set your Location", Snackbar.LENGTH_SHORT).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                      .setAction("Set Location", new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                              Intent intent1 = new Intent(getApplicationContext(), MapActivity.class);
                              startActivity(intent1);
                          }
                      }).show();
              addToCart.setEnabled(false);
              addToCart.setBackgroundColor(getResources().getColor(R.color.darkGrey));
              addToCart.setTextColor(Color.WHITE);
          }

          else {
//              Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
              Snackbar.make(pincode,"Sorry your pincode Is not Available for delivery", Snackbar.LENGTH_LONG).show();
              addToCart.setEnabled(false);
              addToCart.setBackgroundColor(getResources().getColor(R.color.darkGrey));
              addToCart.setTextColor(Color.WHITE);
          }

//        Delivery Timing
         calForDate = Calendar.getInstance();
         currentDate = new SimpleDateFormat("dd MMM");
         currentTime = new SimpleDateFormat("HH:mm:ss a");

         saveCurrentDate = currentDate.format(calForDate.getTime());

         calForDate.add(Calendar.DAY_OF_YEAR, 1);
         saveNextDate = currentDate.format(calForDate.getTime());
         cancelTime.setText("Order can be canceled \nbefore 11:59PM "+saveCurrentDate+", Today");
         deliveryDate.setText("Delivery by "+saveNextDate+", Tomorrow");
         deliveryPrice.setPaintFlags(priceView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

//         Payment mode
         if (isPrepaid){
             paymentMode.setText("This product has only prepaid payment mode");
             paymentIcon.setImageResource(R.drawable.ic_prepaid);
         }else {
             paymentMode.setText("Cash on Delivery Available");
             paymentIcon.setImageResource(R.drawable.ic_money);
         }

//         Virido Pro
         viridoPro.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent viridopro = new Intent(getApplicationContext(), ViridoProActivity.class);
                 startActivity(viridopro);
             }
         });

//         Detail

         detail.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 detailPopup();
             }
         });

//         Save  For Later
         saveForLater.setOnFocusChangeListener(new View.OnFocusChangeListener() {
             @Override
             public void onFocusChange(View view, boolean b) {
                 if (b){
                     handler.postDelayed(new Runnable() {
                         @Override
                         public void run() {
                             view.clearFocus();
                         }
                     },250);
                 }
             }
         });


//         Add to Cart

         auth = FirebaseAuth.getInstance();
         user = auth.getCurrentUser();
         userID = user.getUid();
         cartModel = new CartModel();
         firebaseDatabase = FirebaseDatabase.getInstance();


         addToCart.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 AddCart();
             }
         });

    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference dr = firebaseDatabase.getReference("Cart").child(userID);
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartCount  = snapshot.getChildrenCount();

                if (cartCount == 0){
                    cartCountCard.setVisibility(View.GONE);
                }

                else if (cartCount != 0 && cartCount != null){
                    cartCountCard.setVisibility(View.VISIBLE);
                    cartCountView.setText(cartCount.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void AddCart() {
        saveCurrentDate = currentDate.format(calForDate.getTime());
        saveCurrentTIme = currentTime.format(calForDate.getTime());

        if (isDiscount){
            cartModel.setDiscountprice(discountPrice);
            cartModel.setPrice(price);
            cartModel.setTotalprice(discountPrice);
        }else {
            cartModel.setDiscountprice(price);
            cartModel.setPrice(price);
            cartModel.setTotalprice(price);
        }

        cartModel.setItemcount("1");


        cartid = name+ saveCurrentDate+saveCurrentTIme;
        cartModel.setUid(cartid);

        cartModel.setName(name);
        cartModel.setType(type);
        if (ratingCount!=null) {
            cartModel.setRatingCount(ratingCount);
        }else {
            cartModel.setRatingCount("0");
        }
        cartModel.setDiscountpercent(discount);
        cartModel.setImageurl(image);
        cartModel.setRating(rating);
        cartModel.setIsdiscounted(isDiscount.toString());

        databaseReference = firebaseDatabase.getReference("Cart").child(userID).child(cartid);
        if (cartModel != null) {
            databaseReference.setValue(cartModel)

                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
//                                Toast.makeText(ProductView.this, "Item Has Been Added To Cart", Toast.LENGTH_SHORT).show();
                                Snackbar.make(addToCart, "Item has been added to cart", Snackbar.LENGTH_SHORT).setAction("View Cart", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                                        startActivity(intent);
                                    }
                                }).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show();
                                finish();
                                startActivity(ProductView.this.getIntent());
                            } else {
                                Toast.makeText(ProductView.this, "failed to add Cart", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(this, "data not fetched", Toast.LENGTH_SHORT).show();
        }


    }

    private void detailPopup() {
            TextView quantityV,categoryV, typeV, descriptionV;

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ProductView.this, R.style.BottomSheetDialog);
            bottomSheetDialog.setContentView(R.layout.description_bottom_sheet);

            descriptionV = bottomSheetDialog.findViewById(R.id.details_description);
            typeV = bottomSheetDialog.findViewById(R.id.details_type);
            categoryV = bottomSheetDialog.findViewById(R.id.details_category);
            quantityV = bottomSheetDialog.findViewById(R.id.details_quantity);

            descriptionV.setText(description);
            typeV.setText(type);
            categoryV.setText(category);
            quantityV.setText(quantity);

            bottomSheetDialog.show();
            bottomSheetDialog.getWindow().findViewById(R.id.mainCard).setBackgroundResource(R.color.transparent);

    }
}