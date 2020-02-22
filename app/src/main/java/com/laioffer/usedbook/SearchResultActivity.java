package com.laioffer.usedbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.laioffer.usedbook.Entity.Book;
import com.laioffer.usedbook.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class SearchResultActivity extends AppCompatActivity implements BookAdapter.BookAdapterHandler {
    private String mSearchQuery;
    private String mSearchColumn;

    private RecyclerView mSearchResultsRecyclerView;
    private ProgressBar mProgressBar;

    private BookAdapter mAdapter;
    private List<Book> mBooksList;
    private String userName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        mBooksList = new ArrayList<>();
        mAdapter = new BookAdapter(this);

        mProgressBar = findViewById(R.id.pb_search_loading_indicator);
        mSearchResultsRecyclerView = findViewById(R.id.rv_search_results);
        mSearchResultsRecyclerView.setHasFixedSize(true);
        mSearchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultsRecyclerView.setAdapter(mAdapter);

        Intent intent = getIntent();
        mSearchQuery = intent.getStringExtra(getString(R.string.query_intent_key));
        mSearchColumn = intent.getStringExtra(getString(R.string.column_intent_key));
        userName = intent.getStringExtra(getString(R.string.user_name_intent_key));


        new SearchTask().execute();
    }

    @Override
    public void onClick(Book book) {
        Intent intent = new Intent(this, BookDetailActivity.class);
        intent.putExtra(getString(R.string.book_intent_key), book);
        intent.putExtra(getString(R.string.user_name_intent_key), userName);
        startActivity(intent);
    }

    private class SearchTask extends AsyncTask<Void, Void, List<Book>> {
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Book> doInBackground(Void... voids) {
            URL url = NetworkUtils.buildBookSearchUrl(mSearchQuery, mSearchColumn);
            try {
                return Book.parseSearchResultString(NetworkUtils.getResponse(url));
            } catch (Exception e) {
                Log.v(TAG, "Error getting response from GoogleBooks");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mSearchResultsRecyclerView.setVisibility(View.VISIBLE);
            if (books != null) {
                mBooksList.addAll(books);
                mAdapter.setBooksList(mBooksList);
            }
        }
    }
}

