package com.lcontvir.bot.controlador.discord;

import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class SupportEmbedBuilder {

    public static MessageEmbed RequestBaseSupportEmbed(){

        MessageEmbed.AuthorInfo infoAutor;

        return new MessageEmbed(
                "",
                "Sistema de Soporte",
                "Bienvenido al sistema de soporte del servidor, en que te podemos ayudar!",
                EmbedType.RICH,
                OffsetDateTime.now(),
                0,
                null,
                null,
                null,
                null,
                new MessageEmbed.Footer("Soporte Servidor", "", ""),
                null,
                null);
    }

    public static List<Button> RequestBaseSupportActionRowButtons(){

        List<Button> buttons = new ArrayList<>();;

        buttons.add(Button.primary("AutomaticSupport", "Explorar opciones de soporte"));
        buttons.add(Button.secondary("ManualSupport", "Contactar con un administrador"));

        return buttons;
    }
}
