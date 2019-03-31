package com.andy.books;

public class SearchResult {
    private final String author;
    private final String title;
    private final String publisher;
    private final String thumbnail;
    private final String link;

    SearchResult(String author, String title, String publisher, String thumbnail, String link) {
        this.author = author;
        this.title = title;
        this.publisher = publisher;
        this.thumbnail = thumbnail;
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getLink() {
        return link;
    }
}
