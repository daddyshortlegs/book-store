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
        ModelAndView modelAndView = bookStore.search("", "0");

        assertEquals("index", modelAndView.getViewName());
        verify(searchService, times(0)).search(anyString(), eq("0"));
    }

    @Test
    public void shouldHandleSearchRequest() {
        ModelAndView modelAndView = bookStore.search("clean code", "0");

        assertEquals("index", modelAndView.getViewName());
        verify(searchService, times(1)).search("clean code", "0");
    }

    @Test
    public void shouldHandleSearchRequest_andReturnSomeResults() {
        List<SearchResult> searchResults = new ArrayList<>();
        searchResults.add(itemFromSearch("Title 1"));
        when(searchService.search("clean code", "0")).thenReturn(searchResults);

        ModelAndView modelAndView = bookStore.search("clean code", "0");

        verifyBookResults(searchResults, modelAndView, "0", "0");
    }

    @Test
    public void shouldHandleSearchRequest_withTenPages() {
        List<SearchResult> searchResults = createTenPagesOfBooks();
        when(searchService.search("clean code", "5")).thenReturn(searchResults);

        ModelAndView modelAndView = bookStore.search("clean code", "5");

        verifyBookResults(searchResults, modelAndView, "5", "10");
    }

    @Test
    public void shouldRedirectToErrorPage_whenFailureGettingData() {
        when(searchService.search("clean code", "0")).thenThrow(BookSearchException.class);

        ModelAndView modelAndView = bookStore.search("clean code", "0");

        assertEquals("error", modelAndView.getViewName());
        verify(searchService, times(1)).search("clean code", "0");
    }

    private void verifyBookResults(List<SearchResult> searchResults, ModelAndView modelAndView, String pageNumber, String totalPages) {
        assertEquals("index", modelAndView.getViewName());
        verify(searchService, times(1)).search("clean code", pageNumber);
        Map<String, Object> model = modelAndView.getModel();
        List<SearchResult> results = (List<SearchResult>) model.get("results");
        assertEquals(totalPages, model.get("totalPages"));
        assertEquals("clean code", model.get("query"));
        assertEquals(searchResults, results);
    }

    private List<SearchResult> createTenPagesOfBooks() {
        List<SearchResult> searchResults = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            searchResults.add(itemFromSearch("Title " + i));
        }
        return searchResults;
    }

    private SearchResult itemFromSearch(String bookName) {
        return new SearchResultBuilder().
                setAuthor("Michael Feathers").
                setTitle(bookName).
                setPublisher("Prentice Hall").
                setThumbnail("http://books.google.com/books/content?id=fB6s_Z6g0gIC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api").
                setLink("http://books.google.co.uk/books?id=fB6s_Z6g0gIC&printsec=frontcover&dq=legacy+code&hl=&cd=1&source=gbs_api").
                createSearchResult();
    }

}