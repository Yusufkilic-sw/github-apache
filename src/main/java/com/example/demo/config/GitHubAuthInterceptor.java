package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * HTTP interceptor that adds GitHub Personal Access Token to all API requests
 */
@Component
public class GitHubAuthInterceptor implements ClientHttpRequestInterceptor {

    @Value("${github.api.token:}")
    private String githubToken;

    @Override
    @NonNull
    public ClientHttpResponse intercept(@NonNull HttpRequest request, @NonNull byte[] body, 
                                       @NonNull ClientHttpRequestExecution execution) throws IOException {
        if (githubToken != null && !githubToken.isBlank()) {
            request.getHeaders().add("Authorization", "Bearer " + githubToken);
        }
        ClientHttpResponse response = execution.execute(request, body);
        return response;
    }
}
