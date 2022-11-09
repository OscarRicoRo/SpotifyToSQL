package es.ulpgc.spotify.downloader.model;

public class Artist {
    private final String id;
    private final String name;
    private final Integer popularity;
    private final Integer followers;

    public Artist(String id, String name, Integer popularity, Integer followers) {
        this.id = id;
        this.name = name;
        this.popularity = popularity;
        this.followers = followers;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public Integer getFollowers() {
        return followers;
    }

}
