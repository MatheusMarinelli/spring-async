package br.com.java.thread.async.helper.request;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Component
@AllArgsConstructor
public class AsyncRequest {

    @Autowired
    private RestTemplate restTemplate;

    @Async
    public CompletableFuture<Boolean> makeAsyncRequest(String url) {
        try {
            restTemplate.getForObject(url, String.class);
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(false);
        }
    }


}
