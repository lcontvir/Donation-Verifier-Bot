package com.lcontvir.bot.controlador.discord;

import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketEmbedBuilder {

    /**
     * Este método se utiliza para crear un nuevo objeto MessageEmbed para una solicitud de ticket.
     * El objeto MessageEmbed representa un mensaje de contenido enriquecido enviado por un usuario bot.
     *
     * @param canal El objeto TextChannel que representa el canal donde se realiza la solicitud de ticket.
     * @return Un objeto MessageEmbed con las siguientes propiedades:
     * - Título: "Sistema de Soporte"
     * - Descripción: Un mensaje que indica que se ha creado un canal privado para que el usuario se ponga en contacto con el personal. El ID del canal se incluye en el mensaje.
     * - Tipo: RICH (contenido enriquecido)
     * - Timestamp: La hora actual
     * - Color: 11674015 (un color específico en RGB)
     * - Pie de página: "Soporte Servidor"
     */
    public static MessageEmbed RequestTicketEmbed(TextChannel canal) {
        return new MessageEmbed(
                "",
                "Sistema de Soporte",
                "Se te ha creado un canal donde podras contactar privadamente con un staff:\n\n<#" + canal.getId() + ">",
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
     * Este método se utiliza para crear un nuevo objeto MessageEmbed para mostrar el panel de ticket.
     * El objeto MessageEmbed representa un mensaje de contenido enriquecido enviado por un usuario bot.
     *
     * @return Un objeto MessageEmbed con las siguientes propiedades:
     * - Título: "Tu Ticket"
     * - Descripción: Un mensaje de bienvenida al ticket del usuario, donde puede discutir tranquilamente y comentar los problemas que ha encontrado y necesita soporte. Incluye instrucciones para esperar a un moderador y cómo cerrar el ticket una vez resuelto el problema.
     * - Tipo: RICH (contenido enriquecido)
     * - Timestamp: La hora actual
     * - Color: 11674015 (un color específico en RGB)
     * - Pie de página: "Soporte Servidor"
     */
    public static MessageEmbed ShowTicketPanelEmbed() {
        return new MessageEmbed(
                "",
                "Tu Ticket",
                "Bienvenido a tu ticket, aqui podras charlar tranquilamente y comentar aquellos problemas que has encontrado y necesitas soporte.\n\n Cuando un moderador este disponible, este se pondra en contacto contigo, por ahora puedes esperar.\n Cuando tu duda se solucione, pulsa el boton para cerrar este ticket.",
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
     * Este método se utiliza para crear una lista de botones para la acción de cerrar un ticket.
     * Cada botón representa una acción interactiva en un mensaje de Discord.
     *
     * @return Una lista de objetos Button con las siguientes propiedades:
     * - ID: "CloseTicket"
     * - Label: "Finalizar Ticket"
     * - Style: PRIMARY (botón azul)
     */
    public static List<Button> CloseTicketActionRowButtons() {

        List<Button> buttons = new ArrayList<>();

        buttons.add(Button.primary("CloseTicket", "Finalizar Ticket"));

        return buttons;
    }

}
