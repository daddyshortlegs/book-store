package com.andy.books;

import org.springframework.stereotype.Component;

import java.util.List;

public interface SearchService {
    List<SearchResult> search(String query);
}
