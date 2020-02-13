package com.laioffer.usedbook.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class Book implements Parcelable {
    public final static String STATUS_POSTING = "POSTING";
    public final static String STATUS_TRADING = "TRADING";
    public final static String STATUS_SOLD = "SOLD";
    private String bookId;
    private String title;
    private String author;
    private Double price;
    private String description;
    private String sellerId;
    private String address;
    private String imgUrl;
    private String status;
    private String buyerId;

    public static final Parcelable.Creator<Book> CREATOR
            = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    private Book() {
    }

    public static class BookBuilder {
        private String bookId;
        private String title;
        private String author;
        private Double price;
        private String description;
        private String sellerId;
        private String address;
        private String imgUrl;
        private String status;
        private String buyerId;

        public BookBuilder() {
        }

        public Book build() {
            Book b = new Book();
            b.setBookId(this.bookId);
            b.setTitle(this.title);
            b.setAuthor(this.author);
            b.setPrice(this.price);
            b.setDescription(this.description);
            b.setSellerId(this.sellerId);
            b.setAddress(this.address);
            b.setImgUrl(this.imgUrl);
            b.setStatus(STATUS_POSTING);
            b.setBuyerId("");
            return b;
        }

        public BookBuilder setBookId(String bookId) {
            this.bookId = bookId;
            return this;
        }

        public BookBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public BookBuilder setAuthor(String author) {
            this.author = author;
            return this;
        }

        public BookBuilder setPrice(Double price) {
            this.price = price;
            return this;
        }

        public BookBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public BookBuilder setSellerId(String sellerId) {
            this.sellerId = sellerId;
            return this;
        }

        public BookBuilder setAddress(String address) {
            this.address = address;
            return this;
        }

        public BookBuilder setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
            return this;
        }

        public BookBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public BookBuilder setBuyerId(String buyerId) {
            this.buyerId = buyerId;
            return this;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(bookId);
        parcel.writeString(title);
        parcel.writeString(author);
        parcel.writeDouble(price);
        parcel.writeString(description);
        parcel.writeString(sellerId);
        parcel.writeString(address);
        parcel.writeString(imgUrl);
        parcel.writeString(status);
        parcel.writeString(buyerId);
    }

    private Book(Parcel in) {
        bookId = in.readString();
        title = in.readString();
        author = in.readString();
        price = in.readDouble();
        description = in.readString();
        sellerId = in.readString();
        address = in.readString();
        imgUrl = in.readString();
        status = in.readString();
        buyerId = in.readString();
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("author", author);
        result.put("price", price);
        result.put("description", description);
        result.put("sellerId", sellerId);
        result.put("address", address);
        result.put("imgUrl", imgUrl);
        result.put("status", status);
        result.put("buyerId", buyerId);
        return result;
    }
}
