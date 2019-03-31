package com.andy.books;

public class SearchResultBuilder {
    private String author;
    private String title;
    private String publisher;
    private String thumbnail;
    private String link;

    public SearchResultBuilder setAuthor(String author) {
        this.author = author;
        return this;
    }

    public SearchResultBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public SearchResultBuilder setPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public SearchResultBuilder setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public SearchResultBuilder setLink(String link) {
        this.link = link;
        return this;
    }

    public SearchResult createSearchResult() {
        return new SearchResult(author, title, publisher, thumbnail, link);
    }
}