package com.laioffer.usedbook.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
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
    private RecyclerView recyclerView_buy;
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
        recyclerView_sell = view.findViewById(R.id.myBook);
        recyclerView_buy = view.findViewById(R.id.recycler_view);

        recyclerView_buy.setHasFixedSize(true);
        recyclerView_buy.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_buy.setNestedScrollingEnabled(false);

        return view;
    }
}
