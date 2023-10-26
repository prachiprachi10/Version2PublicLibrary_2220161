package com.example.administrator.publiclibrary;



public class my_books {


    private String Book_ID,Book_Name,Book_Category,Borrowed_Date,Return_Date;

    public my_books()
    {

    }
    public my_books(String Return_Date, String Borrowed_Date, String Book_Category, String Book_Name, String Book_ID) {
        this.Return_Date = Return_Date;
        this.Borrowed_Date = Borrowed_Date;
        this.Book_Category = Book_Category;
        this.Book_Name = Book_Name;
        this.Book_ID = Book_ID;
    }

    public String getBook_ID() {
        return Book_ID;
    }

    public void setBook_ID(String Book_ID) {
        this.Book_ID = Book_ID;
    }

    public String getReturn_Date() {
        return Return_Date;
    }

    public void setReturn_Date(String Return_Date) {
        this.Return_Date = Return_Date;
    }

    public String getBorrowed_Date() {
        return Borrowed_Date;
    }

    public void setBorrowed_Date(String Borrowed_Date) {
        this.Borrowed_Date = Borrowed_Date;
    }

    public String getBook_Category() {
        return Book_Category;
    }

    public void setBook_Category(String Book_Category) {
        this.Book_Category = Book_Category;
    }

    public String getBook_Name() {
        return Book_Name;
    }

    public void setBook_Name(String Book_Name) {
        this.Book_Name = Book_Name;
    }







}
