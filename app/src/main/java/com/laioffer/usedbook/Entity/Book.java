package com.laioffer.usedbook.Entity;

public class Book {
    private String bookName;
    private String price;
    private String desciption;
    private String uploadBy;
    private String imageURL;



    public Book(String bookName, String price, String desciption, String uploadBy, String imageURL) {
        this.bookName = bookName;
        this.price = price;
        this.desciption = desciption;
        this.uploadBy = uploadBy;
        this.imageURL = imageURL;

    }

    public Book() {

    }




    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public String getUploadBy() {
        return uploadBy;
    }

    public void setUploadBy(String uploadBy) {
        this.uploadBy = uploadBy;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


}
