package com.lcontvir.bot.controlador.discord;

import com.lcontvir.bot.modelo.PropsLoader;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.time.OffsetDateTime;
import java.util.ArrayList;

public class FeedbackEmbedBuilder {

    /**
     * Crea y devuelve un objeto MessageEmbed para un mensaje de aprobación de feedback.
     *
     * @param miembro El miembro de Discord que realiza el feedback.
     * @return {@link net.dv8tion.jda.api.entities.MessageEmbed} Un objeto MessageEmbed que contiene la información del mensaje de aprobación.
     */
    public static MessageEmbed FeedBackApprove(Member miembro, boolean IsAnonimo) {

        MessageEmbed.AuthorInfo infoAutor;

        if (IsAnonimo) {
            infoAutor = new MessageEmbed.AuthorInfo("Anonimo", "", null, "");
        } else {
            infoAutor = new MessageEmbed.AuthorInfo(miembro.getEffectiveName(), "", miembro.getEffectiveAvatarUrl(), "");
        }

        return new MessageEmbed(
                "",
                "Feedback Registrado",
                "Se ha registrado tu feedback, un staff lo mirara y valorará",
                EmbedType.RICH,
                OffsetDateTime.now(),
                6618980,
                new MessageEmbed.Thumbnail("https://media.discordapp.net/attachments/1037337266697285685/1232237412160114699/positive-feedback-icon.png?ex=6628ba04&is=66276884&hm=ac7d8ce9f90f7bbb292aa4e2752ecb5cd718cd24e95f5a7399f237e6d285c2d7&=&format=webp&quality=lossless&width=765&height=768", "", 500, 500),
                null,
                infoAutor,
                null,
                new MessageEmbed.Footer("Feedback Servidor", "", ""),
                null,
                null);
    }

    /**
     * Crea y devuelve un objeto MessageEmbed para mostrar el mensaje de feedback en el canal correspondiente.
     *
     * @param miembro El miembro de Discord que realiza el feedback.
     * @return {@link net.dv8tion.jda.api.entities.MessageEmbed} Un objeto MessageEmbed que contiene la información del mensaje de aprobación.
     */
    public static MessageEmbed FeedBackRegister(String asunto, String cuerpo, Member miembro, boolean IsAnonimo) {

        MessageEmbed.AuthorInfo infoAutor;

        if (IsAnonimo) {
            infoAutor = new MessageEmbed.AuthorInfo("Anonimo", "", null, "");
        } else {
            infoAutor = new MessageEmbed.AuthorInfo(miembro.getEffectiveName(), "", miembro.getEffectiveAvatarUrl(), "");
        }

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
                infoAutor,
                null,
                new MessageEmbed.Footer("Feedback Servidor", "", ""),
                null,
                fields);
    }

    /**
     * Este método crea y devuelve un objeto MessageEmbed para un mensaje de comando inactivo.
     *
     * @return {@link net.dv8tion.jda.api.entities.MessageEmbed} Un objeto MessageEmbed que contiene la información del mensaje de comando inactivo.
     * El mensaje indica que el comando está inactivo en este momento.
     * El tipo de embed es RICH, el color es 16711680 (rojo), y el pie de página indica "Feedback Servidor".
     */
    public static MessageEmbed FeedBackInactive() {

        return new MessageEmbed(
                "",
                "Comando Inactivo!",
                "Este comando esta inactivo en este momento!",
                EmbedType.RICH,
                OffsetDateTime.now(),
                16711680,
                null,
                null,
                null,
                null,
                new MessageEmbed.Footer("Feedback Servidor", "", ""),
                null,
                null);
    }

    /**
     * Registra un feedback en el canal correspondiente y devuelve una respuesta en forma de MessageEmbed.
     *
     * @param nombre  El nombre del remitente del feedback. Si está vacío o es "Anonimo", se considera anónimo.
     * @param asunto  El asunto del feedback.
     * @param cuerpo  El cuerpo del feedback.
     * @param miembro El miembro que envía el feedback.
     * @return Una respuesta en forma de MessageEmbed que indica que el feedback ha sido registrado correctamente.
     * @see TextChannel
     * @see MessageEmbed
     * @see FeedbackEmbedBuilder#FeedBackRegister(String, String, Member, boolean)
     * @see FeedbackEmbedBuilder#FeedBackApprove(Member, boolean)
     */
    public static MessageEmbed RegistrarFeedback(String nombre, String asunto, String cuerpo, Member miembro) {
        TextChannel feedbackChannel = miembro.getJDA().getTextChannelById(PropsLoader.getFeedbackChannelId());

        MessageEmbed response;

        if (nombre.isEmpty() || nombre.equals("Anonimo")) {
            feedbackChannel.sendMessageEmbeds(FeedbackEmbedBuilder.FeedBackRegister(asunto, cuerpo, miembro, true)).queue();
            response = FeedbackEmbedBuilder.FeedBackApprove(miembro, true);
        } else {
            feedbackChannel.sendMessageEmbeds(FeedbackEmbedBuilder.FeedBackRegister(asunto, cuerpo, miembro, false)).queue();
            response = FeedbackEmbedBuilder.FeedBackApprove(miembro, false);
        }
        return response;
    }
}
