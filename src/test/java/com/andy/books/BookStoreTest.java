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
    public void shouldHandleSearchRequest_andReturnSomeResults() {
        List<SearchResult> results = new ArrayList<>();
        results.add(itemFromSearch("Title 1"));
        SearchResults searchResults = new SearchResults(1, results);
        String pageNumber = "0";
        when(searchService.search("clean code", pageNumber)).thenReturn(searchResults);

        ModelAndView modelAndView = bookStore.search("clean code", pageNumber);

        verifyBookResults(searchResults, modelAndView, pageNumber, "0");
        verifyPreviousButtonSetTo(modelAndView, "disabled");
        verifyNextButtonSetTo(modelAndView, "disabled");
    }

    @Test
    public void shouldHandleSearchRequest_withTenPages() {
        SearchResults searchResults = createTenPagesOfBooks();
        String pageNumber = "5";
        when(searchService.search("clean code", pageNumber)).thenReturn(searchResults);

        ModelAndView modelAndView = bookStore.search("clean code", pageNumber);

        verifyBookResults(searchResults, modelAndView, pageNumber, "10");
        verifyPreviousButtonSetTo(modelAndView, "enabled");
        verifyNextButtonSetTo(modelAndView, "enabled");

        Map<String, Object> model = modelAndView.getModel();
        assertEquals("4", model.get("previousPage"));
        assertEquals("6", model.get("nextPage"));
    }

    @Test
    public void shouldHandleSearchRequest_andHideNextButtonWhenOnLastPage() {
        SearchResults searchResults = createTenPagesOfBooks();
        String pageNumber = "10";
        when(searchService.search("clean code", pageNumber)).thenReturn(searchResults);

        ModelAndView modelAndView = bookStore.search("clean code", pageNumber);

        verifyBookResults(searchResults, modelAndView, pageNumber, "10");
        verifyPreviousButtonSetTo(modelAndView, "enabled");
        verifyNextButtonSetTo(modelAndView, "disabled");

        Map<String, Object> model = modelAndView.getModel();
        assertEquals("9", model.get("previousPage"));
    }

    @Test
    public void shouldRedirectToErrorPage_whenFailureGettingData() {
        when(searchService.search("clean code", "0")).thenThrow(BookSearchException.class);

        ModelAndView modelAndView = bookStore.search("clean code", "0");

        assertEquals("error", modelAndView.getViewName());
        verify(searchService, times(1)).search("clean code", "0");
    }

    private void verifyPreviousButtonSetTo(ModelAndView modelAndView, String buttonStatus) {
        Map<String, Object> model = modelAndView.getModel();
        assertEquals(buttonStatus, model.get("previousStatus"));
    }

    private void verifyNextButtonSetTo(ModelAndView modelAndView, String buttonStatus) {
        Map<String, Object> model = modelAndView.getModel();
        assertEquals(buttonStatus, model.get("nextStatus"));
    }

    private void verifyBookResults(SearchResults searchResults, ModelAndView modelAndView, String pageNumber, String totalPages) {
        assertEquals("index", modelAndView.getViewName());
        verify(searchService, times(1)).search("clean code", pageNumber);
        Map<String, Object> model = modelAndView.getModel();
        List<SearchResult> results = (List<SearchResult>) model.get("results");
        assertEquals(pageNumber, model.get("pageNumber"));
        assertEquals(totalPages, model.get("totalPages"));
        assertEquals("clean code", model.get("query"));
        assertEquals(searchResults.getSearchResults(), results);
    }

    private SearchResults createTenPagesOfBooks() {
        List<SearchResult> results = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            results.add(itemFromSearch("Title " + i));
        }

        return new SearchResults(100, results);
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