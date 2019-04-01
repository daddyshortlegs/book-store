package com.andy.books;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookStoreTest {

    @Mock
    private SearchService searchService;

    private BookStore bookStore;

    @Before
    public void setup() {
        bookStore = new BookStore(searchService);
    }


    @Test
    public void shouldGetLandingPage() {
        assertEquals("index", bookStore.searchLandingPage());
    }

    @Test
    public void shouldNotHandleSearchRequest_whenQueryIsEmpty() {
        ModelAndView modelAndView = bookStore.search("");

        assertEquals("index", modelAndView.getViewName());
        verify(searchService, times(0)).search(anyString());
    }

    @Test
    public void shouldHandleSearchRequest() {
        ModelAndView modelAndView = bookStore.search("clean code");

        assertEquals("index", modelAndView.getViewName());
        verify(searchService, times(1)).search("clean code");
    }

    @Test
    public void shouldHandleSearchRequest_andReturnSomeResults() {
        SearchResult searchResult = new SearchResultBuilder().
                setAuthor("Michael Feathers").
                setTitle("Working Effectively With Legacy Code").
                setPublisher("Prentice Hall").
                setThumbnail("http://books.google.com/books/content?id=fB6s_Z6g0gIC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api").
                setLink("http://books.google.co.uk/books?id=fB6s_Z6g0gIC&printsec=frontcover&dq=legacy+code&hl=&cd=1&source=gbs_api").
                createSearchResult();

        List<SearchResult> searchResults = new ArrayList<>();
        searchResults.add(searchResult);
        when(searchService.search("clean code")).thenReturn(searchResults);

        ModelAndView modelAndView = bookStore.search("clean code");

        assertEquals("index", modelAndView.getViewName());

        Map<String, Object> model = modelAndView.getModel();
        List<SearchResult> results = (List<SearchResult>) model.get("results");

        verify(searchService, times(1)).search("clean code");
        assertEquals(results, searchResults);
    }

}