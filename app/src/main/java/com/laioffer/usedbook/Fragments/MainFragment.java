package com.laioffer.usedbook.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.laioffer.usedbook.Entity.Book;
import com.laioffer.usedbook.Entity.SellerInfo;
import com.laioffer.usedbook.Entity.User;
import com.laioffer.usedbook.LocationTracker;
import com.laioffer.usedbook.MessageActivity;
import com.laioffer.usedbook.R;
import com.laioffer.usedbook.Utils;


public class MainFragment extends Fragment implements OnMapReadyCallback , GoogleMap.OnMarkerClickListener{
    private MapView mapView;
    private View view;
    private GoogleMap googleMap;
    private LocationTracker locationTracker;
    private Marker currentMarker;
    private Book book;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;


    private BottomSheetBehavior bottomSheetBehavior;
    private ImageView sellerProfile;
    private TextView sellerName;
    private TextView price;
    private ImageView goToChat;
    private SellerInfo mseller;

    private String sellChat;


    //bottom
    private void setupBottomBehavior() {
        final View nestedScrollView = (View) view.findViewById(R.id.nestedScrollView);
        bottomSheetBehavior = BottomSheetBehavior.from(nestedScrollView);

        //set hidden initially
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        //set expansion speed
        bottomSheetBehavior.setPeekHeight(1000);

        sellerProfile = view.findViewById(R.id.sellerProile);
        sellerName = view.findViewById(R.id.seller_name);
        price = view.findViewById(R.id.price);
        goToChat = view.findViewById(R.id.goToChat);





    }






    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main, container, false);

        setupBottomBehavior();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference("Sell");
        book =  getActivity().getIntent().getParcelableExtra("Book");

        //google map
        mapView = (MapView) this.view.findViewById(R.id.event_map_view);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();// needed to get the map to display immediately
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        googleMap.setOnMarkerClickListener(this);
        this.googleMap = googleMap;
        this.googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        getActivity(), R.raw.style_json));



        locationTracker = new LocationTracker(getActivity());
        locationTracker.getLocation();

        LatLng latLng = new LatLng(locationTracker.getLatitude(), locationTracker.getLongitude());

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to Mountain View
                .zoom(16)// Sets the zoom
                .bearing(90)           // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        MarkerOptions marker = new MarkerOptions().position(latLng).
                title("You");

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.boy));

        // adding marker
        googleMap.addMarker(marker);

        //load seller
        loadSellerVisableMap();

    }

    private void loadSellerVisableMap() {
        if (book == null) {
            return;
        }
        String bookId = book.getId();
        databaseReference.child(bookId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    SellerInfo seller = noteDataSnapshot.getValue(SellerInfo.class);

//                    if (seller.getSellerId() == null || seller.getSellerId().equals(firebaseUser.getUid())) {
//                        continue;
//                    }
                    double sellerLatitude = seller.getLatitude();
                    double sellerLongitude = seller.getLongitude();

                    locationTracker.getLocation();
                    double centerLatitude = locationTracker.getLatitude();
                    double centerLongitude = locationTracker.getLongitude();

                    int distance = Utils.distanceBetweenTwoLocations(centerLatitude, centerLongitude,
                            sellerLatitude, sellerLongitude);

                    if (distance < 30) {
                        LatLng latLng = new LatLng(sellerLatitude, sellerLongitude);
                        MarkerOptions marker = new MarkerOptions().position(latLng).title("seller");

                        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.book_sell);
                        Bitmap resizeBitmap = Utils.getResizedBitmap(icon, 130, 130);
                        marker.icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap));

                        Marker mark = googleMap.addMarker(marker);
                        mark.setTag(seller);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mseller = (SellerInfo) marker.getTag();
        if (mseller == null) {
            return false;
        }
        currentMarker = marker;
        String userName = mseller.getUserName();
        String bookprice = mseller.getPrice();
        sellerName.setText(userName);
        price.setText(bookprice);

        sellChat = mseller.getSellerId();

        if (mseller.getImageUrl().equals("default")) {
            sellerProfile.setImageResource(R.mipmap.ic_launcher);
        }
        else {
            Glide.with(getContext()).load(mseller.getImageUrl()).into(sellerProfile);
        }

        goToChat.setImageResource(R.drawable.gotochat);

        goToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MessageActivity.class);
                intent.putExtra("userid", sellChat);
                startActivity(intent);

            }
        });


        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }

        return true;
    }
}
