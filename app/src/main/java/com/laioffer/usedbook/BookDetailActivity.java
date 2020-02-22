package com.laioffer.usedbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.laioffer.usedbook.Entity.Book;
import com.laioffer.usedbook.Entity.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class BookDetailActivity extends AppCompatActivity {
    private Set<String> sellers;
    private Set<String> buyers;
    private TextView nearbyCount;
    private String userName;
    private Boolean selfIsASeller = false;
    private Boolean selfIsABuyer = false;
    private int toastLen = Toast.LENGTH_SHORT;
    private int defaultSearchRadius = 30;
    private GeoFire sellerGeoFire;
    private GeoFire buyerGeoFire;
    private double longitude;
    private double latitude;
    private  Book book;
    private User seller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);


        sellers = new HashSet<>();
        buyers = new HashSet<>();

        nearbyCount = findViewById(R.id.tv_nearby_sellers);
        nearbyCount.setText("No information nearby");

        findViewById(R.id.bn_sell).setOnClickListener(new SellButtonHandler());
        findViewById(R.id.bn_buy).setOnClickListener(new BuyButtonHandler());

        Intent intent = getIntent();
        userName = intent.getStringExtra(getString(R.string.user_name_intent_key));
        book = intent.getParcelableExtra(getString(R.string.book_intent_key));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user.getId().equals(userName)) {
                        seller = user;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        assert book != null;
        book.populateView(
                (ImageView) findViewById(R.id.iv_detail_book_cover),
                (TextView) findViewById(R.id.tv_detail_book_title),
                (TextView) findViewById(R.id.tv_detail_authors),
                (TextView) findViewById(R.id.tv_detail_publish_year),
                (TextView) findViewById(R.id.tv_detail_publisher),
                (TextView) findViewById(R.id.tv_detail_description)
        );

        DatabaseReference sellerRef = FirebaseDatabase.getInstance().getReference("books/" + book.getId() + "/sellers");
        DatabaseReference buyerRef = FirebaseDatabase.getInstance().getReference("books/" + book.getId() + "/buyers");

        sellerGeoFire = new GeoFire(sellerRef);
        buyerGeoFire = new GeoFire(buyerRef);

        //get user location used for query
        final LocationTracker mLocationTracker = new LocationTracker(this);
        mLocationTracker.getLocation();
        longitude = mLocationTracker.getLongitude();
        latitude = mLocationTracker.getLatitude();

        GeoQuery sellerQuery = sellerGeoFire.queryAtLocation(new GeoLocation(latitude, longitude), defaultSearchRadius);
        GeoQuery buyerQuery = buyerGeoFire.queryAtLocation(new GeoLocation(latitude, longitude), defaultSearchRadius);

        sellerQuery.addGeoQueryEventListener(new SellSearchHandler());
        buyerQuery.addGeoQueryEventListener(new BuySearchHandler());
    }

    private void refreshSellerBuyerCount() {
        String sellerDisplay = " sellers in " + defaultSearchRadius + "miles";
        String buyerDisplay = " readers interested";

        sellerDisplay = (selfIsASeller ? "You & " + (sellers.size() - 1) : sellers.size()) + sellerDisplay;
        buyerDisplay = (selfIsABuyer ? "You & " + (buyers.size() - 1) : buyers.size()) + buyerDisplay;

        nearbyCount.setText(buyerDisplay + ", " + sellerDisplay);
    }

    abstract class SearchHandler implements GeoQueryEventListener {
        @Override
        public void onKeyMoved(String key, GeoLocation location) {
        }

        @Override
        public void onGeoQueryReady() {
        }

        @Override
        public void onGeoQueryError(DatabaseError error) {
            Toast.makeText(getApplicationContext(), getString(R.string.cloud_error_message), toastLen).show();
        }
    }

    class SellSearchHandler extends SearchHandler {
        @Override
        public void onKeyEntered(String key, GeoLocation location) {
            sellers.add(key);
            if (key.equals(userName)) selfIsASeller = true;
            refreshSellerBuyerCount();
        }

        @Override
        public void onKeyExited(String key) {
            sellers.remove(key);
            if (key.equals(userName)) selfIsASeller = false;
            refreshSellerBuyerCount();
        }
    }

    class BuySearchHandler extends SearchHandler {
        @Override
        public void onKeyEntered(String key, GeoLocation location) {
            buyers.add(key);
            if (key.equals(userName)) selfIsABuyer = true;
            refreshSellerBuyerCount();
        }

        @Override
        public void onKeyExited(String key) {
            buyers.remove(key);
            if (key.equals(userName)) selfIsABuyer = false;
            refreshSellerBuyerCount();
        }
    }

    class SellButtonHandler implements View.OnClickListener {



        @Override
        public void onClick(final View view) {
            sellerGeoFire.setLocation(userName, new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
                Boolean tmp = selfIsASeller;

                @Override
                public void onComplete(String key, DatabaseError error) {
                    Context ctx = view.getContext();
                    if (error != null) {
                        Toast.makeText(ctx, getString(R.string.cloud_error_message), toastLen).show();
                    } else {
                        if (tmp) {
                            Toast.makeText(ctx, getString(R.string.already_seller_message), toastLen).show();
                        } else {
                            Toast.makeText(ctx, getString(R.string.success_selling_message), toastLen).show();
                        }
                    }
                }
            });

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Sell");
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("longitude",longitude);
            hashMap.put("latitude", latitude);
            hashMap.put("seller", userName);
            hashMap.put("imageUrl", seller.getImageURL());
            hashMap.put("userName", seller.getUsername());

            reference.child(book.getId()).push().setValue(hashMap);
        }
    }

    class BuyButtonHandler implements View.OnClickListener {
        @Override
        public void onClick(final View view) {
            buyerGeoFire.setLocation(userName, new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
                Boolean tmp = selfIsABuyer;

                @Override
                public void onComplete(String key, DatabaseError error) {
                    Context ctx = view.getContext();
                    if (error != null) {
                        Toast.makeText(ctx, getString(R.string.cloud_error_message), toastLen).show();
                    } else {
                        if (tmp) {
                            Toast.makeText(ctx, getString(R.string.already_buyer_message), toastLen).show();
                        } else {
                            Toast.makeText(ctx, getString(R.string.success_buying_message), toastLen).show();


                        }
                    }
                }
            });




            Intent intent = new Intent(BookDetailActivity.this, ControlPannel.class);
            intent.putExtra("Book", book);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}


