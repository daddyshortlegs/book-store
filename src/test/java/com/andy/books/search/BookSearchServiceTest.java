package com.andy.books.search;

import com.andy.books.SearchResult;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookSearchServiceTest {

    private BookSearchService service;

    @Mock
    private HttpConnector httpConnector;

    @Before
    public void setup() {
        service = new BookSearchService(httpConnector);
    }

    @Test
    public void shouldSearchForLegacyCode() throws Exception {
        when(httpConnector.get(new URL("https://www.googleapis.com/books/v1/volumes?q=legacy+code"))).thenReturn(loadCannedJson());

        List<SearchResult> searchResults = service.search("legacy code");

        verifyWeGetBackCannedData(searchResults);
    }

    @Test
    public void shouldSearchForCleanCode() throws Exception {
        when(httpConnector.get(new URL("https://www.googleapis.com/books/v1/volumes?q=clean+code"))).thenReturn(loadCannedJson());

        List<SearchResult> searchResults = service.search("clean code");

        verifyWeGetBackCannedData(searchResults);
    }

    @Test
    public void shouldCreateCommaDelimtedAuthors_whenOneAuthor() {
        JSONArray authors = new JSONArray();
        authors.put("Uncle Bob");
        JSONObject volumeInfo = new JSONObject();
        volumeInfo.put("authors", authors);

        String delimtedString = service.getAuthors(volumeInfo);

        assertEquals("Uncle Bob", delimtedString);
    }

    @Test
    @Ignore
    public void shouldCreateCommaDelimtedAuthors_whenMoreThanOneAuthor() {
        JSONArray authors = new JSONArray();
        authors.put("Erich Gamma");
        authors.put("Richard Helm");
        authors.put("Ralph Johnson");
        authors.put("John Vlissides");
        JSONObject volumeInfo = new JSONObject();
        volumeInfo.put("authors", authors);

        String delimtedString = service.getAuthors(volumeInfo);

        assertEquals("Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides", delimtedString);
    }


    private void verifyWeGetBackCannedData(List<SearchResult> searchResults) {
        assertNotNull(searchResults);
        assertEquals(10, searchResults.size());
        SearchResult searchResult1 = searchResults.get(0);

        assertEquals("Working Effectively with Legacy Code", searchResult1.getTitle());
        assertEquals("Michael Feathers", searchResult1.getAuthor());
        assertEquals("Prentice Hall Professional", searchResult1.getPublisher());
        assertEquals("http://books.google.com/books/content?id=fB6s_Z6g0gIC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api",
                searchResult1.getThumbnail());
        assertEquals("http://books.google.co.uk/books?id=fB6s_Z6g0gIC&printsec=frontcover&dq=legacy+code&hl=&cd=1&source=gbs_api",
                searchResult1.getLink());

        SearchResult searchResult2 = searchResults.get(1);
        assertEquals("Working Effectively with Legacy Code", searchResult2.getTitle());
        assertEquals("Michael C. Feathers", searchResult2.getAuthor());
        assertEquals("Prentice Hall", searchResult2.getPublisher());
        assertEquals("http://books.google.com/books/content?id=vlo_nWophSYC&printsec=frontcover&img=1&zoom=1&source=gbs_api",
                searchResult2.getThumbnail());
        assertEquals("http://books.google.co.uk/books?id=vlo_nWophSYC&q=legacy+code&dq=legacy+code&hl=&cd=2&source=gbs_api",
                searchResult2.getLink());
    }

    private String loadCannedJson() throws URISyntaxException, IOException {
        URL resource = getClass().getResource("/legacy_code.json");
        FileInputStream fileInputStream = new FileInputStream(new File(resource.toURI()));
        return IOUtils.toString(fileInputStream, StandardCharsets.UTF_8.name());
    }
}
