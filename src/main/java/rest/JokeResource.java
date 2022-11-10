package rest;

import com.google.gson.Gson;
import dtos.ChuckDTO;
import dtos.DadDTO;
import dtos.CombinedJokesDTO;
import utils.HttpUtils;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;

@Path("jokes")
public class JokeResource {

    @Context
    private UriInfo context;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJokes() throws IOException {
        Gson gson = new Gson();
        String chuck = HttpUtils.fetchData("https://api.chucknorris.io/jokes/random");
        String dad = HttpUtils.fetchData("https://icanhazdadjoke.com");
        ChuckDTO chuckDTO = gson.fromJson(chuck, ChuckDTO.class);
        DadDTO dadDTO = gson.fromJson(dad, DadDTO.class);
        CombinedJokesDTO combinedJokesDTO = new CombinedJokesDTO(chuckDTO, dadDTO);
        return gson.toJson(combinedJokesDTO);
    }
}
