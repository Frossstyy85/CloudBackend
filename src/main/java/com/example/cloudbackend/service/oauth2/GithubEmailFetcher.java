package com.example.cloudbackend.service.oauth2;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.List;
import java.util.Map;

@Service
public class GithubEmailFetcher {

    private final RestClient restClient;

    public GithubEmailFetcher(RestClient restClient) {
        this.restClient = restClient;
    }

    public String fetchEmail(String token) {
        var emails = restClient.get()
                .uri("https://api.github.com/user/emails")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .header(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Map<String, Object>>>() {});

        if (emails == null) return null;

        return (String) emails.stream()
                .findFirst()
                .map(map -> map.get("email"))
                .orElse(null);
    }




}

