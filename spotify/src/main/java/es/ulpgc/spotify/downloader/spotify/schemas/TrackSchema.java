package es.ulpgc.spotify.downloader.spotify.schemas;

import java.util.List;

public class TrackSchema {
    private final String id;
    private final String name;
    private final Boolean explicit;
    private final Integer duration_ms;
    List<ArtistSchema> artists;


    public TrackSchema(List<ArtistSchema> artists, Integer duration_ms, Boolean explicit, String id, String name) {
        this.artists = artists;
        this.duration_ms = duration_ms;
        this.explicit = explicit;
        this.id = id;
        this.name = name;
    }

    public List<ArtistSchema> getArtists() {
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
