package com.github.vivyteam.url.api;

import com.github.vivyteam.url.api.contract.FullUrl;
import com.github.vivyteam.url.api.contract.ShortenedUrl;
import com.github.vivyteam.url.api.service.Base62UrlShortenerServiceImpl;
import com.github.vivyteam.url.api.service.UrlShortenerService;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class UrlApi {

    private final UrlShortenerService shortenerService;

    public UrlApi(Base62UrlShortenerServiceImpl shortenerService) {
        this.shortenerService = shortenerService;
    }

    @GetMapping("/{url}/short")
    public Mono<ShortenedUrl> shortUrl(@PathVariable final String url) {
        return Mono.just(shortenerService.shortenUrl(url));
    }

    @GetMapping("/{shortenedUrl}/full")
    public Mono<ResponseEntity<Object>>  getFullUrl(@PathVariable final String shortenedUrl) {
        try {
            FullUrl fullUrl = shortenerService.getFullUrl(shortenedUrl);
            return Mono.just(ResponseEntity.status(HttpStatus.FOUND).body(fullUrl));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            e.printStackTrace();
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
        }
    }

    @GetMapping("/{shortenedUrl}")
    public Mono<ResponseEntity<Void>> redirect(@PathVariable final String shortenedUrl) {
        FullUrl fullUrl = shortenerService.getFullUrl(shortenedUrl);
        return Mono.just(ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(fullUrl.getUrl()))
                .build());
    }
}