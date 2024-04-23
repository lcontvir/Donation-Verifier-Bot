package com.lcontvir.bot.modelo;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PropsLoader {
    private static final Properties appProps = new Properties();

    /**
     * Este método carga las propiedades de la aplicación desde un archivo de propiedades.
     *
     * <p>Primero, intenta encontrar el archivo de propiedades en el directorio actual del usuario.
     * Si el archivo existe, lo carga en las propiedades de la aplicación {@link #appProps}.
     * Si el archivo no existe, llama al método {@link #copiarConfiguracion(String, String)} para crear un nuevo archivo de propiedades.</p>
     *
     * @see Properties#load(java.io.InputStream)
     * @see System#getProperty(String)
     * @see Files#exists(Path, java.nio.file.LinkOption...)
     * @see #copiarConfiguracion(String, String)
     */
    public static void loadAppProps() {

        String directorioActual = System.getProperty("user.dir");
        String separador = File.separator;
        String nombreArchivoConfiguracion = "donacion_bot.properties";
        String rutaArchivoConfiguracion = directorioActual + separador + nombreArchivoConfiguracion;


        Path archivoConfiguracion = Paths.get(rutaArchivoConfiguracion);
        if (Files.exists(archivoConfiguracion)) {
            try (FileInputStream fis = new FileInputStream(archivoConfiguracion.toFile())) {
                appProps.load(fis);
            } catch (IOException e) {
                LoggerFactory.getLogger("Bot Donaciones - Props Loader").error("Ha ocurrido un error al cargar la configuracion: " + e.getMessage());
            }
        } else {
            copiarConfiguracion(directorioActual, separador);
        }
    }

    /**
     * Este método copia la configuración de un archivo de propiedades de ejemplo a un archivo de propiedades principal.
     *
     * <p>Primero, verifica si el archivo de propiedades de ejemplo existe en el directorio actual del usuario.
     * Si el archivo existe, lo copia al archivo de propiedades principal {@link #donacion_bot.properties}.
     * Si el archivo de ejemplo no existe, imprime un mensaje de error.</p>
     *
     * @param directorioActual El directorio actual del usuario donde se buscará el archivo de propiedades de ejemplo.
     * @param separador El separador de archivos del sistema operativo.
     *
     * @see Files#exists(Path, java.nio.file.LinkOption...)
     * @see Files#copy(Path, Path, java.nio.file.CopyOption...)
     */
    public static void copiarConfiguracion(String directorioActual, String separador) {

        String nombreArchivoConfiguracionEjemplo = "donacion_bot.properties.example";
        String nombreArchivoConfiguracion = "donacion_bot.properties";

        Path archivoConfiguracionEjemplo = Paths.get(directorioActual + separador + nombreArchivoConfiguracionEjemplo);
        Path archivoConfiguracion = Paths.get(directorioActual + separador + nombreArchivoConfiguracion);

        if (Files.exists(archivoConfiguracionEjemplo)) {
            try {
                Files.copy(archivoConfiguracionEjemplo, archivoConfiguracion);

                LoggerFactory.getLogger("Bot Donaciones - Props Loader").warn("Se ha creado una configuracion nueva por que no existia una anteriormente, por favor, rellena la configuracion para usar el programa");
            } catch (IOException e) {
                LoggerFactory.getLogger("Bot Donaciones - Props Loader").error("Error al copiar y renombrar el archivo: " + e.getMessage());
            }
        } else {
            LoggerFactory.getLogger("Bot Donaciones - Props Loader").error("No se ha podido crear el archivo de configuracion por que no existe su ejemplo.");
        }
    }

    public static String getJDAToken() {
        return appProps.getProperty("JDA_TOKEN");
    }

    public static String getSteamAPIToken() {
        return appProps.getProperty("STEAM_TOKEN");
    }

    public static String getBBDDConexion() {
        return appProps.getProperty("BBDDConexion");
    }

    public static String getBBDDUser() {
        return appProps.getProperty("BBDDUser");
    }

    public static String getBBDDPassw() {
        return appProps.getProperty("BBDDPassw");
    }

    public static String getFeedbackChannelId() {
        return appProps.getProperty("FeedbackChannelId");
    }

    public static int getExpireDays() {
        int expireDays = -1;
        try{
            expireDays = parseToInt(appProps.getProperty("ExpireDays"));
        }catch (Exception e){
            LoggerFactory.getLogger("Bot Donaciones - Props Loader").error("No se ha podido cargar el tiempo de expiracion de la donacion " + e.getMessage());
        }
        return expireDays;
    }

    /**
     * Este método obtiene el valor de la propiedad `CoolDownModificacion` de las propiedades de la aplicación.
     *
     * <p>Primero, intenta parsear el valor de la propiedad a un entero utilizando el método {@link #parseToInt(String)}.
     * Si el valor parseado es -1, lo establece a 1. Finalmente, devuelve el valor de `cooldown`.</p>
     *
     * @return El valor de `cooldown` que es el valor entero de la propiedad `CoolDownModificacion` o 1 si el valor original es -1.
     *
     * @see #parseToInt(String)
     * @see Properties#getProperty(String)
     */
    public static int getCoolDownModificacion() {
        int cooldown = -1;
        try{
            cooldown = parseToInt(appProps.getProperty("CoolDownModificacion"));
        }catch (Exception e){
            LoggerFactory.getLogger("Bot Donaciones - Props Loader").error("No se ha podido cargar el cooldown de los comandos: " + e.getMessage());
        }
        return cooldown;
    }

    /**
     * Este método establece el valor de la propiedad `CoolDownModificacion` en las propiedades de la aplicación.
     *
     * <p>Convierte el valor entero dado a una cadena de texto utilizando el método {@link String#valueOf(int)} y luego
     * establece esta cadena de texto como el valor de la propiedad `CoolDownModificacion` en las propiedades de la aplicación {@link #appProps}.</p>
     *
     * @param cooldown El valor entero que se establecerá como el valor de la propiedad `CoolDownModificacion`.
     *
     * @see Properties#setProperty(String, String)
     * @see String#valueOf(int)
     */
    public static void setCooldownModificacion(int cooldown) {
        appProps.setProperty("CoolDownModificacion", String.valueOf(cooldown));
    }

    /**
     * Este método establece el valor de la propiedad `ExpireDays` en las propiedades de la aplicación.
     *
     * <p>Convierte el valor entero dado a una cadena de texto utilizando el método {@link String#valueOf(int)} y luego
     * establece esta cadena de texto como el valor de la propiedad `ExpireDays` en las propiedades de la aplicación {@link #appProps}.</p>
     *
     * @param expireDays El valor entero que se establecerá como el valor de la propiedad `ExpireDays`.
     *
     * @see Properties#setProperty(String, String)
     * @see String#valueOf(int)
     */
    public static void setExpireDays(int expireDays) {
        appProps.setProperty("ExpireDays", String.valueOf(expireDays));
    }

    /**
     * Este método convierte una cadena de texto a un entero.
     *
     * <p>Intenta parsear la cadena de texto dada a un entero utilizando el método {@link Integer#parseInt(String)}.
     * Si la cadena de texto no puede ser convertida a un entero, lanza una excepción {@link NumberFormatException}.</p>
     *
     * @param str La cadena de texto que se intentará convertir a un entero.
     * @return El valor entero de la cadena de texto dada.
     * @throws NumberFormatException Si la cadena de texto no puede ser convertida a un entero.
     *
     * @see Integer#parseInt(String)
     */
    private static int parseToInt(String str) throws NumberFormatException {
        return Integer.parseInt(str);
    }
}
