package com.lcontvir.bot.vista.discord;

import com.lcontvir.bot.controlador.discord.SupportEmbedBuilder;
import com.lcontvir.bot.controlador.discord.TicketEmbedBuilder;
import com.lcontvir.bot.modelo.PropsLoader;
import com.lcontvir.bot.modelo.discord.DiscordEntradaSoporteManager;
import com.lcontvir.bot.modelo.discord.DiscordTicketManager;
import com.lcontvir.bot.modelo.discord.EntradaSoporte;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.LoggerFactory;

public class ButtonListener extends ListenerAdapter {

    /**
     * Este método se activa cuando ocurre una interacción de botón en Discord.
     *
     * @param event El evento de interacción de botón que contiene la información sobre la interacción realizada por el usuario.
     *              <p>
     *              Primero, se verifica si la funcionalidad de soporte está activa.
     *              Si está activa, se realiza una operación switch en el ID del botón del evento.
     *              <p>
     *              Si el ID del botón es "AutomaticSupport", se responde al evento con un embed de soporte y se añade una fila de acción con un menú de selección de soporte.
     *              La respuesta es efímera (solo visible para el usuario que realizó la interacción).
     *              <p>
     *              Si el ID del botón es "CloseAutomaticSupport", se edita el mensaje del evento para agradecer al usuario por usar los servicios.
     *              <p>
     *              Si el ID del botón es "AutomaticSupportContinue", se edita el mensaje del evento para mostrar un embed de soporte y se añade una fila de acción con un menú de selección de soporte.
     *              <p>
     *              Si el ID del botón es "ManualSupport", se crea un ticket para el miembro que realizó la interacción y se envía un mensaje al canal del ticket con un embed de panel de ticket y una fila de acción con botones para cerrar el ticket.
     *              Se responde al evento con un embed de solicitud de ticket. La respuesta es efímera.
     *              <p>
     *              Si el ID del botón es "ManualSupportContinue", se crea un ticket para el miembro que realizó la interacción y se envía un mensaje al canal del ticket con un embed de panel de ticket y una fila de acción con botones para cerrar el ticket.
     *              Se edita el mensaje del evento para mostrar un embed de solicitud de ticket.
     *              <p>
     *              Si el ID del botón es "CloseTicket", se elimina el canal de texto del evento.
     */
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        try {
            if (PropsLoader.isSoporteActive()) {
                switch (event.getButton().getId()) {
                    case "AutomaticSupport":
                        event.replyEmbeds(SupportEmbedBuilder.RequestSupportEmbed(EntradaSoporte.readSupportSpecificFile("ejemplo6.md"))).addActionRow(SupportEmbedBuilder.RequestSupportActionRowMenu(DiscordEntradaSoporteManager.getEntradasSoporteBase())).setEphemeral(true).queue();
                        break;
                    case "CloseAutomaticSupport":
                        event.editMessage("Gracias por usar nuestros servicios!").setReplace(true).queue();
                        break;
                    case "AutomaticSupportContinue":
                        event.editMessageEmbeds(SupportEmbedBuilder.RequestSupportEmbed(EntradaSoporte.readSupportSpecificFile("ejemplo6.md"))).setActionRow(SupportEmbedBuilder.RequestSupportActionRowMenu(DiscordEntradaSoporteManager.getEntradasSoporteBase())).queue();
                        break;
                    case "ManualSupport":
                        TextChannel ticket = DiscordTicketManager.CreateTicket(event.getMember(), "1238812869206741032");
                        ticket.sendMessageEmbeds(TicketEmbedBuilder.ShowTicketPanelEmbed()).addActionRow(TicketEmbedBuilder.CloseTicketActionRowButtons()).queue();
                        event.replyEmbeds(TicketEmbedBuilder.RequestTicketEmbed(ticket)).setEphemeral(true).queue();
                        break;
                    case "ManualSupportContinue":
                        TextChannel ticketContinue = DiscordTicketManager.CreateTicket(event.getMember(), "1238812869206741032");
                        ticketContinue.sendMessageEmbeds(TicketEmbedBuilder.ShowTicketPanelEmbed()).addActionRow(TicketEmbedBuilder.CloseTicketActionRowButtons()).queue();
                        event.editMessageEmbeds(TicketEmbedBuilder.RequestTicketEmbed(ticketContinue)).setReplace(true).queue();
                        break;
                    case "CloseTicket":
                        event.getChannel().asTextChannel().delete().queue();
                        break;
                }
            }
        } catch (Exception ex) {
            LoggerFactory.getLogger("M.I.M.I - Discord Button Listener").error("Ha ocurrido un error al procesar la interacción de botón: " + ex.getMessage());
        }
    }
}
