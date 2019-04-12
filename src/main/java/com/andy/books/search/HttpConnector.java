package com.andy.books.search;

import org.springframework.stereotype.Component;

import java.net.URL;

@Component
interface HttpConnector {
    String get(URL theUrl);
}
