package es.ulpgc.spotify.downloader.spotify.schemas;

public class ArtistSchema {
    private final String id;
    private final String name;
    private final Integer popularity;
    Follower followers;

    public ArtistSchema(String id, String name, Integer popularity, Follower followers) {
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
        return followers.getTotal();
    }

    private class Follower {
        private final Integer total;

        public Follower(Integer total) {
            this.total = total;
        }

        public Integer getTotal() {
            return total;
        }
    }
}
