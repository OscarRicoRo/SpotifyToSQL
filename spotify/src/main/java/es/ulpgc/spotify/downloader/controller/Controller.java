package es.ulpgc.spotify.downloader.controller;

import es.ulpgc.spotify.downloader.database.SQLiteDatabase;
import es.ulpgc.spotify.downloader.model.Album;
import es.ulpgc.spotify.downloader.model.Artist;
import es.ulpgc.spotify.downloader.model.Track;
import es.ulpgc.spotify.downloader.spotify.Spotify;
import es.ulpgc.spotify.downloader.spotify.schemas.AlbumSchema;
import es.ulpgc.spotify.downloader.spotify.schemas.ArtistSchema;
import es.ulpgc.spotify.downloader.spotify.schemas.TrackSchema;
import es.ulpgc.spotify.downloader.view.View;

import java.util.List;
import java.util.Map;

public class Controller {
    private final View view = new View();
    private final SQLiteDatabase database = new SQLiteDatabase();
    private final Spotify downloader = new Spotify();

    public Controller() throws Exception {
    }

    public void control() throws Exception {
        view.display("Insert an artist id or several artists ids separated by commas: ");
        String artistsIds = view.read();
        List<ArtistSchema> artistsList = downloader.downloadArtists(artistsIds);
        Map<String, List<AlbumSchema>> albumsMap = downloader.downloadAlbums(artistsList);
        Map<String, List<TrackSchema>> tracksMap = downloader.downloadTracks(albumsMap);
        database.storeArtists(artistsList);
        database.storeAlbums(albumsMap);
        database.storeTracks(tracksMap);
        database.deleteRepeatedRows();
        List<Artist> artistsToShow = database.selectArtists();
        Map<Album, String> albumsToShow = database.selectAlbums();
        Map<Track, String> tracksToShow = database.selectTracks();
        view.showArtists(artistsToShow);
        view.showAlbums(albumsToShow);
        view.showTracks(tracksToShow);
    }
}
