package com.andy.books.search;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public interface HttpConnector {
    String get(String url);
}
