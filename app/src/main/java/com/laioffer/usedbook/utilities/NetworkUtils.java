package com.laioffer.usedbook.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

public class NetworkUtils {
    final private static String BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes";
    final private static String mApiKey = "AIzaSyCiFDxKF18-wagUK8mUGpnMJqhLB3x1SDg";

    public static URL buildBookSearchUrl(String query, String searchColumn) {
        Uri uri = Uri.parse(BOOK_BASE_URL).buildUpon()
                .appendQueryParameter("q", searchColumn + query)
                .appendQueryParameter("printType", "books")
                .appendQueryParameter("maxResults", "40")
                .appendQueryParameter("key", mApiKey)
                .build();
        return uri2url(uri);
    }

    private static URL uri2url(Uri uri) {
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URL " + url);

        return url;
    }

    public static String getResponse(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}

