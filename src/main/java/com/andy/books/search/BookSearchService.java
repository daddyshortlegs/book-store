package com.andy.books.search;

import com.andy.books.SearchResults;
import com.andy.books.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class BookSearchService implements SearchService {

    @Autowired
    private final HttpConnector httpConnector;

    public BookSearchService(HttpConnector httpConnector) {
        this.httpConnector = httpConnector;
    }

    @Override
    public SearchResults search(String query, String pageNumber) {
        URL theUrl = GoogleBooksUrl.createSearchQuery(query, pageNumber);
        String response = httpConnector.get(theUrl);

        BookJsonUnmarshaller unmarshaller = new BookJsonUnmarshaller();
        return unmarshaller.buildSearchResults(response);
    }


}
