package com.andy.books;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

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
    public void shouldNotHandleSearchRequest_whenQueryIsEmpty() {
        ModelAndView modelAndView = bookStore.search("");

        assertEquals("index", modelAndView.getViewName());
        verify(searchService, times(0)).search(anyString());
    }

    @Test
    public void shouldHandleSearchRequest() {
        ModelAndView modelAndView = bookStore.search("clean code");

        assertEquals("searchResults", modelAndView.getViewName());
        verify(searchService, times(1)).search("clean code");
    }
}