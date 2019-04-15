package com.andy.books.search;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BookJsonUnmarshallerTest {

    private BookJsonUnmarshaller bookJsonUnmarshaller;

    @Before
    public void setup() {
        bookJsonUnmarshaller = new BookJsonUnmarshaller();
    }
    @Test
    public void shouldCreateEmptyString_whenNoAuthors() {
        JSONObject volumeInfo = new JSONObject();
        volumeInfo.put("title", "BDD In Action");

        String delimitedString = bookJsonUnmarshaller.getAuthorsOrEmptyString(volumeInfo);

        assertEquals("", delimitedString);
    }

    @Test
    public void shouldCreateCommaDelimtedAuthors_whenOneAuthor() {
        JSONArray authors = new JSONArray();
        authors.put("Uncle Bob");
        JSONObject volumeInfo = new JSONObject();
        volumeInfo.put("authors", authors);

        String delimitedString = bookJsonUnmarshaller.getAuthorsOrEmptyString(volumeInfo);

        assertEquals("Uncle Bob", delimitedString);
    }

    @Test
    public void shouldCreateCommaDelimtedAuthors_whenMoreThanOneAuthor() {
        JSONArray authors = new JSONArray();
        authors.put("Erich Gamma");
        authors.put("Richard Helm");
        authors.put("Ralph Johnson");
        authors.put("John Vlissides");
        JSONObject volumeInfo = new JSONObject();
        volumeInfo.put("authors", authors);

        String delimitedString = bookJsonUnmarshaller.getAuthorsOrEmptyString(volumeInfo);

        assertEquals("Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides", delimitedString);
    }

}