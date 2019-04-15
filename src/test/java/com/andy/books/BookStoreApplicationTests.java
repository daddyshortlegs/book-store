package com.andy.books;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookStoreApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void serverStarts_andLandingPageIsMyBookStore() {
		String body = this.restTemplate.getForObject("/", String.class);

		assertTrue(body.contains("Andy's Book Store"));
	}

}

