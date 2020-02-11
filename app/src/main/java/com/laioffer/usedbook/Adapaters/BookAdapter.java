package com.laioffer.usedbook.Adapaters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.laioffer.usedbook.Entity.Book;
import com.laioffer.usedbook.Entity.User;
import com.laioffer.usedbook.R;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private Context mContext;
    private List<Book> mBook;


    public BookAdapter(Context mContext, List<Book> mBook) {
        this.mContext = mContext;
        this.mBook = mBook;
    }


    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.book_item,parent,false);
        return new BookAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookAdapter.ViewHolder holder, int position) {
        final Book book = mBook.get(position);
        holder.bookName.setText(book.getBookName());
        holder.price.setText("Price: "+ book.getPrice());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user.getId().equals(book.getUploadBy())) {
                        holder.seller.setText("Seller: "+user.getUsername());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //img
        if (book.getImageURL().equals("defalut")) {
            holder.bookImg.setImageResource(R.mipmap.ic_launcher);
        }
        else {
            Glide.with(mContext).load(book.getImageURL()).into(holder.bookImg);
        }

    }

    @Override
    public int getItemCount() {
        return mBook.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView bookImg;
        private TextView bookName;
        private TextView price;
        private TextView seller;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bookImg = itemView.findViewById(R.id.book_img);
            bookName = itemView.findViewById(R.id.book_name);
            price = itemView.findViewById(R.id.price);
            seller = itemView.findViewById(R.id.seller);
        }


    }


}
