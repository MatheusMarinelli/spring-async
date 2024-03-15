package br.com.java.thread.async.feign;

import br.com.java.thread.async.domain.StarWarsFilm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "star-wars-film-api", url = "${star-wars.film.url}")
public interface StarWarsFilmClient {

    @GetMapping("/{id}")
    StarWarsFilm getFilmSpecs(@PathVariable Integer id);


}
