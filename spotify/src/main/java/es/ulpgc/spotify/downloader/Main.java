package es.ulpgc.spotify.downloader;

import es.ulpgc.spotify.downloader.controller.Controller;

public class Main {
    public static void main(String[] args) throws Exception {
        new Controller().control();
    }
}
