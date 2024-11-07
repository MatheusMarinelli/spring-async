package br.com.java.thread.async.helper.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@AllArgsConstructor
public class AsyncRequest {

    @Autowired
    private RestTemplate restTemplate;

    @Async // cria threads apartadas para executar o método abaixo baseado no método de configuração da classe AsyncConfig
    public CompletableFuture<Boolean> makeAsyncRequest(String url) {
        try {
            restTemplate.getForObject(url, String.class);
            log.warn("Thread: " + Thread.currentThread().getName());
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(false);
        }
    }


}
