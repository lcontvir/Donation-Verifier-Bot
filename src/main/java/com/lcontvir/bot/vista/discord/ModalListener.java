package com.lcontvir.bot.vista.discord;

import com.lcontvir.bot.controlador.discord.FeedbackEmbedBuilder;
import com.lcontvir.bot.modelo.PropsVerificator;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.LoggerFactory;

public class ModalListener extends ListenerAdapter {
    /**
     * Este método se activa cuando ocurre una interacción modal en Discord.
     *
     * @param event El evento de interacción modal que contiene la información sobre la interacción realizada por el usuario.
     *              <p>
     *              Primero, se verifica si la funcionalidad de feedback está activa.
     *              Si está activa, se verifica si el ID del modal es "feedback".
     *              Si es "feedback", se obtienen los valores de "usuario", "asunto" y "cuerpo" del evento.
     *              Luego, se llama al método RegistrarFeedback de la clase DiscordDonationManager, pasando los valores obtenidos y el miembro que realizó la interacción.
     *              Se obtiene un objeto MessageEmbed como respuesta.
     *              Finalmente, se responde al evento con el embed de respuesta y se establece que la respuesta sea efímera (solo visible para el usuario que realizó la interacción).
     */
    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        try {
            if (PropsVerificator.isFeedbackActive()) {
                if (event.getModalId().equals("feedback")) {
                    String usuario = event.getValue("usuario").getAsString();
                    String asunto = event.getValue("asunto").getAsString();
                    String cuerpo = event.getValue("cuerpo").getAsString();
                    MessageEmbed respuesta = FeedbackEmbedBuilder.RegistrarFeedback(usuario, asunto, cuerpo, event.getMember());
                    event.replyEmbeds(respuesta).setEphemeral(true).queue();
                }
            }
        } catch (Exception ex) {
            LoggerFactory.getLogger("M.I.M.I - Discord Modal Listener").error("Ha ocurrido un error al procesar la interacción modal: " + ex.getMessage());
        }
    }
}
