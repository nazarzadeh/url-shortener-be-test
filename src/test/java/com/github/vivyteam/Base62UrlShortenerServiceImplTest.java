package com.github.vivyteam;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.github.vivyteam.url.api.contract.FullUrl;
import com.github.vivyteam.url.api.contract.ShortenedUrl;
import com.github.vivyteam.url.api.repository.UrlRepository;
import com.github.vivyteam.url.api.service.Base62Codec;
import com.github.vivyteam.url.api.service.Base62UrlShortenerServiceImpl;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class Base62UrlShortenerServiceImplTest {
    private static final String LONG_URL = "https://www.example.com";
    private static final String SHORT_URL = "http://localhost:8080/abcdefg";
    private static final String PORT = "8080";

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private Base62Codec codec;

    @Mock
    private Environment env;

    private Base62UrlShortenerServiceImpl service;

    @BeforeEach
    public void setUp() {
        Mockito.lenient().when(env.getProperty("local.server.port")).thenReturn(PORT);
        Mockito.lenient().when(codec.encode(anyLong())).thenReturn("abcdefg");
        Mockito.lenient().when(codec.decode(anyString())).thenReturn(12345L);
        service = new Base62UrlShortenerServiceImpl(urlRepository, codec, env);
    }

    @Test
    public void testShortenUrl() {
        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setId(12345L);
        shortenedUrl.setFullUrl(LONG_URL);
        when(urlRepository.findByFullUrl(LONG_URL)).thenReturn(Optional.empty());
        when(urlRepository.save(any())).thenReturn(shortenedUrl);

        ShortenedUrl result = service.shortenUrl(LONG_URL);

        assertNotNull(result);
        assertEquals(SHORT_URL, result.getShortenedUrl());
        assertEquals(LONG_URL, result.getFullUrl());
    }

    @Test
    public void testGetFullUrl() {
        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setFullUrl(LONG_URL);
        when(urlRepository.findById(12345L)).thenReturn(Optional.of(shortenedUrl));

        FullUrl result = service.getFullUrl(SHORT_URL);

        assertNotNull(result);
        assertEquals(LONG_URL, result.getUrl());
    }

    @Test()
    public void testFindByShortenedUrl_entityNotFoundException() {
        try {
            service.findByShortenedUrl("invalid-shortened-url");
            fail("Expected EntityNotFoundException to be thrown");
        } catch (EntityNotFoundException e) {
            assertTrue(e.getMessage().contains("No Url was found with the given shortened URL:"));
        }
    }
}
