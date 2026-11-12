package com.lazyspotify;

public class Cancion {
    private String titulo;
    private String artista;
    private String rutaArchivo; // Ruta local del archivo de audio

    public Cancion(String titulo, String artista, String rutaArchivo) {
        this.titulo = titulo;
        this.artista = artista;
        this.rutaArchivo = rutaArchivo;
    }

    // Getters y setters
    public String getTitulo() { return titulo; }
    public String getArtista() { return artista; }
    public String getRutaArchivo() { return rutaArchivo; }

    @Override
    public String toString() {
        return titulo + " - " + artista;
    }
}
