package br.com.java.thread.async.controller;

import br.com.java.thread.async.domain.StarWarsFilm;
import br.com.java.thread.async.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class FilmController {

    @Autowired
    private FilmService service;

    @GetMapping("/film/{id}")
    public ResponseEntity<StarWarsFilm> getFilm(@PathVariable Integer id,
                                                @RequestHeader("request-type") String requestType) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(service.getFilm(id,requestType).get());
    }

}
