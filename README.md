# Andy's Books

A simple app to demo connecting to the Google Books API to display a simple Book Store (although you cannot purchase anything).

## Running the tests

```sh
$ ./gradlew test
```

## Running Locally

```sh
$ cd book-store
$ ./gradlew build
$ heroku local web
```

Your app should now be running on [localhost:5000](http://localhost:5000/).

## Notes

It's a very basic UI and is just functional, no bells or whistle. The main point of this is to try out the Google Books API and do some TDD with Spring Boot.

No real validation is used on the search query, the main reason being that Google's API does this already and I'm pretty sure it is pretty robust about checking for dodgy data. So this has been left out.
