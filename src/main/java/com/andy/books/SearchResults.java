package com.andy.books;

import java.util.List;

public class SearchResults {
    private final int totalItems;
    private final List<SearchResult> search;

    public SearchResults(int totalItems, List<SearchResult> search) {
        this.totalItems = totalItems;
        this.search = search;
    }

    public List<SearchResult> getSearchResults() {
        return search;
    }

    public int getTotalItems() {
        return totalItems;
    }
}
