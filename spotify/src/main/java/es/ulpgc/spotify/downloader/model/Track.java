package es.ulpgc.spotify.downloader.model;

public class Track {
    private final String id;
    private final String name;
    private final Boolean explicit;
    private final Integer duration_ms;
    private final String artists;


    public Track(String artists, Integer duration_ms, Boolean explicit, String id, String name) {
        this.artists = artists;
        this.duration_ms = duration_ms;
        this.explicit = explicit;
        this.id = id;
        this.name = name;
    }

    public String getArtists() {
        return artists;
    }

    public Integer getDuration_ms() {
        return duration_ms;
    }

    public Boolean getExplicit() {
        return explicit;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
