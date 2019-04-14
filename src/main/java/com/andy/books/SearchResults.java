package com.andy.books;

import java.util.List;

public class SearchResults {
    private final List<SearchResult> search;

    public SearchResults(List<SearchResult> search) {
        this.search = search;
    }

    public List<SearchResult> getSearchResults() {
        return search;
    }
}
