package com.github.vivyteam.url.api.repository;

import com.github.vivyteam.url.api.contract.ShortenedUrl;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository<ShortenedUrl, Long> {
    Optional<ShortenedUrl> findByFullUrl(String url);
}