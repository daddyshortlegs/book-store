package com.andy.books;

class SearchResultBuilder {
    private String author;
    private String title;
    private String publisher;
    private String thumbnail;
    private String link;

    SearchResultBuilder setAuthor(String author) {
        this.author = author;
        return this;
    }

    SearchResultBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    SearchResultBuilder setPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    SearchResultBuilder setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    SearchResultBuilder setLink(String link) {
        this.link = link;
        return this;
    }

    SearchResult createSearchResult() {
        return new SearchResult(author, title, publisher, thumbnail, link);
    }
}