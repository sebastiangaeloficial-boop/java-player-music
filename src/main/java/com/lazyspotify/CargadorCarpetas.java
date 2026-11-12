package com.lazyspotify;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CargadorCarpetas {

    public static List<Cancion> cargar(String rutaCarpeta) throws IOException {
        List<Cancion> resultado = new ArrayList<>();
        Path raiz = Paths.get(rutaCarpeta).toAbsolutePath().normalize();

        if (!Files.isDirectory(raiz)) {
            throw new IOException("No es un directorio: " + raiz);
        }

        String nombreRaiz = raiz.getFileName() != null
                ? raiz.getFileName().toString()
                : "Desconocido";

        try (Stream<Path> stream = Files.walk(raiz)) {
            stream.filter(Files::isRegularFile)
                  .filter(p -> {
                      String n = p.getFileName().toString().toLowerCase();
                      return n.endsWith(".mp3") || n.endsWith(".flac");
                  })
                  .forEach(p -> {
                      String nombreArchivo = p.getFileName().toString();
                      String titulo = nombreArchivo.substring(0, nombreArchivo.lastIndexOf('.'));

                      Path relativo = raiz.relativize(p);
                      String artista;
                      if (relativo.getNameCount() >= 3) {
                          artista = relativo.getName(0).toString();
                      } else {
                          artista = nombreRaiz;
                      }

                      resultado.add(new Cancion(titulo, artista, p.toString()));
                  });
        }
        return resultado;
    }
}
