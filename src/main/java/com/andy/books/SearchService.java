package com.andy.books;

import java.util.List;

public interface SearchService {
    List<SearchResult> search(String query, String pageNumber);
}
