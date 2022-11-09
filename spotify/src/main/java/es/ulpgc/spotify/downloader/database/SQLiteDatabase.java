package es.ulpgc.spotify.downloader.database;

import es.ulpgc.spotify.downloader.model.Album;
import es.ulpgc.spotify.downloader.model.Artist;
import es.ulpgc.spotify.downloader.model.Track;
import es.ulpgc.spotify.downloader.spotify.schemas.AlbumSchema;
import es.ulpgc.spotify.downloader.spotify.schemas.ArtistSchema;
import es.ulpgc.spotify.downloader.spotify.schemas.TrackSchema;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLiteDatabase implements Database {
    private Connection connection;

    public SQLiteDatabase() throws SQLException {
        connect();
        createTables();
    }

    @Override
    public void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:Spotify.db");
    }

    @Override
    public void createTables() throws SQLException {
        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS Artists(" +
                "id TEXT PRIMARY KEY," +
                "name TEXT," +
                "popularity INTEGER," +
                "followers INTEGER" +
                ");");

        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS Albums(" +
                "id TEXT PRIMARY KEY," +
                "name TEXT," +
                "album_type TEXT," +
                "artists TEXT," +
                "release_date TEXT," +
                "total_tracks INTEGER," +
                "artist_id TEXT," +
                "FOREIGN KEY (artist_id) REFERENCES Artists(id)" +
                ");");

        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS Tracks("
                + "id TEXT PRIMARY KEY," +
                "name TEXT," +
                "duration INTEGER," +
                "explicit BOOLEAN," +
                "artists TEXT," +
                "album_id TEXT," +
                "FOREIGN KEY (album_id) REFERENCES Albums(id)" +
                ");");
    }

    @Override
    public void storeArtists(List<ArtistSchema> artists) throws Exception {
        connection.setAutoCommit(false);
        String sql = "INSERT or REPLACE INTO Artists(id, name, popularity, followers) VALUES(?,?,?,?)";
        PreparedStatement psi = connection.prepareStatement(sql);
        prepareArtists(artists, psi);
        connection.commit();
        connection.setAutoCommit(true);
    }

    private void prepareArtists(List<ArtistSchema> artists, PreparedStatement psi) throws SQLException {
        for (ArtistSchema artist : artists) {
            psi.setString(1, artist.getId());
            psi.setString(2, artist.getName());
            psi.setInt(3, artist.getPopularity());
            psi.setInt(4, artist.getFollowers());
            psi.executeUpdate();
        }
    }

    @Override
    public void storeAlbums(Map<String, List<AlbumSchema>> albumsMap) throws Exception {
        connection.setAutoCommit(false);
        String sql = "INSERT or REPLACE INTO Albums(id, name, album_type, artists, release_date, total_tracks, artist_id) VALUES(?,?,?,?,?,?,?)";
        PreparedStatement psi = connection.prepareStatement(sql);
        for (Map.Entry<String, List<AlbumSchema>> albums : albumsMap.entrySet()) {
            prepareAlbums(psi, albums);
        }
        connection.commit();
        connection.setAutoCommit(true);
    }

    private void prepareAlbums(PreparedStatement psi, Map.Entry<String, List<AlbumSchema>> albums) throws SQLException {
        for (AlbumSchema album : albums.getValue()) {
            psi.setString(1, album.getId());
            psi.setString(2, album.getName());
            psi.setString(3, album.getAlbum_type());
            psi.setString(4, getArtists(album.getArtists()));
            psi.setString(5, album.getRelease_date());
            psi.setInt(6, album.getTotal_tracks());
            psi.setString(7, albums.getKey());
            psi.executeUpdate();
        }
    }

    @Override
    public void storeTracks(Map<String, List<TrackSchema>> tracksMap) throws Exception {
        connection.setAutoCommit(false);
        String sql = "INSERT or REPLACE INTO Tracks(id, name, duration, explicit, artists,album_id) VALUES(?,?,?,?,?,?)";
        PreparedStatement psi = connection.prepareStatement(sql);
        for (Map.Entry<String, List<TrackSchema>> tracks : tracksMap.entrySet()) {
            prepareTracks(psi, tracks);
        }
        connection.commit();
        connection.setAutoCommit(true);
    }

    private void prepareTracks(PreparedStatement psi, Map.Entry<String, List<TrackSchema>> tracks) throws SQLException {
        for (TrackSchema track : tracks.getValue()) {
            psi.setString(1, track.getId());
            psi.setString(2, track.getName());
            psi.setInt(3, track.getDuration_ms() / 1000);
            psi.setBoolean(4, track.getExplicit());
            psi.setString(5, getArtists(track.getArtists()));
            psi.setString(6, tracks.getKey());
            psi.executeUpdate();
        }
    }

    private String getArtists(List<ArtistSchema> artistList) {
        StringBuilder artists = new StringBuilder();
        for (ArtistSchema artist : artistList) {
            artists.append(artist.getName()).append(",");
        }
        artists = new StringBuilder(artists.substring(0, artists.length() - 1));
        return artists.toString();
    }

    @Override
    public void deleteRepeatedRows() throws SQLException {
        connection.createStatement().execute("DELETE FROM Albums WHERE id NOT IN (SELECT MIN(id) FROM Albums GROUP BY name);");
        connection.createStatement().execute("DELETE FROM Tracks WHERE id NOT IN (SELECT MIN(id) FROM Tracks GROUP BY name);");
    }

    @Override
    public List<Artist> selectArtists() throws SQLException {
        List<Artist> artistsList = new ArrayList<>();
        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Artists LIMIT 50;");
        while (rs.next()) {
            String id = rs.getString("id");
            String name = rs.getString("name");
            int popularity = rs.getInt("popularity");
            int followers = rs.getInt("followers");
            artistsList.add(new Artist(id, name, popularity, followers));
        }
        rs.close();
        return artistsList;
    }

    @Override
    public Map<Album, String> selectAlbums() throws SQLException {
        Map<Album, String> albumsMap = new HashMap<>();
        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Albums LIMIT 100;");
        while (rs.next()) {
            String id = rs.getString("id");
            String name = rs.getString("name");
            String album_type = rs.getString("album_type");
            String artists = rs.getString("artists");
            String release_date = rs.getString("release_date");
            Integer total_tracks = rs.getInt("total_tracks");
            String artist_id = rs.getString("artist_id");
            albumsMap.put(new Album(album_type, id, name, release_date, total_tracks, artists), artist_id);
        }
        rs.close();
        return albumsMap;
    }

    @Override
    public Map<Track, String> selectTracks() throws SQLException {
        Map<Track, String> tracksMap = new HashMap<>();
        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Tracks LIMIT 150;");
        while (rs.next()) {
            String id = rs.getString("id");
            String name = rs.getString("name");
            Integer duration = rs.getInt("duration");
            Boolean explicit = rs.getBoolean("explicit");
            String artists = rs.getString("artists");
            String album_id = rs.getString("album_id");
            tracksMap.put(new Track(artists, duration, explicit, id, name), album_id);
        }
        rs.close();
        return tracksMap;
    }
}
