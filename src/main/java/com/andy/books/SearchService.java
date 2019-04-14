package com.andy.books;

import java.util.List;

public interface SearchService {

    SearchResults searchForIt(String query, String pageNumber);

    List<SearchResult> search(String query, String pageNumber);
}
