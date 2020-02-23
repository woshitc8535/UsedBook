package com.laioffer.usedbook.Entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class Book implements Parcelable {
    public String getId() {
        return id;
    }
    private String id;
    private String title;
    private String isbn10;
    private String isbn13;
    private String coverImageUrl;
    private List<String> authors;
    private String publisher;
    private String description;
    private Calendar date;

    static private final SimpleDateFormat[] formats = {
            new SimpleDateFormat("yyyy-MM-dd"),
            new SimpleDateFormat("yyyy-MM"),
            new SimpleDateFormat("yyyy")
    };

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    Book() {
    }

    protected Book(Parcel in) {
        id = in.readString();
        title = in.readString();
        isbn10 = in.readString();
        isbn13 = in.readString();
        coverImageUrl = in.readString();
        authors = in.createStringArrayList();
        publisher = in.readString();
        description = in.readString();
        date = Calendar.getInstance();
        date.setTimeInMillis(in.readLong());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(isbn10);
        dest.writeString(isbn13);
        dest.writeString(coverImageUrl);
        dest.writeStringList(authors);
        dest.writeString(publisher);
        dest.writeString(description);
        dest.writeLong(date.getTimeInMillis());
    }

    public static List<Book> parseSearchResultString(String json) throws JSONException {
        if (json == null || json.length() == 0) {
            return null;
        }

        List<Book> books = new ArrayList<>();
        JSONArray resultsJsonArray = new JSONObject(json).getJSONArray("items");
        for (int i = 0; i < resultsJsonArray.length(); i++) {
            try {
                books.add(parseBookDetail(resultsJsonArray.getJSONObject(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return books;
    }

    private static Book parseBookDetail(JSONObject json) throws Exception {
        Book book = new Book();

        // must have
        book.id = json.getString("id");
        json = json.getJSONObject("volumeInfo");

        book.title = json.getString("title");
        // converting form http to https since Google does not allow raw access anymore
        String url = json.getJSONObject("imageLinks").getString("smallThumbnail");
        book.coverImageUrl = "https" + url.substring(4);

        JSONArray authorsJSON = json.getJSONArray("authors");
        book.authors = new ArrayList<>();
        for (int i = 0; i < authorsJSON.length(); i++) {
            book.authors.add(authorsJSON.getString(i));
        }

        // nice to have
        book.description = json.getString("description");
        book.publisher = json.getString("publisher");

        JSONArray isbn = json.getJSONArray("industryIdentifiers");
        for (int i = 0; i < isbn.length(); i++) {
            JSONObject ithObject = isbn.getJSONObject(i);
            switch (ithObject.getString("type")) {
                case "ISBN_13":
                    book.isbn13 = ithObject.getString("identifier");
                    break;
                case "ISBN_10":
                    book.isbn10 = ithObject.getString("identifier");
                    break;
                default:
                    Log.v(TAG, "Unknown ISBN type: " + ithObject.getString("type"));
            }
        }

        book.date = Calendar.getInstance();

        for (SimpleDateFormat f : formats) {
            try {
                book.date.setTime(Objects.requireNonNull(f.parse(json.getString("publishedDate"))));
                return book;
            } catch (Exception ignored) {
            }
        }
        throw (new Exception("Date Format Unknown: " + json.getString("publishedDate")));
    }

    public void populateView(
            ImageView coverDisplay,
            TextView titleDisplay,
            TextView authorDisplay,
            TextView yearDisplay,
            TextView publisherDisplay,
            TextView descriptionDisplay) {
        if (coverDisplay != null) Picasso.get().load(this.coverImageUrl).into(coverDisplay);
        if (titleDisplay != null) titleDisplay.setText(this.title);
        if (authorDisplay != null) authorDisplay.setText("by: " + String.join(", ", this.authors));
        if (yearDisplay != null) yearDisplay.setText(String.valueOf(this.date.get(Calendar.YEAR)));
        if (publisherDisplay != null) publisherDisplay.setText("publisher: " + this.publisher);
        if (descriptionDisplay != null) descriptionDisplay.setText("\t" + this.description);
    }
}
