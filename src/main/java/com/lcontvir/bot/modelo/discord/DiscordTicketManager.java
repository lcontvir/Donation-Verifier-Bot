package com.lcontvir.bot.modelo.discord;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.EnumSet;

public class DiscordTicketManager {

    /**
     * Este método se utiliza para crear un nuevo ticket de soporte en un canal de texto específico.
     *
     * @param miembro   El miembro del servidor que solicita el ticket de soporte.
     * @param categoria La categoría del servidor donde se creará el canal de texto para el ticket de soporte.
     * @return Un objeto TextChannel que representa el nuevo canal de texto creado para el ticket de soporte.
     * El canal de texto tiene las siguientes propiedades:
     * - Nombre: "Ticket de " seguido del nombre efectivo del miembro.
     * - Categoría: La categoría del servidor donde se creó el canal de texto.
     * - Permisos: El miembro tiene permisos para ver el canal y enviar mensajes en él.
     */
    public static TextChannel CreateTicket(Member miembro, String categoria) {

        String TicketName = "Ticket de " + miembro.getEffectiveName();
        Category SupportCategory = miembro.getGuild().getCategoryById(categoria);

        return SupportCategory.createTextChannel(TicketName).addMemberPermissionOverride(miembro.getIdLong(), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND), null).complete();
    }

}
