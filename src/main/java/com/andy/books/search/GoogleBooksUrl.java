package com.andy.books.search;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

class GoogleBooksUrl {

    private static Logger logger = LogManager.getLogger();

    private static final String BOOK_SEARCH_URL = "https://www.googleapis.com/books/v1/volumes";

    static URL createSearchQuery(String query, String pageNumber) {
        URL theUrl = null;
        try {
            theUrl = new URL(BOOK_SEARCH_URL + "?" + createQueryString(query) + addPageNumber(pageNumber));
        } catch (MalformedURLException e) {
            logger.error("Invalid URL");
        } catch (UnsupportedEncodingException ignored) {
        }
        return theUrl;
    }

    private static String addPageNumber(String pageNumber) {
        if (pageNumber == null || pageNumber.equals("")) { return ""; }
        int i = Integer.parseInt(pageNumber);
        return i > 0 ? "&startIndex=" + pageNumber : "";
    }

    private static String createQueryString(String query) throws UnsupportedEncodingException {
        return "q=" + URLEncoder.encode(createDefaultQueryIfEmpty(query), StandardCharsets.UTF_8.name());
    }

    private static String createDefaultQueryIfEmpty(String query) {
        if (query.isEmpty()) {
            query = "\"\"";
        }
        return query;
    }

}
