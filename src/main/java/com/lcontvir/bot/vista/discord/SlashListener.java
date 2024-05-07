package com.lcontvir.bot.vista.discord;

import com.lcontvir.bot.controlador.discord.FeedbackEmbedBuilder;
import com.lcontvir.bot.controlador.discord.SupportEmbedBuilder;
import com.lcontvir.bot.modelo.PropsVerificator;
import com.lcontvir.bot.modelo.discord.DiscordDonationManager;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;


public class SlashListener extends ListenerAdapter {

    /**
     * Este método se activa cuando se realiza un comando slash en Discord.
     * <p>
     * Primero, verifica si el miembro que realizó el comando no es nulo. Si el miembro es nulo, se envía un mensaje de error.
     * Si el miembro no es nulo, se verifica el nombre del comando y se ejecuta la acción correspondiente.
     *
     * @param event Es el evento de interacción del comando slash que contiene la información del comando realizado.
     */
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        try {
            if (event.getMember() != null) {
                switch (event.getName()) {
                    case "registrar-donacion":
                        event.replyEmbeds(DiscordDonationManager.NuevaDonacion(event.getMember(), event.getOption("steamid64").getAsString())).queue();
                        break;
                    case "ver-donacion":
                        event.replyEmbeds(DiscordDonationManager.VerDonacion(event.getMember())).queue();
                        break;
                    case "modificar-donacion":
                        event.replyEmbeds(DiscordDonationManager.ModificarDonacion(event.getMember(), event.getOption("steamid64").getAsString())).queue();
                        break;
                    case "feedback":
                        if(PropsVerificator.feedbackCommandActive){

                            TextInput usuario = TextInput.create("usuario", "Usuario", TextInputStyle.PARAGRAPH)
                                    .setPlaceholder("Dejalo en blanco si quieres que sea anonimo")
                                    .setMinLength(3)
                                    .setRequired(false)
                                    .build();

                            TextInput asunto = TextInput.create("asunto", "Asunto", TextInputStyle.SHORT)
                                    .setPlaceholder("Asunto del feedback")
                                    .setMinLength(5)
                                    .setMaxLength(30)
                                    .build();

                            TextInput body = TextInput.create("cuerpo", "Cuerpo", TextInputStyle.PARAGRAPH)
                                    .setPlaceholder("Comunica lo que necesites!")
                                    .setMinLength(0)
                                    .setMaxLength(1024)
                                    .build();

                            Modal modal = Modal.create("feedback", "Feedback")
                                    .addComponents(ActionRow.of(usuario), ActionRow.of(asunto), ActionRow.of(body))
                                    .build();

                            event.replyModal(modal).queue();
                        }
                        else{
                            event.replyEmbeds(FeedbackEmbedBuilder.FeedBackInactive()).queue();
                        }
                    case "soporte":
                            event.replyEmbeds(SupportEmbedBuilder.RequestBaseSupportEmbed()).addActionRow(SupportEmbedBuilder.RequestBaseSupportActionRowButtons()).queue();
                }
            } else {
                event.replyEmbeds(new MessageEmbed(
                        "",
                        "Error Interno",
                        "Ha ocurrido un error, intentalo de nuevo mas tarde, si sique sin funcionar, ponte en contacto con un administrador",
                        EmbedType.RICH,
                        OffsetDateTime.now(),
                        16711680,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null)).queue();
            }
        } catch (Exception e) {
            LoggerFactory.getLogger("Bot Donaciones - Slash Listener").error("Ha ocurrido un problema al manejar los comandos slash: " + e.getMessage());
        }
    }
}
