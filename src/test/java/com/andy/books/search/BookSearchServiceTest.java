package com.andy.books.search;

import com.andy.books.SearchResult;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookSearchServiceTest {

    BookSearchService service;

    @Mock
    HttpConnector httpConnector;

    @Before
    public void setup() {
        service = new BookSearchService(httpConnector);
    }

    @Test
    public void shouldPerformSearch() throws Exception {
        URL resource = getClass().getResource("/legacy_code.json");
        FileInputStream fileInputStream = new FileInputStream(new File(resource.toURI()));
        String json = IOUtils.toString(fileInputStream, StandardCharsets.UTF_8.name());

        when(httpConnector.get("https://www.googleapis.com/books/v1/volumes?q=legacy+code")).thenReturn(json);

        List<SearchResult> searchResults = service.search("clean code");

        assertNotNull(searchResults);
        assertEquals(1, searchResults.size());
        SearchResult searchResult = searchResults.get(0);

        assertEquals("Working Effectively with Legacy Code", searchResult.getTitle());
        assertEquals("Michael Feathers", searchResult.getAuthor());
        assertEquals("Prentice Hall Professional", searchResult.getPublisher());
        assertEquals("http://books.google.com/books/content?id=fB6s_Z6g0gIC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api",
                searchResult.getThumbnail());
        assertEquals("http://books.google.co.uk/books?id=fB6s_Z6g0gIC&printsec=frontcover&dq=legacy+code&hl=&cd=1&source=gbs_api",
                searchResult.getLink());
    }
}
