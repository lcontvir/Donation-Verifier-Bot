package com.lcontvir.bot.controlador.discord;

import com.lcontvir.bot.modelo.discord.EntradaSoporte;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class SupportEmbedBuilder {

    /**
     * Este método se utiliza para crear un nuevo objeto MessageEmbed para una solicitud de soporte inicial.
     * El objeto MessageEmbed representa un mensaje de contenido enriquecido enviado por un usuario bot.
     *
     * @return Un objeto MessageEmbed con las siguientes propiedades:
     * - Título: "Sistema de Soporte"
     * - Descripción: "Bienvenido al sistema de soporte del servidor, en que te podemos ayudar!"
     * - Tipo: RICH (contenido enriquecido)
     * - Timestamp: La hora actual
     * - Color: 11674015 (un color específico en RGB)
     * - Pie de página: "Soporte Servidor"
     */
    public static MessageEmbed RequestBaseSupportEmbed() {
        return new MessageEmbed(
                "",
                "Sistema de Soporte",
                "Bienvenido al sistema de soporte del servidor, en que te podemos ayudar!",
                EmbedType.RICH,
                OffsetDateTime.now(),
                11674015,
                null,
                null,
                null,
                null,
                new MessageEmbed.Footer("Soporte Servidor", "", ""),
                null,
                null);
    }


    /**
     * Este método se utiliza para crear una lista de botones para las acciones iniciales de una solicitud de soporte.
     * Cada botón representa una acción interactiva en un mensaje de Discord.
     *
     * @return Una lista de objetos Button con las siguientes propiedades:
     * - ID: "AutomaticSupport" para el primer botón y "ManualSupport" para el segundo botón.
     * - Label: "Explorar opciones de soporte" para el primer botón y "Contactar con un administrador" para el segundo botón.
     * - Style: PRIMARY (botón azul) para el primer botón y DANGER (botón rojo) para el segundo botón.
     */
    public static List<Button> RequestBaseSupportActionRowButtons() {

        List<Button> buttons = new ArrayList<>();

        buttons.add(Button.primary("AutomaticSupport", "Explorar opciones de soporte"));
        buttons.add(Button.danger("ManualSupport", "Contactar con un administrador"));

        return buttons;
    }

    /**
     * Este método se utiliza para crear una lista de botones para las acciones finales de una solicitud de soporte.
     * Cada botón representa una acción interactiva en un mensaje de Discord.
     *
     * @return Una lista de objetos Button con las siguientes propiedades:
     * - ID: "ManualSupportContinue" para el primer botón y "AutomaticSupportContinue" para el segundo botón.
     * - Label: "Contactar con un administrador" para el primer botón y "Volver a otras opciones de soporte" para el segundo botón.
     * - Style: DANGER (botón rojo) para el primer botón y SECONDARY (botón gris) para el segundo botón.
     */
    public static List<Button> RequestEndBaseSupportActionRowButtons() {

        List<Button> buttons = new ArrayList<>();

        buttons.add(Button.danger("ManualSupportContinue", "Contactar con un administrador"));
        buttons.add(Button.secondary("AutomaticSupportContinue", "Volver a otras opciones de soporte"));

        return buttons;
    }

    /**
     * Este método se utiliza para crear un nuevo objeto StringSelectMenu para una solicitud de soporte.
     * El objeto StringSelectMenu representa un menú de selección en un mensaje de Discord.
     *
     * @param entradaSoporteList La lista de objetos EntradaSoporte que se mostrarán como opciones en el menú de selección.
     * @return Un objeto StringSelectMenu con las opciones correspondientes a los títulos de las entradas de soporte proporcionadas.
     * Cada opción tiene las siguientes propiedades:
     * - Label: El título de la entrada de soporte.
     * - Value: El título de la entrada de soporte en minúsculas y sin espacios.
     * - Description: La descripción de la entrada de soporte.
     * - Emoji: El emoji asociado a la entrada de soporte.
     */
    public static StringSelectMenu RequestSupportActionRowMenu(List<EntradaSoporte> entradaSoporteList) {

        StringSelectMenu.Builder menu = StringSelectMenu.create("SupportCommonSelection");

        for (EntradaSoporte n : entradaSoporteList) {
            menu.addOption(n.getTitulo(), n.getTitulo().toLowerCase().trim(), n.getDescripcion(), Emoji.fromUnicode(n.getEmoji()));
        }

        return menu.build();

    }

    /**
     * Este método se utiliza para crear un nuevo objeto MessageEmbed para una solicitud de soporte.
     * El objeto MessageEmbed representa un mensaje de contenido enriquecido enviado por un usuario bot.
     *
     * @param description La descripción que se mostrará en el objeto MessageEmbed.
     * @return Un objeto MessageEmbed con las siguientes propiedades:
     * - Título: "Sistema de Soporte"
     * - Descripción: La descripción proporcionada como parámetro.
     * - Tipo: RICH (contenido enriquecido)
     * - Timestamp: La hora actual
     * - Color: 11674015 (un color específico en RGB)
     * - Pie de página: "Soporte Servidor"
     */
    public static MessageEmbed RequestSupportEmbed(String description) {
        return new MessageEmbed(
                "",
                "Sistema de Soporte",
                description,
                EmbedType.RICH,
                OffsetDateTime.now(),
                11674015,
                null,
                null,
                null,
                null,
                new MessageEmbed.Footer("Soporte Servidor", "", ""),
                null,
                null);
    }
}
