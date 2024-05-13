package com.lcontvir.bot.modelo.json;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class JsonManager {

    /**
     * Verifica y procesa la configuración JSON.
     *
     * @return true si la configuración JSON es válida, false si hay algún error.
     */
    public static boolean VerifyJsonConfig() {
        String directorioActual = System.getProperty("user.dir");
        String separador = File.separator;
        String nombreArchivoConfiguracion = "supportquestions.json";
        String rutaArchivoPreguntas = directorioActual + separador + nombreArchivoConfiguracion;

        File jsonFile = new File(rutaArchivoPreguntas);
        if (!jsonFile.exists()) {
            try {
                String exampleJsonContent = new String(Files.readAllBytes(Paths.get("./supportquestions.json.example")));

                Files.write(Paths.get(rutaArchivoPreguntas), exampleJsonContent.getBytes());

                System.out.println("Se ha creado un archivo JSON utilizando el ejemplo en: " + rutaArchivoPreguntas);
            } catch (IOException e) {
                LoggerFactory.getLogger("M.I.M.I - Lectura Preguntas Json Config").error("No se ha podido crear el archivo JSON: " + e.getMessage());
                return false;
            }
        }

        if (jsonFile.length() == 0) {
            LoggerFactory.getLogger("M.I.M.I - Lectura Preguntas Json Config").error("El archivo JSON está vacío.");
            return false;
        }

        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(rutaArchivoPreguntas)));

            try {
                new JSONObject(jsonContent);
            } catch (JSONException e) {
                LoggerFactory.getLogger("M.I.M.I - Lectura Preguntas Json Config").error("El archivo no tiene un formato JSON válido: " + e.getMessage());
                return false;
            }

            JSONObject jsonObject = new JSONObject(jsonContent);

            if (!jsonObject.has("Menu_Seleccion")) {
                LoggerFactory.getLogger("M.I.M.I - Lectura Preguntas Json Config").error("El archivo no tiene un formato JSON válido: No contiene --> Menu_Seleccion");
                return false;
            }

            JSONObject menuSeleccion = jsonObject.getJSONObject("Menu_Seleccion");

            ArrayList<String> SupportBlocks = new ArrayList<>();
            ArrayList<String> NextSupportBlocks = new ArrayList<>();

            for (String key : menuSeleccion.keySet()) {
                JSONObject menuItem = menuSeleccion.getJSONObject(key);

                if (!menuItem.has("Titulo") || !menuItem.has("Descripcion") || !menuItem.has("Emoji") || !menuItem.has("Support_block")) {
                    LoggerFactory.getLogger("M.I.M.I - Lectura Preguntas Json Config").error("La entrada con clave " + key + " tiene campos faltantes.");
                    return false;
                }

                if (menuItem.getString("Support_block").isEmpty()) {
                    LoggerFactory.getLogger("M.I.M.I - Lectura Preguntas Json Config").error("Existen SupportBlocks que estan vacios");
                    return false;
                }

                SupportBlocks.add(menuItem.getString("Support_block"));

                if (menuItem.has("Id_to_next_supportblock")) {
                    if (menuItem.getString("Id_to_next_supportblock").isEmpty()) {
                        LoggerFactory.getLogger("M.I.M.I - Lectura Preguntas Json Config").error("Existen NextSupportBlocks que estan vacios");
                        return false;
                    }
                    NextSupportBlocks.add(menuItem.getString("Id_to_next_supportblock"));
                } else if (menuItem.has("Path_to_message")) {
                    if (menuItem.getString("Path_to_message").isEmpty()) {
                        LoggerFactory.getLogger("M.I.M.I - Lectura Preguntas Json Config").error("Paths_to_messages que estan vacios");
                        return false;
                    }
                }
            }

            String baseSupportBlock = "base";
            boolean hasBaseSupportBlock = false;

            for (String SupportBlock : SupportBlocks) {
                if (Objects.equals(SupportBlock, baseSupportBlock)) {
                    hasBaseSupportBlock = true;
                    break;
                }
            }

            if (!hasBaseSupportBlock) {
                LoggerFactory.getLogger("M.I.M.I - Lectura Preguntas Json Config").error("No se ha encontrado un punto de inicio en la configuracion, recuerde que debe tener un SupportBlock denominado 'base' que sera el punto de inicio");
                return false;
            }

            if (!NextSupportBlocks.isEmpty()) {
                for (String NextSupportBlock : NextSupportBlocks) {
                    if (!SupportBlocks.contains(NextSupportBlock)) {
                        LoggerFactory.getLogger("M.I.M.I - Lectura Preguntas Json Config").error("El NextSupportBlock: " + NextSupportBlock + " No apunta hacia ningun SupportBlock existente");
                        return false;
                    }
                }
            }

            return true;

        } catch (IOException e) {
            LoggerFactory.getLogger("M.I.M.I - Lectura Preguntas Json Config").error("Error al leer el archivo JSON: " + e.getMessage());
            return false;
        } catch (JSONException e) {
            LoggerFactory.getLogger("M.I.M.I - Lectura Preguntas Json Config").error("Error al procesar el JSON: " + e.getMessage());
            return false;
        }
    }
}
