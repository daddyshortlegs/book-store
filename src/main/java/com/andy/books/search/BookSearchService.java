package com.andy.books.search;

import com.andy.books.SearchResult;
import com.andy.books.SearchResultBuilder;
import com.andy.books.SearchService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class BookSearchService implements SearchService {
    private final HttpConnector httpConnector;

    public BookSearchService(HttpConnector httpConnector) {
        this.httpConnector = httpConnector;
    }

    @Override
    public List<SearchResult> search(String query) {
        String response = httpConnector.get("https://www.googleapis.com/books/v1/volumes?q=legacy+code");

        JSONTokener tokener = new JSONTokener(response);
        JSONObject jsonObject = new JSONObject(tokener);

        List<SearchResult> searchResults = new ArrayList<>();


        JSONArray items = jsonObject.getJSONArray("items");
        JSONObject item1 = items.getJSONObject(0);


        JSONObject volumeInfo = item1.getJSONObject("volumeInfo");

        JSONArray authors = volumeInfo.getJSONArray("authors");
        String author1 = authors.getString(0);

        String title = volumeInfo.getString("title");

        String publisher = volumeInfo.getString("publisher");

        JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
        String thumbnail = imageLinks.getString("thumbnail");

        String previewLink = volumeInfo.getString("previewLink");

        SearchResult searchResult = new SearchResultBuilder().
                setAuthor(author1).setTitle(title).setPublisher(publisher).setThumbnail(thumbnail).setLink(previewLink).createSearchResult();


        searchResults.add(searchResult);

        return searchResults;
    }
}
