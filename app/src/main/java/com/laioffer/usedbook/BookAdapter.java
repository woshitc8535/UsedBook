package com.laioffer.usedbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.laioffer.usedbook.Entity.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private final BookAdapterHandler mHandler;
    private List<Book> mBooksList;

    BookAdapter(BookAdapterHandler handler) {
        mHandler = handler;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.book_result_list_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        mBooksList.get(position).populateView(
                holder.mBookCoverImageView,
                holder.mTitleDisplayTextView,
                holder.mAuthorsDisplayTextView,
                holder.mYearDisplayTextView,
                null,
                null
        );
    }

    @Override
    public int getItemCount() {
        return mBooksList == null ? 0 : mBooksList.size();
    }

   public void setBooksList(List<Book> booksList) {
        mBooksList = booksList;
        notifyDataSetChanged();
    }

    public interface BookAdapterHandler {
        void onClick(Book book);
    }

    class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView mBookCoverImageView;
        final TextView mTitleDisplayTextView;
        final TextView mAuthorsDisplayTextView;
        final TextView mYearDisplayTextView;


        BookViewHolder(@NonNull View itemView) {
            super(itemView);
            mBookCoverImageView = itemView.findViewById(R.id.iv_book_cover);
            mTitleDisplayTextView = itemView.findViewById(R.id.tv_book_title);
            mAuthorsDisplayTextView = itemView.findViewById(R.id.tv_authors);
            mYearDisplayTextView = itemView.findViewById(R.id.tv_publish_year);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int i = getAdapterPosition();
            mHandler.onClick(mBooksList.get(i));
        }
    }


}
