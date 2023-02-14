package com.github.vivyteam.url.api.service;

import com.github.vivyteam.url.api.contract.FullUrl;
import com.github.vivyteam.url.api.contract.ShortenedUrl;

public interface UrlShortenerService {
    ShortenedUrl shortenUrl(String url);
    FullUrl getFullUrl(String shortenedUrl);
}
