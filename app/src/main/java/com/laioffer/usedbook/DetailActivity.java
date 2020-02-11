package com.laioffer.usedbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.laioffer.usedbook.Entity.Book;

public class DetailActivity extends AppCompatActivity {

    public final static String BookIntentKey = "book_intent_key";

    private Book mBook;
    private ImageView mImg;
    private TextView mTitle;
    private TextView mAuthor;
    private TextView mPrice;
    private TextView mDescription;
    private TextView mSellerId;
    private TextView mAddress;
    private Button mBuyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBook = (Book) getIntent().getParcelableExtra(BookIntentKey);
        setContentView(R.layout.activity_detail);
        mImg = findViewById(R.id.iv_bookImage);
        mTitle = findViewById(R.id.tv_bookTitle);
        mAuthor = findViewById(R.id.tv_bookAuthor);
        mPrice = findViewById(R.id.tv_bookPrice);
        mDescription = findViewById(R.id.tv_bookDescription);
        mSellerId = findViewById(R.id.tv_bookSellerId);
        mAddress = findViewById(R.id.tv_bookAddress);
        mBuyButton = findViewById(R.id.btn_buyBook);

        //TODO: miss image setting
        mTitle.setText(mBook.getTitle());
        mAuthor.setText(mBook.getAuthor());
        mPrice.setText(mBook.getPrice().toString());
        mDescription.setText(mBook.getDescription());
        mSellerId.setText(mBook.getSellerId());
        mAddress.setText(mBook.getAddress());
    }
}
