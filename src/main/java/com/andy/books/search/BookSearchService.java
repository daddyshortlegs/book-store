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
    private static final String BOOK_SEARCH_URL = "https://www.googleapis.com/books/v1/volumes?q=legacy+code";
    private final HttpConnector httpConnector;

    public BookSearchService(HttpConnector httpConnector) {
        this.httpConnector = httpConnector;
    }

    @Override
    public List<SearchResult> search(String query) {
        String response = httpConnector.get(BOOK_SEARCH_URL);

        JSONObject jsonObject = toJsonObject(response);
        JSONArray items = jsonObject.getJSONArray("items");

        List<SearchResult> searchResults = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            SearchResult searchResult = toSearchResult(items.getJSONObject(i));
            searchResults.add(searchResult);
        }

        return searchResults;
    }

    private JSONObject toJsonObject(String response) {
        return new JSONObject(new JSONTokener(response));
    }

    private SearchResult toSearchResult(JSONObject item) {
        SearchResultBuilder searchResultBuilder = new SearchResultBuilder();

        JSONObject volumeInfo = item.getJSONObject("volumeInfo");
        JSONArray authors = volumeInfo.getJSONArray("authors");
        String author = authors.getString(0);
        String title = volumeInfo.getString("title");
        String publisher = volumeInfo.optString("publisher", "");
        JSONObject imageLinks = volumeInfo.optJSONObject("imageLinks");
        if (imageLinks != null) {
            String thumbnail = imageLinks.getString("thumbnail");
            searchResultBuilder.setThumbnail(thumbnail);
        }
        String previewLink = volumeInfo.getString("previewLink");

        searchResultBuilder.setAuthor(author).setTitle(title).setPublisher(publisher).setLink(previewLink);
        return searchResultBuilder.createSearchResult();
    }

}
