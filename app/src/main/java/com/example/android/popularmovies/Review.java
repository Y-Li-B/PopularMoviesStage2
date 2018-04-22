package com.example.android.popularmovies;

class Review {
    private String author;
    private String content;

    Review(String author,String content){
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
