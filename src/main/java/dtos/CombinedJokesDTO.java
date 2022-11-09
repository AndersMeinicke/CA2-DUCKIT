package dtos;

public class CombinedJokesDTO {
    private String chuckJokeId;
    private String chuckJoke;
    private String chuckJokeReference;
    private String dadJoke;
    private String dadJokeReference;

    private String dadStatus;

    public CombinedJokesDTO() {
    }

    public CombinedJokesDTO(ChuckDTO chuckDTO, DadDTO dadDTO) {
        this.chuckJokeId = chuckDTO.getId();
        this.chuckJoke = chuckDTO.getValue();
        this.chuckJokeReference = chuckDTO.getUrl();
        this.dadJoke = dadDTO.getJoke();
        this.dadJokeReference = dadDTO.getUrl();
        this.dadStatus = dadDTO.getStatus();
    }
}
