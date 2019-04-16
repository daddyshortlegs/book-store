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

@Controller
@SpringBootApplication
public class BookStore {

    private static final int PAGE_SIZE = 10;
    private final Logger logger = LogManager.getLogger();

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
        pageNumber = sanitisePageNumber(pageNumber);

        SearchResults searchResults = searchService.search(query, pageNumber);
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("results", searchResults.getSearchResults());
        modelAndView.addObject("query", query);

        setupPaginationButtons(pageNumber, searchResults, modelAndView);
        return modelAndView;
    }

    private void setupPaginationButtons(String pageNumber, SearchResults searchResults, ModelAndView modelAndView) {
        int totalPages = calculateTotalPages(searchResults);
        logger.info("total pages = " + totalPages + ", currentPage = " + pageNumber);
        modelAndView.addObject("totalPages", Integer.toString(totalPages));

        int pageNo = Integer.parseInt(pageNumber);
        modelAndView.addObject("previousStatus", pageNo > 0 ? "enabled" : "disabled");
        modelAndView.addObject("nextStatus", pageNo < totalPages ? "enabled" : "disabled");
        modelAndView.addObject("pageNumber", pageNumber);
        modelAndView.addObject("previousPage", Integer.toString(pageNo - 1));
        modelAndView.addObject("nextPage", Integer.toString(pageNo + 1));
    }

    private String sanitisePageNumber(String pageNumber) {
        try {
            Integer.parseInt(pageNumber);
        } catch (NumberFormatException ignored) {
            return "0";
        }
        return pageNumber;
    }

    private int calculateTotalPages(SearchResults searchResults) {
        return searchResults.getTotalItems() / PAGE_SIZE;
    }

}
