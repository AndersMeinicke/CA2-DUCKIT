package dtos;

public class ChuckDTO {

    private String id;

    private String url = "https://api.chucknorris.io/jokes/random";
    private String value;

    public ChuckDTO(String id, String url, String value) {
        this.id = id;
        this.url = url;
        this.value = value;
    }

    public ChuckDTO() {
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getValue() {
        return value;
    }

}
