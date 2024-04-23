package com.lcontvir.bot.controlador.discord;

import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.time.OffsetDateTime;
import java.util.ArrayList;

public class FeedbackEmbedBuilder {

    /**
     * Crea y devuelve un objeto MessageEmbed para un mensaje de aprobación de feedback.
     *
     * @param miembro     El miembro de Discord que realiza el feedback.
     * @return {@link net.dv8tion.jda.api.entities.MessageEmbed} Un objeto MessageEmbed que contiene la información del mensaje de aprobación.
     */
    public static MessageEmbed FeedBackApprove(Member miembro) {

        return new MessageEmbed(
                "",
                "Feedback Registrado",
                "Se ha registrado tu feedback, un staff lo mirara y valorará",
                EmbedType.RICH,
                OffsetDateTime.now(),
                6618980,
                new MessageEmbed.Thumbnail("https://media.discordapp.net/attachments/1037337266697285685/1232237412160114699/positive-feedback-icon.png?ex=6628ba04&is=66276884&hm=ac7d8ce9f90f7bbb292aa4e2752ecb5cd718cd24e95f5a7399f237e6d285c2d7&=&format=webp&quality=lossless&width=765&height=768", "", 500, 500),
                null,
                new MessageEmbed.AuthorInfo(miembro.getEffectiveName(), "", miembro.getEffectiveAvatarUrl(), ""),
                null,
                new MessageEmbed.Footer("Feedback Servidor", "", ""),
                null,
                null);
    }

    public static MessageEmbed FeedBackApproveAnonimo() {

        return new MessageEmbed(
                "",
                "Feedback Registrado",
                "Se ha registrado tu feedback, un staff lo mirara y valorará",
                EmbedType.RICH,
                OffsetDateTime.now(),
                6618980,
                new MessageEmbed.Thumbnail("https://media.discordapp.net/attachments/1037337266697285685/1232237412160114699/positive-feedback-icon.png?ex=6628ba04&is=66276884&hm=ac7d8ce9f90f7bbb292aa4e2752ecb5cd718cd24e95f5a7399f237e6d285c2d7&=&format=webp&quality=lossless&width=765&height=768", "", 500, 500),
                null,
                new MessageEmbed.AuthorInfo("Anonimo", "https://media.discordapp.net/attachments/1037337266697285685/1232267756091609121/mask.png?ex=6628d647&is=662784c7&hm=46a35280cea1b692e9c8d561df03306726a81a2b4d689b9c9bd8f8ba578279ff&=&format=webp&quality=lossless&width=384&height=384", "", ""),
                null,
                new MessageEmbed.Footer("Feedback Servidor", "", ""),
                null,
                null);
    }

    public static MessageEmbed FeedBackRegister(Member miembro, String asunto, String cuerpo){

        ArrayList<MessageEmbed.Field> fields = new ArrayList<>();
        fields.add(new MessageEmbed.Field("Asunto", asunto, false));
        fields.add(new MessageEmbed.Field("Mensaje", cuerpo, false));

        return new MessageEmbed(
                "",
                "Nuevo Feedback!",
                null,
                EmbedType.RICH,
                OffsetDateTime.now(),
                6618980,
                new MessageEmbed.Thumbnail("https://media.discordapp.net/attachments/1037337266697285685/1232237412160114699/positive-feedback-icon.png?ex=6628ba04&is=66276884&hm=ac7d8ce9f90f7bbb292aa4e2752ecb5cd718cd24e95f5a7399f237e6d285c2d7&=&format=webp&quality=lossless&width=765&height=768", "", 500, 500),
                null,
                new MessageEmbed.AuthorInfo(miembro.getEffectiveName(),null, miembro.getAvatarUrl(), ""),
                null,
                new MessageEmbed.Footer("Feedback Servidor", "", ""),
                null,
                fields);
    }

    public static MessageEmbed FeedBackRegisterAnonimo(String asunto, String cuerpo){
        ArrayList<MessageEmbed.Field> fields = new ArrayList<>();
        fields.add(new MessageEmbed.Field("Asunto", asunto, false));
        fields.add(new MessageEmbed.Field("Mensaje", cuerpo, false));

        return new MessageEmbed(
                "",
                "Nuevo Feedback!",
                null,
                EmbedType.RICH,
                OffsetDateTime.now(),
                6618980,
                new MessageEmbed.Thumbnail("https://media.discordapp.net/attachments/1037337266697285685/1232237412160114699/positive-feedback-icon.png?ex=6628ba04&is=66276884&hm=ac7d8ce9f90f7bbb292aa4e2752ecb5cd718cd24e95f5a7399f237e6d285c2d7&=&format=webp&quality=lossless&width=765&height=768", "", 500, 500),
                null,
                new MessageEmbed.AuthorInfo("Anonimo",null, "https://media.discordapp.net/attachments/1037337266697285685/1232267756091609121/mask.png?ex=6628d647&is=662784c7&hm=46a35280cea1b692e9c8d561df03306726a81a2b4d689b9c9bd8f8ba578279ff&=&format=webp&quality=lossless&width=384&height=384", ""),
                null,
                new MessageEmbed.Footer("Feedback Servidor", "", ""),
                null,
                fields);
    }
}
