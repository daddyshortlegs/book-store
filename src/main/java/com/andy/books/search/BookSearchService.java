package com.andy.books.search;

import com.andy.books.SearchResult;
import com.andy.books.SearchResultBuilder;
import com.andy.books.SearchService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class BookSearchService implements SearchService {

    private Logger logger = LogManager.getLogger();

    private static final String BOOK_SEARCH_URL = "https://www.googleapis.com/books/v1/volumes";

    @Autowired
    private final HttpConnector httpConnector;

    public BookSearchService(HttpConnector httpConnector) {
        this.httpConnector = httpConnector;
    }

    @Override
    public List<SearchResult> search(String query) {
        URL theUrl = createSearchUrl(query);
        String response = httpConnector.get(theUrl);
        JSONArray items = getItemsFromJson(response);
        return createSearchResults(items);
    }

    URL createSearchUrl(String query) {
        URL theUrl = null;
        try {
            theUrl = new URL(BOOK_SEARCH_URL + "?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8.name()));
        } catch (MalformedURLException e) {
            logger.error("Invalid URL");
        } catch (UnsupportedEncodingException ignored) {
        }
        return theUrl;
    }

    private JSONArray getItemsFromJson(String response) {
        return toJsonObject(response).getJSONArray("items");
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

        searchResultBuilder.setAuthor(getAuthors(volumeInfo)).
                setTitle(volumeInfo.getString("title")).
                setPublisher(volumeInfo.optString("publisher", "")).
                setLink(volumeInfo.getString("previewLink")).
                setThumbnail(getThumbnail(volumeInfo)).
                createSearchResult();
        return searchResultBuilder.createSearchResult();
    }

    private String getAuthors(JSONObject volumeInfo) {
        JSONArray authors = volumeInfo.getJSONArray("authors");
        return authors.getString(0);
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
