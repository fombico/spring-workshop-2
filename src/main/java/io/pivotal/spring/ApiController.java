package io.pivotal.spring;

import io.pivotal.spring.models.GameList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ApiController {

    private static final String URL = "https://games.cfapps.pez.pivotal.io/api/v1/games";
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    public ApiController(RestTemplateBuilder restTemplateBuilder) {

        this.restTemplateBuilder = restTemplateBuilder;
    }

    @RequestMapping(value = "/api/v1/games", method = RequestMethod.GET)
    public ResponseEntity<GameList> getGames() {

        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<GameList> responseEntity = restTemplate.exchange(URL, HttpMethod.GET, null, GameList.class);
        GameList gameList = responseEntity.getBody();

        return new ResponseEntity<GameList>(gameList, HttpStatus.OK);
    }

}
