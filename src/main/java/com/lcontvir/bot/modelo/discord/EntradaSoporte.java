package com.lcontvir.bot.modelo.discord;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EntradaSoporte {

    private String id;
    private String titulo;
    private String descripcion;
    private String emoji;
    private String pathToMessage;
    private String node;
    private String nextNode;

    public EntradaSoporte() {
    }

    public EntradaSoporte(String id, String titulo, String descripcion, String emoji, String pathToMessage, String node, String nextNode) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.emoji = emoji;
        this.pathToMessage = pathToMessage;
        this.node = node;
        this.nextNode = nextNode;
    }

    /**
     * Este método se utiliza para cargar todas las entradas de soporte desde un archivo JSON.
     * Cada objeto EntradaSoporte representa una entrada de soporte en el sistema.
     *
     * @return Una lista de objetos EntradaSoporte que se han cargado desde el archivo JSON.
     * Si ocurre un error durante la lectura del archivo o el procesamiento del JSON, se imprime la traza de la excepción y se devuelve una lista vacía.
     */
    public static List<EntradaSoporte> CargarEntradasSoporte() {

        String directorioActual = System.getProperty("user.dir");
        String separador = File.separator;
        String nombreArchivoConfiguracion = "supportquestions.json";
        String rutaArchivoPreguntas = directorioActual + separador + nombreArchivoConfiguracion;

        List<EntradaSoporte> EntradasSoporte = new ArrayList<>();

        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(rutaArchivoPreguntas)));
            JSONObject jsonObject = new JSONObject(jsonContent);

            JSONObject menuSeleccion = jsonObject.getJSONObject("Menu_Seleccion");

            for (String key : menuSeleccion.keySet()) {
                JSONObject menuItemJson = menuSeleccion.getJSONObject(key);

                EntradaSoporte menuItem = new EntradaSoporte();
                menuItem.setId(key);
                menuItem.setTitulo(menuItemJson.getString("Titulo"));
                menuItem.setDescripcion(menuItemJson.getString("Descripcion"));
                menuItem.setEmoji(menuItemJson.getString("Emoji"));
                menuItem.setNode(menuItemJson.getString("Support_block"));

                if (menuItemJson.has("Path_to_message")) {
                    menuItem.setPathToMessage(menuItemJson.getString("Path_to_message"));
                }
                if (menuItemJson.has("Id_to_next_supportblock")) {
                    menuItem.setNextNode(menuItemJson.getString("Id_to_next_supportblock"));
                }

                EntradasSoporte.add(menuItem);
            }

        } catch (IOException | JSONException e) {
            LoggerFactory.getLogger("M.I.M.I - Entrada Soporte").error("No se han podido cargar las entradas de soporte" + e.getMessage());

        }
        return EntradasSoporte;
    }

    /**
     * Este método se utiliza para obtener un objeto EntradaSoporte específico basado en el ID de la fila proporcionado.
     * Cada objeto EntradaSoporte representa una entrada de soporte en el sistema.
     *
     * @param rowID El ID de la fila para el cual se debe obtener el objeto EntradaSoporte.
     * @return Un objeto EntradaSoporte que corresponde al ID de la fila proporcionado.
     * Si no se encuentra ninguna entrada de soporte que corresponda al ID de la fila, se devuelve un nuevo objeto EntradaSoporte vacío.
     */
    public static EntradaSoporte getEntradaSoporteByRowID(String rowID) {
        try {
            List<EntradaSoporte> entradaSoporteList = CargarEntradasSoporte();
            for (EntradaSoporte EntradaSoporte : entradaSoporteList) {
                if (EntradaSoporte.getTitulo().toLowerCase().trim().equals(rowID)) {
                    System.out.println(EntradaSoporte.getPathToMessage());
                    return EntradaSoporte;
                }
            }
        } catch (Exception ex) {
            LoggerFactory.getLogger("M.I.M.I - Entrada Soporte").error("Ha ocurrido un error al obtener la entrada de soporte por ID de fila: " + ex.getMessage());
        }
        return new EntradaSoporte();
    }

    /**
     * Este método se utiliza para leer un archivo específico de soporte y devolver su contenido como una cadena de texto.
     *
     * @param fileName El nombre del archivo de soporte que se va a leer.
     * @return Una cadena de texto que representa el contenido del archivo de soporte.
     * Si ocurre un error durante la lectura del archivo, se imprime el mensaje de error y se devuelve una cadena vacía.
     */
    public static String readSupportSpecificFile(String fileName) {
        String directorioActual = System.getProperty("user.dir");
        String separador = File.separator;
        String nombreArchivoConfiguracion = fileName;
        String rutaArchivoPreguntas = directorioActual + separador + nombreArchivoConfiguracion;

        try {
            BufferedReader br = new BufferedReader(new FileReader(rutaArchivoPreguntas));
            StringBuilder contenido = new StringBuilder();
            String linea;

            while ((linea = br.readLine()) != null) {
                contenido.append(linea).append("\n");
            }

            br.close();

            String contenidoString = contenido.toString();
            return contenidoString;
        } catch (IOException e) {
            LoggerFactory.getLogger("M.I.M.I - Entrada Soporte").error("Ha ocurrido un error al leer el archivo: " + e.getMessage());

        }
        return "";
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getPathToMessage() {
        return pathToMessage;
    }

    public void setPathToMessage(String pathToMessage) {
        this.pathToMessage = pathToMessage;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getNextNode() {
        return nextNode;
    }

    public void setNextNode(String nextNode) {
        this.nextNode = nextNode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
