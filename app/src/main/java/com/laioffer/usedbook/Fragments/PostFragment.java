package com.laioffer.usedbook.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.laioffer.usedbook.BookAdapter;
import com.laioffer.usedbook.Entity.Book;
import com.laioffer.usedbook.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class PostFragment extends Fragment {
    DatabaseReference reference;
    private TextView currentTime;

    private RecyclerView recyclerView_sell;
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
        recyclerView_sell = view.findViewById(R.id.recycler_view);

        recyclerView_sell.setHasFixedSize(true);
        recyclerView_sell.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_sell.setNestedScrollingEnabled(false);
        
        
        readBook();

        return view;
    }

    private void readBook() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Books");



    }
}
