package es.ulpgc.spotify.downloader.view;

import es.ulpgc.spotify.downloader.model.Album;
import es.ulpgc.spotify.downloader.model.Artist;
import es.ulpgc.spotify.downloader.model.Track;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class View {
    private final Scanner reader = new Scanner(System.in);

    public View() {
    }

    public String read() {
        return reader.nextLine();
    }

    public void display(String output) {
        System.out.println(output);
    }

    public void showArtists(List<Artist> artistsToShow) {
        System.out.format("|%-22s|%-15s|%-10s|%-9s|\n", "ID", "NAME", "POPULARITY", "FOLLOWERS");
        for (Artist artist : artistsToShow) {
            System.out.format("|%-22s|%-15.15s|     %-5d|%-9d|\n",
                    artist.getId(),
                    artist.getName(),
                    artist.getPopularity(),
                    artist.getFollowers()
            );
        }
    }

    public void showAlbums(Map<Album, String> albumsToShow) {
        System.out.format("|%-22s|%-20s|%-10s|%-15s|%-10s|%-12s|%-22s|\n", "ID", "NAME", "ALBUM TYPE", "ARTISTS", "RELEASE DATE", "TOTAL TRACKS", "ARTIST ID");
        for (Map.Entry<Album, String> album : albumsToShow.entrySet()) {
            System.out.format("|%-22s|%-20.20s|%-10s|%-15.15s| %-11s|      %-6d|%-22s|\n",
                    album.getKey().getId(),
                    album.getKey().getName(),
                    album.getKey().getAlbum_type(),
                    album.getKey().getArtists(),
                    album.getKey().getRelease_date(),
                    album.getKey().getTotal_tracks(),
                    album.getValue()
            );
        }
    }

    public void showTracks(Map<Track, String> tracksToShow) {
        System.out.format("|%-22s|%-20s|%-11s|%-8s|%-15s|%-22s|\n", "ID", "NAME", "DURATION", "EXPLICIT", "ARTISTS", "ALBUM ID");
        for (Map.Entry<Track, String> track : tracksToShow.entrySet()) {
            System.out.format("|%-22s|%-20.20s|     %-6d| %-7b|%-15.15s|%-22s|\n",
                    track.getKey().getId(),
                    track.getKey().getName(),
                    track.getKey().getDuration_ms(),
                    track.getKey().getExplicit(),
                    track.getKey().getArtists(),
                    track.getValue()
            );
        }
    }
}
