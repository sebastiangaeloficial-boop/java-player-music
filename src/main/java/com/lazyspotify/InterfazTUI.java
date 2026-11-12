package com.lazyspotify;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class InterfazTUI {
    private BibliotecaMusical biblioteca;
    private ReproductorAudio reproductor;
    private Terminal terminal;
    private Screen screen;
    private int indiceSeleccionado = 0;
    private String ultimoMensaje = "";

    public InterfazTUI(BibliotecaMusical biblioteca, ReproductorAudio reproductor) {
        this.biblioteca = biblioteca;
        this.reproductor = reproductor;
    }

    public void iniciar() {
        try {
            terminal = new DefaultTerminalFactory().createTerminal();
            screen = new TerminalScreen(terminal);
            screen.startScreen();

            boolean running = true;
            while (running) {
                dibujarPantalla();
                KeyStroke key = screen.readInput();

                switch (key.getKeyType()) {
                    case ArrowUp:
                        if (indiceSeleccionado > 0) indiceSeleccionado--;
                        break;
                    case ArrowDown:
                        if (indiceSeleccionado < biblioteca.tamaño() - 1) indiceSeleccionado++;
                        break;
                    case Enter:
                        reproducirSeleccionada();
                        break;
                    case Escape:
                        running = false;
                        break;
                    case Character:
                        char c = key.getCharacter();
                        if (c == 'q' || c == 'Q') {
                            running = false;
                        } else if (c == 'a' || c == 'A') {
                            agregarCancion();
                        } else if (c == 'c' || c == 'C') {
                            cargarCarpeta();
                        } else if (c == 'd' || c == 'D') {
                            reproductor.detener();
                            ultimoMensaje = "Reproducción detenida";
                        }
                        break;
                    default:
                        break;
                }
            }
            screen.stopScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reproducirSeleccionada() {
        Cancion c = biblioteca.obtenerPorIndice(indiceSeleccionado);
        if (c != null) {
            reproductor.reproducir(c.getRutaArchivo());
            ultimoMensaje = "▶ " + c.getTitulo();
        }
    }

    private void agregarCancion() throws IOException {
        String titulo = leerTexto("Título");
        if (titulo == null || titulo.isBlank()) return;

        String artista = leerTexto("Artista");
        if (artista == null) return;

        String ruta = leerTexto("Ruta del archivo (MP3 o FLAC)");
        if (ruta == null || ruta.isBlank()) return;

        if (ruta.startsWith("~/")) {
            ruta = System.getProperty("user.home") + ruta.substring(1);
        }

        File f = new File(ruta);
        if (!f.exists() || !f.isFile()) {
            mostrarMensaje("✗ El archivo no existe: " + ruta);
            return;
        }

        biblioteca.agregarCancion(new Cancion(titulo, artista, ruta));
        indiceSeleccionado = biblioteca.tamaño() - 1;
        ultimoMensaje = "Agregada: " + titulo;
    }

    private void cargarCarpeta() throws IOException {
        String ruta = leerTexto("Ruta de la carpeta");
        if (ruta == null || ruta.isBlank()) return;

        if (ruta.startsWith("~/")) {
            ruta = System.getProperty("user.home") + ruta.substring(1);
        }

        try {
            List<Cancion> nuevas = CargadorCarpetas.cargar(ruta);
            if (nuevas.isEmpty()) {
                // ← ÚNICO CAMBIO: el mensaje ahora menciona .flac también
                mostrarMensaje("No se encontraron .mp3 ni .flac en: " + ruta);
                return;
            }
            for (Cancion c : nuevas) {
                biblioteca.agregarCancion(c);
            }
            indiceSeleccionado = biblioteca.tamaño() - nuevas.size();
            ultimoMensaje = "Cargadas " + nuevas.size() + " canciones de " + ruta;
        } catch (IOException e) {
            mostrarMensaje("Error: " + e.getMessage());
        }
    }

    private String leerTexto(String etiqueta) throws IOException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            TerminalSize size = screen.getTerminalSize();
            TextGraphics tg = screen.newTextGraphics();
            int fila = size.getRows() - 3;

            tg.setForegroundColor(TextColor.ANSI.BLACK);
            tg.setBackgroundColor(TextColor.ANSI.YELLOW);

            String prompt = String.format(" %s: %s_  (Enter=OK · Esc=cancelar) ", etiqueta, sb);
            tg.putString(0, fila, String.format("%-" + size.getColumns() + "s", prompt));
            screen.refresh();

            KeyStroke key = screen.readInput();
            if (key.getKeyType() == KeyType.Enter) return sb.toString();
            if (key.getKeyType() == KeyType.Escape) return null;
            if (key.getKeyType() == KeyType.Backspace && sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            } else if (key.getKeyType() == KeyType.Character) {
                sb.append(key.getCharacter());
            }
        }
    }

    private void mostrarMensaje(String mensaje) throws IOException {
        TerminalSize size = screen.getTerminalSize();
        TextGraphics tg = screen.newTextGraphics();
        int fila = size.getRows() - 3;

        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.setBackgroundColor(TextColor.ANSI.RED);
        tg.putString(0, fila,
                String.format("%-" + size.getColumns() + "s",
                        " " + mensaje + " (pulsa una tecla) "));
        screen.refresh();
        screen.readInput();
    }

    private void dibujarPantalla() throws IOException {
        screen.clear();
        TerminalSize size = screen.getTerminalSize();
        TextGraphics tg = screen.newTextGraphics();

        tg.setForegroundColor(TextColor.ANSI.CYAN);
        tg.setBackgroundColor(TextColor.ANSI.BLACK);
        tg.putString(2, 1, " LAZYSPOTIFY LOCAL ", SGR.BOLD);
        tg.drawLine(0, 2, size.getColumns() - 1, 2, '─');

        List<Cancion> canciones = biblioteca.obtenerTodas();
        int fila = 4;
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(2, fila, "Biblioteca Musical (" + canciones.size() + "):");
        fila++;

        for (int i = 0; i < canciones.size(); i++) {
            if (i == indiceSeleccionado) {
                tg.setForegroundColor(TextColor.ANSI.BLACK);
                tg.setBackgroundColor(TextColor.ANSI.CYAN);
            } else {
                tg.setForegroundColor(TextColor.ANSI.WHITE);
                tg.setBackgroundColor(TextColor.ANSI.BLACK);
            }
            String linea = String.format(" ▶ %d. %s", i + 1, canciones.get(i));
            tg.putString(1, fila,
                    String.format("%-" + (size.getColumns() - 2) + "s", linea));
            fila++;
        }

        if (canciones.isEmpty()) {
            tg.setForegroundColor(TextColor.ANSI.WHITE);
            tg.setBackgroundColor(TextColor.ANSI.BLACK);
            tg.putString(3, fila, "(vacía) Presiona [C] para cargar una carpeta o [A] para una canción");
        }

        if (!ultimoMensaje.isEmpty()) {
            tg.setForegroundColor(TextColor.ANSI.GREEN);
            tg.setBackgroundColor(TextColor.ANSI.BLACK);
            tg.putString(0, size.getRows() - 2,
                    String.format("%-" + size.getColumns() + "s", " " + ultimoMensaje));
        }

        tg.setForegroundColor(TextColor.ANSI.BLACK);
        tg.setBackgroundColor(TextColor.ANSI.WHITE);
        String ayuda = " [Q] Salir  [↑/↓] Navegar  [Enter] Reproducir  [C] Cargar carpeta  [A] Agregar  [D] Detener ";
        tg.putString(0, size.getRows() - 1,
                String.format("%-" + size.getColumns() + "s", ayuda));

        screen.refresh();
    }
}
