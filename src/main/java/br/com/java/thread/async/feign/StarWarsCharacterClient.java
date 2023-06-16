package br.com.java.thread.async.feign;

import br.com.java.thread.async.domain.StarWarsCharacter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "star-wars-character-api", url = "${star-wars.character.url}")
public interface StarWarsCharacterClient {

    //todo resilience4j
    @GetMapping("/{id}")
    StarWarsCharacter getCharacterSpecs(@PathVariable Integer id);


}
