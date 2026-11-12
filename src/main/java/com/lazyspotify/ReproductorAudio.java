package com.lazyspotify;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

public class ReproductorAudio {
    private final MediaPlayerFactory factory;
    private final MediaPlayer mediaPlayer;

    public ReproductorAudio() {
        this.factory = new MediaPlayerFactory();
        this.mediaPlayer = factory.mediaPlayers().newMediaPlayer();
    }

    public void reproducir(String rutaArchivo) {
        mediaPlayer.controls().stop();
        mediaPlayer.media().play(rutaArchivo);
    }

    public void detener() {
        mediaPlayer.controls().stop();
    }
}
