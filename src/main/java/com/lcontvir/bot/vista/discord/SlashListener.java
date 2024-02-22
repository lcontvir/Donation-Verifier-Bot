package com.lcontvir.bot.vista.discord;

import com.lcontvir.bot.modelo.discord.DiscordManager;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
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
                        event.replyEmbeds(DiscordManager.NuevaDonacion(event.getMember(), event.getOption("steamid64").getAsString())).queue();
                        break;
                    case "ver-donacion":
                        event.replyEmbeds(DiscordManager.VerDonacion(event.getMember())).queue();
                        break;
                    case "modificar-donacion":
                        event.replyEmbeds(DiscordManager.ModificarDonacion(event.getMember(), event.getOption("steamid64").getAsString())).queue();
                        break;
                }
            } else {
                event.replyEmbeds(new MessageEmbed(
                        "",
                        "Error en la donacion",
                        "Ha ocurrido un error al donar, intentalo mas tarde",
                        EmbedType.RICH,
                        OffsetDateTime.now(),
                        16711680,
                        new MessageEmbed.Thumbnail("https://media.discordapp.net/attachments/1037337266697285685/1199103877262344212/error.png?ex=65c15382&is=65aede82&hm=bb13a110e392fd33774142a0e85f415b0ecd4bcdeec0048a02fb5069ee0a91ff&=&format=webp&quality=lossless", "", 500, 500),
                        null,
                        null,
                        null,
                        new MessageEmbed.Footer("Donaciones Servidor", "", ""),
                        null,
                        null)).queue();
            }
        } catch (Exception e) {
            LoggerFactory.getLogger("Bot Donaciones - Slash Listener").error("Ha ocurrido un problema al manejar los comandos slash: " + e.getMessage());
        }
    }
}
