package com.example.newsfeed;

public class NewsModel {
    private String author;
    private String title;
    private String content;
    private String date;
    private String imageUrl;
    private String readMoreUrl;
    private String inShortsUrl;


    public NewsModel(String author, String title, String content, String date, String imageUrl, String readMoreUrl,String inShortsUrl) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.date = date;
        this.imageUrl = imageUrl;
        this.readMoreUrl = readMoreUrl;
        this.inShortsUrl = inShortsUrl;
    }

    public String getInShortsUrl() { return inShortsUrl; }

    public String getReadMoreUrl() { return readMoreUrl; }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
