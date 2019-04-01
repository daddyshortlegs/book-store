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
import java.util.Map;

@Controller
@SpringBootApplication
public class BookStore {

  private Logger logger = LogManager.getLogger();

  @Autowired
  private final SearchService searchService;

  public BookStore(SearchService searchService) {
    this.searchService = searchService;
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(BookStore.class, args);
  }

  @RequestMapping("/")
  String index() {
    return "index";
  }


  @RequestMapping("/hello")
  String hello(Map<String, Object> model) {
    model.put("science", "Hello from Andy's book store");
    return "searchResults";
  }

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  public ModelAndView search(@RequestParam(value="q", defaultValue="") String query) {
    logger.info("Search request was " + query);
    if (query.isEmpty())
      return new ModelAndView("index");

    List<SearchResult> searchResults = searchService.search(query);

    ModelAndView modelAndView = new ModelAndView("searchResults");
    modelAndView.addObject("results", searchResults);
    return modelAndView;

  }
}
