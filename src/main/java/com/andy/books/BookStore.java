package com.andy.books;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@SpringBootApplication
public class BookStore {

    public static final int PAGE_SIZE = 10;
    private Logger logger = LogManager.getLogger();

    @Autowired
    private final SearchService searchService;

    public BookStore(SearchService searchService) {
        this.searchService = searchService;
    }

    public static void main(String[] args) {
        SpringApplication.run(BookStore.class, args);
    }

    @RequestMapping("/")
    String searchLandingPage() {
        return "index";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    ModelAndView search(@RequestParam(value = "q", defaultValue = "") String query, @RequestParam(value = "page", defaultValue = "0") String pageNumber) {
        logger.info("Search request was " + query);
        if (query.isEmpty())
            return new ModelAndView("index");

        return displayResultsOrErrorMessage(query, pageNumber);
    }

    private ModelAndView displayResultsOrErrorMessage(String query, String pageNumber) {
        try {
            return doSearchAndDisplayResults(query, pageNumber);
        }
        catch (BookSearchException e) {
            return new ModelAndView("error");
        }
    }

    private ModelAndView doSearchAndDisplayResults(String query, String pageNumber) {
        List<SearchResult> searchResults = searchService.search(query, pageNumber);
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("results", searchResults);
        modelAndView.addObject("totalPages", calulcateTotalPages(searchResults));
        return modelAndView;
    }

    private String calulcateTotalPages(List<SearchResult> searchResults) {
        return Integer.toString(searchResults.size() / PAGE_SIZE);
    }
}
