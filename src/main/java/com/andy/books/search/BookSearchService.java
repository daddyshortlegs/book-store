package com.andy.books.search;

import com.andy.books.SearchResult;
import com.andy.books.SearchResultBuilder;
import com.andy.books.SearchResults;
import com.andy.books.SearchService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        return buildSearchResults(response);
    }

    private SearchResults buildSearchResults(String response) {
        JSONObject jsonObject = toJsonObject(response);
        JSONArray items = jsonObject.getJSONArray("items");
        int totalItems = jsonObject.optInt("totalItems", 0);
        return new SearchResults(totalItems, createSearchResults(items));
    }

    private JSONObject toJsonObject(String response) {
        return new JSONObject(new JSONTokener(response));
    }

    private List<SearchResult> createSearchResults(JSONArray items) {
        return IntStream.range(0, items.length()).
                mapToObj(i -> toSearchResult(items.getJSONObject(i))).collect(Collectors.toList());
    }

    private SearchResult toSearchResult(JSONObject item) {
        SearchResultBuilder searchResultBuilder = new SearchResultBuilder();

        JSONObject volumeInfo = item.getJSONObject("volumeInfo");

        searchResultBuilder.setAuthor(getAuthorsOrEmptyString(volumeInfo)).
                setTitle(volumeInfo.getString("title")).
                setPublisher(volumeInfo.optString("publisher", "")).
                setLink(volumeInfo.getString("infoLink")).
                setThumbnail(getThumbnail(volumeInfo)).
                createSearchResult();
        return searchResultBuilder.createSearchResult();
    }

    String getAuthorsOrEmptyString(JSONObject volumeInfo) {
        try {
            return getAuthors(volumeInfo);
        } catch (JSONException e) {
            return "";
        }
    }

    private String getAuthors(JSONObject volumeInfo) {
        JSONArray authors = volumeInfo.getJSONArray("authors");
        if (authors.length() == 1) return authors.getString(0);
        return buildCommaDelimitedString(authors, authors.length());
    }

    private String buildCommaDelimitedString(JSONArray authors, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(authors.getString(i));
            if (i < length - 1)
                sb.append(", ");
        }
        return sb.toString();
    }

    private String getThumbnail(JSONObject volumeInfo) {
        JSONObject imageLinks = volumeInfo.optJSONObject("imageLinks");
        String thumbnail = "";
        if (imageLinks != null) {
            thumbnail = imageLinks.optString("thumbnail", "");
        }
        return thumbnail;
    }
}
