package com.lcontvir.bot.modelo.discord;

import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DiscordEntradaSoporteManager {

    /**
     * Este método se utiliza para obtener una lista de objetos EntradaSoporte que corresponden a un nombre de nodo específico.
     * Cada objeto EntradaSoporte representa una entrada de soporte en el sistema.
     *
     * @param nodeName El nombre del nodo para el cual se deben obtener las entradas de soporte.
     * @return Una lista de objetos EntradaSoporte que corresponden al nombre del nodo proporcionado.
     * Cada objeto EntradaSoporte tiene las siguientes propiedades:
     * - Node: El nombre del nodo al que pertenece la entrada de soporte.
     * - Titulo: El título de la entrada de soporte.
     * - Descripcion: La descripción de la entrada de soporte.
     * - Emoji: El emoji asociado a la entrada de soporte.
     */
    public static List<EntradaSoporte> getEntradasSoportebyNodeName(String nodeName) {
        List<EntradaSoporte> entradaSoportesBase = new ArrayList<>();
        try {
            List<EntradaSoporte> entradaSoportes = EntradaSoporte.CargarEntradasSoporte();
            for (EntradaSoporte entradaSoporte : entradaSoportes) {
                if (Objects.equals(entradaSoporte.getNode(), nodeName)) {
                    entradaSoportesBase.add(entradaSoporte);
                }
            }
        } catch (Exception ex) {
            LoggerFactory.getLogger("M.I.M.I - Entradas Soporte").error("Ha ocurrido un error al obtener las entradas de soporte por nombre de nodo: " + ex.getMessage());
        }
        return entradaSoportesBase;
    }

    /**
     * Este método se utiliza para obtener una lista de objetos EntradaSoporte que corresponden al nodo "base".
     * Cada objeto EntradaSoporte representa una entrada de soporte en el sistema.
     *
     * @return Una lista de objetos EntradaSoporte que corresponden al nodo "base".
     * Cada objeto EntradaSoporte tiene las siguientes propiedades:
     * - Node: El nombre del nodo al que pertenece la entrada de soporte, en este caso, "base".
     * - Titulo: El título de la entrada de soporte.
     * - Descripcion: La descripción de la entrada de soporte.
     * - Emoji: El emoji asociado a la entrada de soporte.
     */
    public static List<EntradaSoporte> getEntradasSoporteBase() {
        return getEntradasSoportebyNodeName("base");
    }

}
