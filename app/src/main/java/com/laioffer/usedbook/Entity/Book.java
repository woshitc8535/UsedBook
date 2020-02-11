package com.laioffer.usedbook.Entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private String title;
    private String author;
    private Double price;
    private String description;
    private String sellerId;
    private String address;
    private String imgUrl;

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
        private String title;
        private String author;
        private Double price;
        private String description;
        private String sellerId;
        private String address;
        private String imgUrl;

        public BookBuilder() {
        }

        public Book build() {
            Book b = new Book();
            b.setTitle(this.title);
            b.setAuthor(this.author);
            b.setPrice(this.price);
            b.setDescription(this.description);
            b.setSellerId(this.sellerId);
            b.setAddress(this.address);
            b.setImgUrl(this.imgUrl);
            return b;
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
        parcel.writeString(title);
        parcel.writeString(author);
        parcel.writeDouble(price);
        parcel.writeString(description);
        parcel.writeString(sellerId);
        parcel.writeString(address);
        parcel.writeString(imgUrl);
    }

    private Book(Parcel in) {
        title = in.readString();
        author = in.readString();
        price = in.readDouble();
        description = in.readString();
        sellerId = in.readString();
        address = in.readString();
        imgUrl = in.readString();
    }
}
