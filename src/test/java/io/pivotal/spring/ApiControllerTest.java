package io.pivotal.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pivotal.spring.models.Game;
import io.pivotal.spring.models.GameList;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiControllerTest {

    private MockMvc mockMvc;
    private MockRestServiceServer mockRestServiceServer;

    @Before
    public void setup() {
        RestTemplate restTemplate = new RestTemplate();

        RestTemplateBuilder restTemplateBuilder = mock(RestTemplateBuilder.class);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);

        ApiController apiController = new ApiController(restTemplateBuilder);
        mockMvc = MockMvcBuilders.standaloneSetup(apiController).build();

        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void shouldReturnGames() throws Exception {
        Game game = new Game();
        game.setName("Game 0");
        game.setPrice(100);

        Game game2 = new Game();
        game2.setName("Game 2");
        game2.setPrice(200);

        GameList gameList = new GameList();
        gameList.setGames(Arrays.asList(game, game2));
        gameList.setCount(2);

        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(gameList);

        mockRestServiceServer.expect(requestTo("https://games.cfapps.pez.pivotal.io/api/v1/games"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        String resultResponse = mockMvc.perform(get("/api/v1/games"))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString();

        GameList resultGameList = objectMapper.readValue(resultResponse, GameList.class);
        assertThat(resultGameList).isEqualTo(gameList);

        mockRestServiceServer.verify();
    }

}