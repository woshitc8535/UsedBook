package com.laioffer.usedbook.Fragments;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.laioffer.usedbook.Adapaters.BookAdapter;
import com.laioffer.usedbook.Entity.Book;
import com.laioffer.usedbook.R;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class PostFragment extends Fragment {
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    DatabaseReference reference;

    private TextView currentTime;
    private EditText title, price, description;
    private ImageView book_img;


    private Button post;
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Book> mBook;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);


        Date current = Calendar.getInstance().getTime();
        String str = current.toString();
        currentTime = view.findViewById(R.id.time_bar);
        currentTime.setText(str);

        //init
        book_img = view.findViewById(R.id.book_img);
        title = view.findViewById(R.id.book_title);
        price = view.findViewById(R.id.book_price);
        description = view.findViewById(R.id.book_description);
        post = view.findViewById(R.id.btn_post);


        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        mBook = new ArrayList<>();

        readBook();

        //upload post
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_bookName = title.getText().toString();
                String text_bookPrice = price.getText().toString();
                String text_decription = description.getText().toString();


                if (TextUtils.isEmpty(text_bookName) || TextUtils.isEmpty(text_bookPrice) || TextUtils.isEmpty(text_decription)) {
                    Toast.makeText(getContext(), "All Fields are needed1", Toast.LENGTH_SHORT).show();
                } else {
                    uploadBook(text_bookName, text_bookPrice, text_decription);
                    Toast.makeText(getContext(), "Successful upload", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    private void readBook() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Books");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mBook.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Book book = snapshot.getValue(Book.class);
                    if (book.getSellerId().equals(firebaseUser.getUid())) {
                        mBook.add(book);
                    }
                }
                bookAdapter = new BookAdapter(getContext(), mBook);
                recyclerView.setAdapter(bookAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void uploadBook(String text_bookName, String text_bookPrice, String text_decription) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String userid = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("bookName", text_bookName);
        hashMap.put("uploadBy", userid);
        hashMap.put("price", text_bookPrice);
        hashMap.put("description", text_decription);
        hashMap.put("imageURL", "default");

        reference.child("Books").push().setValue(hashMap);
    }


}
