# 🎵 LazySpotify Local

Un reproductor de música local con interfaz de terminal (TUI), inspirado en [lazyspotify](https://github.com/dubeyKartikay/lazyspotify), pero **sin conexión a Spotify**: trabaja exclusivamente con archivos de audio locales (MP3 y FLAC).

![Java](https://img.shields.io/badge/Java-17+-orange)
![Maven](https://img.shields.io/badge/Maven-3.8+-blue)
![License](https://img.shields.io/badge/license-MIT-green)

---

## ✨ Características

- 🖥️ Interfaz de terminal a pantalla completa construida con [Lanterna](https://github.com/mabe02/lanterna)
- 🎵 Reproduce **MP3** y **FLAC** (vía [VLCJ](https://github.com/caprica/vlcj) + `libvlc`)
- 📂 Carga automática de carpetas completas (recursiva, infiere artista desde la estructura de carpetas)
- ⌨️ Navegación con flechas, sin ratón
- ➕ Agregar canciones individuales con validación de ruta
- 🚫 Cero dependencias de servicios externos: todo local

---

## 🎮 Controles

| Tecla | Acción |
|-------|--------|
| `↑` / `↓` | Navegar por la lista de canciones |
| `Enter` | Reproducir la canción seleccionada |
| `C` | Cargar una carpeta completa (MP3 y FLAC) |
| `A` | Agregar una canción individual (título → artista → ruta) |
| `D` | Detener la reproducción |
| `Q` o `Esc` | Salir |

---

## 🖥️ Instalación en Linux (Arch)

### 1. Instalar dependencias

```bash
sudo pacman -S jdk-openjdk maven vlc
```

Verifica que todo esté correcto:

```bash
java -version     # debe mostrar OpenJDK 17+
mvn -version
ls /usr/lib/libvlc.so*
```

### 2. Clonar y ejecutar

```bash
git clone https://github.com/TU_USUARIO/lazyspotify-local.git
cd lazyspotify-local
mvn clean compile
mvn exec:java
```

---

## 🪟 Instalación en Windows

### 1. Instalar dependencias

#### Java 17 (JDK)

Descarga el instalador de **Eclipse Temurin JDK 17** desde:  
👉 https://adoptium.net/

Durante la instalación, marca la opción **"Set JAVA_HOME variable"** y **"Add to PATH"**.

Verifica en una nueva terminal (PowerShell o CMD):

```powershell
java -version
```

Debe mostrar `openjdk version "17..."`.

#### Maven

1. Descarga el **Binary zip** de Maven desde:  
   👉 https://maven.apache.org/download.cgi
2. Descomprime en `C:\Program Files\Apache\maven` (por ejemplo).
3. Agrega `C:\Program Files\Apache\maven\bin` al **PATH**:
   - `Win + R` → escribe `sysdm.cpl` → pestaña **Opciones avanzadas** → **Variables de entorno**
   - En **Variables del sistema**, edita `Path` → **Nuevo** → pega la ruta del `bin`
4. Abre una terminal nueva y verifica:

```powershell
mvn -version
```

#### VLC

Descarga e instala VLC desde:  
👉 https://www.videolan.org/vlc/download-windows.html

> ⚠️ **Importante**: instala VLC en la ruta por defecto (`C:\Program Files\VideoLAN\VLC`). Si lo instalas en otra ruta, VLCJ podría no encontrar `libvlc.dll`. Si eso pasa, agrega la carpeta de VLC al `PATH` de Windows.

### 2. Clonar y ejecutar

En **PowerShell** (o en **Git Bash**, pero **no** en CMD por los símbolos de flechas):

```powershell
git clone https://github.com/TU_USUARIO/lazyspotify-local.git
cd lazyspotify-local
mvn clean compile
mvn exec:java
```

> 💡 En Windows, la TUI funciona mejor en **Windows Terminal** o en **PowerShell**, no en `cmd.exe` (que no soporta bien caracteres Unicode como `▶` o `─`).

---

## 📁 Estructura del proyecto

```
lazyspotify-local/
├── pom.xml
├── README.md
├── .gitignore
└── src/
    └── main/
        └── java/
            └── com/
                └── lazyspotify/
                    ├── Main.java
                    ├── Cancion.java
                    ├── BibliotecaMusical.java
                    ├── CargadorCarpetas.java
                    ├── ReproductorAudio.java
                    └── InterfazTUI.java
```

---

## 🎵 Cómo cargar tu música

### Opción A — Cargar una carpeta entera (recomendado)

Organiza tus archivos así:

```
Michael Jackson/
├── Off the Wall/
│   ├── Don't Stop 'Til You Get Enough.flac
│   └── Rock with You.flac
└── Thriller/
    ├── Billie Jean.flac
    └── Beat It.flac
```

Dentro del programa:

1. Presiona `C`
2. Escribe la ruta de la carpeta (ej. `Michael Jackson`)
3. Enter

El programa detectará todos los `.mp3` y `.flac` recursivamente y usará el nombre de la primera carpeta como **artista**.

### Opción B — Agregar canción por canción

Presiona `A` y sigue las instrucciones: título, artista, ruta del archivo.

---

## ⚙️ Requisitos

| Componente | Versión mínima |
|------------|----------------|
| Java (JDK) | 17 |
| Maven | 3.8 |
| VLC | 3.x |
| Sistema operativo | Linux, Windows 10+, macOS* |

\* En macOS no está probado, pero VLCJ es multiplataforma. Requiere instalar VLC desde `brew install --cask vlc`.

---

## 🐛 Problemas conocidos

### "No suitable audio format found" al reproducir

Asegúrate de que VLC esté instalado y que `libvlc` sea accesible desde el `PATH`.  
- **Linux**: `ls /usr/lib/libvlc.so*` debe devolver algo.
- **Windows**: verifica que `C:\Program Files\VideoLAN\VLC\libvlc.dll` exista.

### La TUI se ve rota en Windows

Usa **Windows Terminal** o **PowerShell**. `cmd.exe` no soporta caracteres Unicode extendidos ni colores ANSI modernos de forma fiable.

### `Unresolved compilation problem` al ejecutar

Siempre corre primero `mvn clean compile`. Si compila, entonces `mvn exec:java`.

---

## 📜 Licencia

MIT. Este proyecto es un ejercicio educativo y **no tiene ninguna relación con Spotify**. "LazySpotify" es solo un guiño al proyecto original de [@dubeyKartikay](https://github.com/dubeyKartikay).

---

## 🙏 Créditos

- Inspirado en [lazyspotify](https://github.com/dubeyKartikay/lazyspotify)
- TUI con [Lanterna](https://github.com/mabe02/lanterna)
- Reproducción con [VLCJ](https://github.com/caprica/vlcj)
