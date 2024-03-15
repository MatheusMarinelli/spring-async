package br.com.java.thread.async.controller;

import br.com.java.thread.async.domain.StarWarsCharacter;
import br.com.java.thread.async.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class CharacterController {

    @Autowired
    private CharacterService characterService;

    @GetMapping("/character/{id}")
    public ResponseEntity<StarWarsCharacter> searchEveryCharacterData(@PathVariable String id,
                                                                      @RequestHeader("request-type") String requestType) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(characterService.searchCharacterData(id,requestType).get());
    }

}
