package com.andy.books;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class BookStoreTest {

    @Test
    public void shouldNotHandleSearchRequest_whenQueryIsEmpty() {
        BookStore bookStore = new BookStore();

        String query = "";
        ModelAndView modelAndView = bookStore.search(query);

        assertEquals("index", modelAndView.getViewName());
    }
}