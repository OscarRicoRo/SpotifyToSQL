package es.ulpgc.spotify.downloader.database;

import es.ulpgc.spotify.downloader.model.Album;
import es.ulpgc.spotify.downloader.model.Artist;
import es.ulpgc.spotify.downloader.model.Track;
import es.ulpgc.spotify.downloader.spotify.schemas.AlbumSchema;
import es.ulpgc.spotify.downloader.spotify.schemas.ArtistSchema;
import es.ulpgc.spotify.downloader.spotify.schemas.TrackSchema;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface Database {

    void connect() throws SQLException;

    void createTables() throws SQLException;

    void storeArtists(List<ArtistSchema> artists) throws Exception;
    void storeAlbums(Map<String, List<AlbumSchema>> albums) throws Exception;
    void storeTracks(Map<String, List<TrackSchema>> tracks) throws Exception;

    void deleteRepeatedRows() throws SQLException;

    List<Artist> selectArtists() throws SQLException;
    Map<Album, String> selectAlbums() throws SQLException;
    Map<Track, String> selectTracks() throws SQLException;
}
