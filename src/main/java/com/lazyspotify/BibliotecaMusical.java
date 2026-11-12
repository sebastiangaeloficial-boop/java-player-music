package com.lazyspotify;

import java.util.ArrayList;

import java.util.List;

public class BibliotecaMusical {
    private ArrayList<Cancion> canciones;

    public BibliotecaMusical() {
        this.canciones = new ArrayList<>();
    }

    public void agregarCancion(Cancion cancion) {
        canciones.add(cancion);
    }

    public List<Cancion> obtenerTodas() {
        return canciones;
    }

    public Cancion obtenerPorIndice(int indice) {
        if (indice >= 0 && indice < canciones.size()) {
            return canciones.get(indice);
        }
        return null;
    }

    public int tamaño() {
        return canciones.size();
    }

    // Puedes agregar más métodos: buscar por título, filtrar por artista, etc.
}
