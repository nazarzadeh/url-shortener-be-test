package com.github.vivyteam.url.api.service;

import com.github.vivyteam.url.api.contract.FullUrl;
import com.github.vivyteam.url.api.contract.ShortenedUrl;
import com.github.vivyteam.url.api.repository.UrlRepository;
import java.net.InetAddress;
import java.util.Objects;
import javax.persistence.EntityNotFoundException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class Base62UrlShortenerServiceImpl implements UrlShortenerService {
    private final UrlRepository urlRepository;
    private final Base62Codec codec;
    private final Environment env;


    public Base62UrlShortenerServiceImpl(UrlRepository urlRepository, Base62Codec codec, Environment env) {
        this.codec = codec;
        this.urlRepository = urlRepository;
        this.env = env;
    }

    public FullUrl getFullUrl(String shortenedUrl) {
        ShortenedUrl url = findByShortenedUrl(shortenedUrl);
        return new FullUrl(url.getFullUrl());
    }

    public ShortenedUrl shortenUrl(String url) {
        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setFullUrl(url);

        ShortenedUrl savedUrl = urlRepository.findByFullUrl(url)
                .orElseGet(() -> urlRepository.save(shortenedUrl));

        savedUrl.setShortenedUrl(Objects
                .requireNonNull(getBaseAddr())
                .concat(codec.encode(savedUrl.getId())));
        return savedUrl;
    }

    public ShortenedUrl findByShortenedUrl(String shortenedUrl) throws EntityNotFoundException {
        long id = codec.decode(shortenedUrl);
        return urlRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No Url was found with the given shortened URL:" + shortenedUrl));
    }

    private String getBaseAddr(){
        String hostAddress = InetAddress.getLoopbackAddress().getHostName();
        String port = env.getProperty("local.server.port");
        return String.format("http://%s:%s/",hostAddress,port);

    }
}
