package com.lcontvir.bot.vista.discord;

import com.lcontvir.bot.controlador.discord.FeedbackEmbedBuilder;
import com.lcontvir.bot.modelo.discord.DiscordManager;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ModalListener extends ListenerAdapter {
    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equals("feedback")) {
            String usuario = event.getValue("usuario").getAsString();
            String asunto = event.getValue("asunto").getAsString();
            String cuerpo = event.getValue("cuerpo").getAsString();
            MessageEmbed respuesta ;
            //createSupportTicket(subject, body);
            if(!usuario.isEmpty()){
                DiscordManager.RegistrarFeedback(asunto, cuerpo, event.getMember());
                respuesta = FeedbackEmbedBuilder.FeedBackApprove(event.getMember());
            }else{
                DiscordManager.RegistrarFeedbackAnonimo(asunto, cuerpo, event.getGuild());
                respuesta = FeedbackEmbedBuilder.FeedBackApproveAnonimo();
            }
            event.replyEmbeds(respuesta).setEphemeral(true).queue();
        }
    }
}
