package com.andy.books;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BookStoreTest {

    @Mock
    private SearchService searchService;

    @Test
    public void shouldNotHandleSearchRequest_whenQueryIsEmpty() {
        BookStore bookStore = new BookStore(searchService);

        String query = "";
        ModelAndView modelAndView = bookStore.search(query);

        assertEquals("index", modelAndView.getViewName());
        verify(searchService, times(0)).search(anyString());
    }
}