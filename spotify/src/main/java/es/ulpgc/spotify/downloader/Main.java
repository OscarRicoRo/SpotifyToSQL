package es.ulpgc.spotify.downloader;

import es.ulpgc.spotify.downloader.controller.Controller;

public class Main {
    public static void main(String[] args) throws Exception {
        long Start = System.currentTimeMillis();
        new Controller().control();
        long End = System.currentTimeMillis();
        System.out.println(End-Start);
    }
}
