package com.lcontvir.bot.controlador.discord;

import com.lcontvir.bot.modelo.discord.Donador;
import com.lcontvir.bot.modelo.steam.SteamJugador;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.time.OffsetDateTime;
import java.util.ArrayList;

public class DonationEmbedBuilder {

    /**
     * Crea y devuelve un objeto MessageEmbed para un mensaje de aprobación de donación.
     *
     * @param titulo      El título del mensaje de aprobación.
     * @param descripcion La descripción del mensaje de aprobación.
     * @param miembro     El miembro de Discord que realiza la donación.
     * @param jugador     El jugador de Steam que realiza la donación.
     * @param donacion    La donación que se está aprobando.
     * @return {@link net.dv8tion.jda.api.entities.MessageEmbed} Un objeto MessageEmbed que contiene la información del mensaje de aprobación.
     */
    public static MessageEmbed DonationApprove(String titulo, String descripcion, Member miembro, SteamJugador jugador, Donador donacion) {

        ArrayList<MessageEmbed.Field> fields = new ArrayList<>();
        fields.add(new MessageEmbed.Field(
                "Datos del registro", "- Usuario: " + jugador.getPersonaname() + "\n- SteamId64: " + jugador.getSteamid() + "\n- Rol de donador: " + donacion.getRoleDonador().getNombre() + "\n- Dias restantes: " + donacion.calcularDiasRestantes(), true
        ));

        return new MessageEmbed(
                "",
                titulo,
                descripcion,
                EmbedType.RICH,
                OffsetDateTime.now(),
                6618980,
                new MessageEmbed.Thumbnail("https://media.discordapp.net/attachments/1037337266697285685/1199103877023289505/cash.png?ex=65c15382&is=65aede82&hm=18f774a85cce0300655e9e711530269b5e7f2e107abe08f44794abe7ac3174a7&=&format=webp&quality=lossless", "", 500, 500),
                null,
                new MessageEmbed.AuthorInfo(miembro.getEffectiveName(), "", miembro.getEffectiveAvatarUrl(), ""),
                null,
                new MessageEmbed.Footer("Donaciones Servidor", "", ""),
                new MessageEmbed.ImageInfo(jugador.getAvatarfull(), "", 500, 500),
                fields);
    }

    /**
     * Crea y devuelve un objeto MessageEmbed para un mensaje de denegación de donación.
     *
     * @param titulo      El título del mensaje de denegación.
     * @param descripcion La descripción del mensaje de denegación.
     * @param miembro     El miembro de Discord al que se le deniega la donación.
     * @return {@link net.dv8tion.jda.api.EmbedBuilder} Un objeto MessageEmbed que contiene la información del mensaje de denegación.
     */
    public static MessageEmbed DonationDeny(String titulo, String descripcion, Member miembro) {

        return new MessageEmbed(
                "",
                titulo,
                descripcion,
                EmbedType.RICH,
                OffsetDateTime.now(),
                16711680,
                new MessageEmbed.Thumbnail("https://media.discordapp.net/attachments/1037337266697285685/1199103877262344212/error.png?ex=65c15382&is=65aede82&hm=bb13a110e392fd33774142a0e85f415b0ecd4bcdeec0048a02fb5069ee0a91ff&=&format=webp&quality=lossless", "", 500, 500),
                null,
                new MessageEmbed.AuthorInfo(miembro.getEffectiveName(), "", miembro.getEffectiveAvatarUrl(), ""),
                null,
                new MessageEmbed.Footer("Donaciones Servidor", "", ""),
                null,
                null);
    }
}
