package es.ulpgc.spotify.downloader.spotify;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import es.ulpgc.spotify.downloader.spotify.schemas.AlbumSchema;
import es.ulpgc.spotify.downloader.spotify.schemas.ArtistSchema;
import es.ulpgc.spotify.downloader.spotify.schemas.TrackSchema;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spotify {
    private final SpotifyAccessor accessor = new SpotifyAccessor();
    private final Gson gson = new Gson();
    private int globalRequests;

    public Spotify() throws Exception {
        this.globalRequests = 0;
    }

    public List<ArtistSchema> downloadArtists(String artistsIds) throws Exception {
        List<ArtistSchema> artistList = new ArrayList<>();
        String json = accessor.get("/artists", artistParams(artistsIds));
        artistsJsonParser(artistList, json);
        requestControl();
        return artistList;
    }

    private Map<String, String> artistParams(String artistsIds) {
        return Map.of("ids", artistsIds);
    }

    private void artistsJsonParser(List<ArtistSchema> artistList, String json) {
        JsonObject artists = gson.fromJson(json, JsonObject.class);
        Type listType = new TypeToken<ArrayList<ArtistSchema>>() {}.getType();
        artistList.addAll(gson.fromJson(artists.getAsJsonArray("artists"), listType));
    }

    public Map<String, List<AlbumSchema>> downloadAlbums(List<ArtistSchema> artistsIdList) throws Exception {
        Map<String, List<AlbumSchema>> albumsMap = new HashMap<>();
        for (ArtistSchema artist : artistsIdList) {
            List<AlbumSchema> albumsList = albumsDownloaderController(artist);
            albumsMap.put(artist.getId(), albumsList);
        }
        return albumsMap;
    }

    private List<AlbumSchema> albumsDownloaderController(ArtistSchema artist) throws Exception {
        int offset = 0, offsetControl = 0;
        List<AlbumSchema> albumsList = new ArrayList<>();
        while (offsetControl % 50 == 0) {
            int changesControl = albumsList.size();
            albumsList.addAll(albumsDownloader(artist, offset));
            offset++;
            requestControl();
            offsetControl = albumsList.size();
            if (changesControl == albumsList.size()) break;
        }
        return albumsList;
    }

    private List<AlbumSchema> albumsDownloader(ArtistSchema artist, int offset) throws Exception {
        String json = accessor.get("/artists/" + artist.getId() + "/albums", albumParams(offset));
        JsonObject albums = gson.fromJson(json, JsonObject.class);
        Type listType = new TypeToken<ArrayList<AlbumSchema>>() {}.getType();
        return gson.fromJson(albums.get("items").getAsJsonArray(), listType);
    }

    private Map<String, String> albumParams(Integer offset) {
        return Map.of("include_groups", "album,single,compilation", "limit", "50", "offset", Integer.toString(offset * 50));
    }

    public Map<String, List<TrackSchema>> downloadTracks(Map<String, List<AlbumSchema>> albumsMap) throws Exception {
        Map<String, List<TrackSchema>> tracksMap = new HashMap<>();
        for (List<AlbumSchema> albums : albumsMap.values()) {
            for (AlbumSchema album : albums) {
                List<TrackSchema> tracksList = tracksDownloaderController(album);
                tracksMap.put(album.getId(), tracksList);
            }
        }
        return tracksMap;
    }

    private List<TrackSchema> tracksDownloaderController(AlbumSchema album) throws Exception {
        int offset = 0, offsetControl = 0;
        List<TrackSchema> tracksList = new ArrayList<>();
        while (offsetControl % 50 == 0) {
            int changesControl = tracksList.size();
            tracksList.addAll(tracksDownloader(album, offset));
            offset++;
            requestControl();
            offsetControl = tracksList.size();
            if (changesControl == tracksList.size()) break;
        }
        return tracksList;
    }

    private List<TrackSchema> tracksDownloader(AlbumSchema album, int offset) throws Exception {
        String json = accessor.get("/albums/" + album.getId() + "/tracks", trackParams(offset));
        JsonObject object = gson.fromJson(json, JsonObject.class);
        Type listType = new TypeToken<ArrayList<TrackSchema>>() {}.getType();
        return gson.fromJson(object.get("items").getAsJsonArray(), listType);
    }

    private Map<String, String> trackParams(Integer offset) {
        return Map.of("limit", "50", "offset", Integer.toString(offset * 50));
    }

    private void requestControl() throws InterruptedException {
        globalRequests++;
        if (globalRequests % 100 == 0) Thread.sleep(3000);
    }
}
