# URL Shortener API
This project implements a URL shortening service. It provides the functionality to shorten a given URL and retrieve the original URL from the shortened URL.

In this project, we use a unique approach to URL shortening by encoding the URL into a compact string using base62 encoding.

The base62 encoding scheme is an efficient way to convert a long URL into a smaller string representation while still preserving the unique properties of the original URL.

By using base62 encoding, we are able to produce shortened URLs that are more memorable, easily shareable and take up less space than traditional URL shortening techniques.
## Getting started
The following instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites
You will need to have the following installed on your machine:

Java 8 or later

Gradle 4.10.2 or later

### Running the tests
The tests can be run using the following command:
```
./gradlew test
```
## API
The API provides three endpoints for URL shortening and retrieval:

### Shorten a URL
````
GET /{url}/short
````
Shortens the given URL and returns the shortened URL.

### Get the original URL
````
GET /{shortenedUrl}/full
````
Retrieves the original URL from the shortened URL.

### Redirect to the original URL
````
GET /{shortenedUrl}
````
Redirects to the original URL.

## Built with
Java - Programming language

Gradle - Build tool

Spring Boot - Application framework

Reactor - Reactive programming library

## Authors
Vivy Team - Initial work

Amir N Ziksari