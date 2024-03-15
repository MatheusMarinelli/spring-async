package br.com.java.thread.async.service;

import br.com.java.thread.async.domain.StarWarsFilm;
import br.com.java.thread.async.feign.StarWarsFilmClient;
import br.com.java.thread.async.helper.request.AsyncRequest;
import br.com.java.thread.async.helper.request.SyncRequest;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class FilmService {

    @Autowired
    private StarWarsFilmClient client;

    @Autowired
    private AsyncRequest asyncRequest;

    @Autowired
    private SyncRequest syncRequest;


    @TimeLimiter(name = "default")
    @Bulkhead(name = "default", type = Bulkhead.Type.THREADPOOL)
    public CompletableFuture<StarWarsFilm> getFilm(Integer id, String requestType) {
        StarWarsFilm filmSpecs = client.getFilmSpecs(id);

        List<String> characters = filmSpecs.getCharacters();
        List<String> species = filmSpecs.getSpecies();
        List<String> starships = filmSpecs.getStarships();
        List<String> planets = filmSpecs.getPlanets();
        List<String> vehicles = filmSpecs.getVehicles();

        List<String> urls = new ArrayList<>();
        urls.addAll(characters);
        urls.addAll(species);
        urls.addAll(starships);
        urls.addAll(planets);
        urls.addAll(vehicles);

        if (requestType.equalsIgnoreCase("async")) {
            try {
                searchForSpecs(urls);
            } catch (ExecutionException | InterruptedException e) {
                log.error("Erro: ", e.getCause());
                throw new RuntimeException(e);
            }
        } else {
            log.info("Starting sync requests at {}", LocalDateTime.now());
            urls.forEach(url -> syncRequest.makeSyncRequest(url,urls.indexOf(url)));
            log.info("Finishing sync requests at {}", LocalDateTime.now());
            log.info("Total of requests {}", urls.size());
        }

        return CompletableFuture.completedFuture(filmSpecs);
    }

    public void searchForSpecs(List<String> urls) throws ExecutionException, InterruptedException {

        CompletableFuture<Boolean>[] futures = new CompletableFuture[urls.size()];

        log.info("Starting async requests at {}", LocalDateTime.now());

        for (int i = 0; i < urls.size(); i++) {
            String url = urls.get(i);
            futures[i] = asyncRequest.makeAsyncRequest(url);
        }

        // aguarda a execução de todas as chamadas
        CompletableFuture.allOf((futures)).join();

        AtomicBoolean allRequestsSucceded = new AtomicBoolean(true);

        for(CompletableFuture<Boolean> future : futures) {
            if (!future.get()) {
                allRequestsSucceded.set(false);
                break;
            }
        }

        if (allRequestsSucceded.get()) {
            log.info("Finishing async requests at {}", LocalDateTime.now());
            log.info("All requests were made successfully");
            log.info("Total of requests {}", futures.length);
        } else  {
            log.error("One or more requests have been failed");
        }
    }




}
