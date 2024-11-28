package br.com.java.thread.async.service;

import br.com.java.thread.async.domain.StarWarsCharacter;
import br.com.java.thread.async.feign.StarWarsCharacterClient;
import br.com.java.thread.async.helper.request.AsyncRequest;
import br.com.java.thread.async.helper.request.SyncRequest;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
public class CharacterService {

    @Autowired
    private StarWarsCharacterClient client;

    @Autowired
    private AsyncRequest asyncRequest;

    @Autowired
    private SyncRequest syncRequest;


    @TimeLimiter(name = "default")
    @CircuitBreaker(name = "default", fallbackMethod = "searchCharacterDataFallback")
    @Bulkhead(name = "default", type = Bulkhead.Type.THREADPOOL)
    public CompletableFuture<StarWarsCharacter> searchCharacterData(String id, String requestType) {

        StarWarsCharacter characterSpecs = client.getCharacterSpecs(Integer.parseInt(id));
        List<String> films = characterSpecs.getFilms();
        List<String> vehicles = characterSpecs.getVehicles();
        List<String> species = characterSpecs.getSpecies();
        List<String> starships = characterSpecs.getStarships();

        List<String> urls = new ArrayList<>() ;
        urls.addAll(films);
        urls.addAll(vehicles);
        urls.addAll(species);
        urls.addAll(starships);

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
        }

        return CompletableFuture.completedFuture(characterSpecs);

    }

    /**
     *
     * Caso a API do SWAPI esteja fora do ar, o fallback será acionado
     *
     * @param id
     * @param requestType
     * @param e
     * @return
     */
    public CompletableFuture<StarWarsCharacter> searchCharacterDataFallback(String id, String requestType, Exception e) {
        return CompletableFuture.failedFuture(new RuntimeException("Falha na integração com a API externa"));
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
