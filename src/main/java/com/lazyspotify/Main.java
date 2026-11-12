package com.lazyspotify;

public class Main {
    public static void main(String[] args) {
        BibliotecaMusical biblioteca = new BibliotecaMusical();
        ReproductorAudio reproductor = new ReproductorAudio();

        InterfazTUI tui = new InterfazTUI(biblioteca, reproductor);
        tui.iniciar();
    }
}
