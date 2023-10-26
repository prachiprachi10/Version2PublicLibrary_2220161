package com.example.administrator.publiclibrary;



public class admin_books {

    private String Book_ID;

    public admin_books()
    {

    }

    public admin_books(String book_ID) {
        Book_ID = book_ID;
    }

    public String getBook_ID() {
        return Book_ID;
    }

    public void setBook_ID(String book_ID) {
        Book_ID = book_ID;
    }
}
