package com.laioffer.usedbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.laioffer.usedbook.Entity.Book;
import com.laioffer.usedbook.Entity.User;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    public final static String BookIntentKey = "book_intent_key";

    private DatabaseReference mDatabase;
    private FirebaseUser mFirebaseUser;

    private Book mBook;
    private ImageView mImg;
    private TextView mTitle;
    private TextView mAuthor;
    private TextView mPrice;
    private TextView mDescription;
    private TextView mSellerId;
    private TextView mAddress;
    private Button mBuyButton;
    private Button mFinishTradingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String bookId = getIntent().getStringExtra(BookIntentKey);
        setContentView(R.layout.activity_detail);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getBook(bookId);

        mImg = findViewById(R.id.iv_bookImage);
        mTitle = findViewById(R.id.tv_bookTitle);
        mAuthor = findViewById(R.id.tv_bookAuthor);
        mPrice = findViewById(R.id.tv_bookPrice);
        mDescription = findViewById(R.id.tv_bookDescription);
        mSellerId = findViewById(R.id.tv_bookSellerId);
        mAddress = findViewById(R.id.tv_bookAddress);
        mBuyButton = findViewById(R.id.btn_buyBook);
        mFinishTradingButton = findViewById(R.id.btn_finishTrading);

        mBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyBook();
            }
        });
        mFinishTradingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishTrade();
            }
        });
    }

    private void updateUI() {
        if (mBook.getStatus().equals(Book.STATUS_POSTING)) {
            mBuyButton.setVisibility(View.VISIBLE);
        } else if (mBook.getStatus().equals(Book.STATUS_TRADING)) {
            mFinishTradingButton.setVisibility(View.VISIBLE);
        }

        Picasso.get().load(mBook.getImgUrl()).into(mImg);
        mTitle.setText(mBook.getTitle());
        mAuthor.setText(mBook.getAuthor());
        mPrice.setText(mBook.getPrice().toString());
        mDescription.setText(mBook.getDescription());
        getSellerUser(mBook.getSellerId());
        mAddress.setText(mBook.getAddress());
    }

    private void getBook(String bookId) {
        mDatabase.child("Books").child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mBook = dataSnapshot.getValue(Book.class);
                        mBook.setBookId(dataSnapshot.getKey());
                        updateUI();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void finishTrade() {
        mDatabase.child("Books").child(mBook.getBookId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Book book = dataSnapshot.getValue(Book.class);
                        book.setBookId(dataSnapshot.getKey());
                        book.setStatus(Book.STATUS_SOLD);
                        Map<String, Object> bookValues = book.toMap();
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/Books/" + book.getBookId(), bookValues);
                        mDatabase.updateChildren(childUpdates)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mBook.setStatus(Book.STATUS_SOLD);
                                        mBuyButton.setVisibility(View.GONE);
                                        mFinishTradingButton.setVisibility(View.GONE);
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void buyBook() {
        mDatabase.child("Books").child(mBook.getBookId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Book book = dataSnapshot.getValue(Book.class);
                        book.setBookId(dataSnapshot.getKey());
                        book.setBuyerId(mFirebaseUser.getUid());
                        book.setStatus(Book.STATUS_TRADING);
                        Map<String, Object> bookValues = book.toMap();
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/Books/" + book.getBookId(), bookValues);
                        mDatabase.updateChildren(childUpdates)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mBook.setBuyerId(mFirebaseUser.getUid());
                                        mBook.setStatus(Book.STATUS_TRADING);
                                        mBuyButton.setVisibility(View.GONE);
                                        mFinishTradingButton.setVisibility(View.VISIBLE);
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void getSellerUser(String userId) {
        if (userId == null || userId.equals("")) return;
        mDatabase.child("Users").child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        mSellerId.setText(user.getUsername());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
