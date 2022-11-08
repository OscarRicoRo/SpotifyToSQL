package es.ulpgc.spotify.downloader.spotify.schemas;

import java.util.List;

public class AlbumSchema {
    private final String id;
    private final String name;
    private final String album_type;
    private final String release_date;
    private final Integer total_tracks;
    List<ArtistSchema> artists;

    public AlbumSchema(String album_type, String id, String name, String release_date, Integer total_tracks, List<ArtistSchema> artists) {
        this.album_type = album_type;
        this.id = id;
        this.name = name;
        this.release_date = release_date;
        this.total_tracks = total_tracks;
        this.artists = artists;
    }

    public String getAlbum_type() {
        return album_type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRelease_date() {
        return release_date;
    }

    public Integer getTotal_tracks() {
        return total_tracks;
    }

    public List<ArtistSchema> getArtists() {
        return artists;
    }
}
