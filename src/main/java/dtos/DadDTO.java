package dtos;

public class DadDTO {

    private String url = "https://icanhazdadjoke.com/";
    private String joke;

    private String status;

    public DadDTO(String url, String joke, String status) {
        this.url = url;
        this.joke = joke;
        this.status = status;
    }

    public DadDTO() {
    }

    public String getUrl() {
        return url;
    }

    public String getJoke() {
        return joke;
    }

    public String getStatus() {
        return status;
    }
}
