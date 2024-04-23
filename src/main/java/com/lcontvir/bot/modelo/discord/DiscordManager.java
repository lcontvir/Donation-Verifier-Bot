package com.lcontvir.bot.modelo.discord;

import com.lcontvir.bot.controlador.discord.DonationEmbedBuilder;
import com.lcontvir.bot.controlador.discord.FeedbackEmbedBuilder;
import com.lcontvir.bot.controlador.jdbc.DiscordJdbcExtensions;
import com.lcontvir.bot.controlador.jdbc.OperacionesCRUD;
import com.lcontvir.bot.modelo.PropsLoader;
import com.lcontvir.bot.modelo.steam.SteamJugador;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.spi.CalendarNameProvider;

import static com.lcontvir.bot.controlador.jdbc.OperacionesCRUD.obtenerDonador;

public class DiscordManager {

    /**
     * Este método se encarga de registrar una nueva donación en el sistema.
     *
     * @param miembro Es el miembro de Discord que realiza la donación.
     * @param opcion  Es la opción de donación seleccionada por el miembro.
     * @return Retorna un objeto {@link MessageEmbed} que contiene la respuesta del sistema a la donación.
     * Esta respuesta puede ser de aprobación si la donación se registra correctamente,
     * o de negación si ocurre algún error durante el proceso de registro.
     * En caso de error, el mensaje incluirá una descripción detallada del mismo.
     */
    public static MessageEmbed NuevaDonacion(Member miembro, String opcion) {
        Role rolSeleccionadoDiscord;
        MessageEmbed respuesta = DonationEmbedBuilder.DonationDeny("Error en la donacion", "Ha ocurrido un error con tu donacion, contacta a un Administrador.", miembro);
        SteamJugador jugador;
        Donador donador;

        try {
            rolSeleccionadoDiscord = DiscordJdbcExtensions.GetMatchingDonationRoleFromDB(miembro);

            if (!DiscordJdbcExtensions.haDonado(miembro)) {
                if (rolSeleccionadoDiscord != null) {

                    jugador = SteamJugador.GetSteamJugador(opcion);

                    if (jugador != null) {

                        OperacionesCRUD.RegistrarDonacionBBDD(jugador, miembro, rolSeleccionadoDiscord.getId());
                        donador = obtenerDonador(miembro);
                        respuesta = DonationEmbedBuilder.DonationApprove("Donacion Registrada!", "Registro de donacion realizado con exito", miembro, jugador, donador);

                    } else {
                        respuesta = DonationEmbedBuilder.DonationDeny("Error en la donacion!", "La id proporcionada no es valida, por favor, verificala!", miembro);
                    }

                } else {
                    respuesta = DonationEmbedBuilder.DonationDeny("Error en la donacion!", "No tienes un rol de donador! Comprueba que tienes las cuentas del servicio de donacion conectadas con discord!", miembro);
                }
            } else {
                donador = obtenerDonador(miembro);
                jugador = SteamJugador.GetSteamJugador(donador.getSteamid64());
                respuesta = DonationEmbedBuilder.DonationApprove("Donacion Previamente Registrada!", "Ya existe una donacion previa! Modifique su donacion!", miembro, jugador, donador);
            }
        } catch (Exception e) {
            LoggerFactory.getLogger("Bot Donaciones - Discord Manager").error("Ha ocurrido un error al registrar una nueva donacion: " + e.getMessage());
        }
        return respuesta;
    }

    /**
     * Modifica la donación de un miembro en la base de datos.
     * <p>
     * Este método recibe un miembro y una opción como parámetros. Primero, verifica si el miembro está en cooldown.
     * Si no está en cooldown, verifica si el miembro ha donado antes. Si ha donado, verifica si tiene un rol de donador.
     * Si tiene un rol de donador, intenta obtener el jugador de Steam a partir de la opción proporcionada.
     * Si el jugador es válido, actualiza la donación en la base de datos y construye un mensaje de aprobación.
     * Si en cualquier punto se encuentra un error, se construye un mensaje de denegación con la descripción del error.
     *
     * @param miembro El miembro cuya donación se va a modificar.
     * @param opcion  La opción que se utilizará para obtener el jugador de Steam.
     * @return Un objeto {@link MessageEmbed} que contiene el mensaje de aprobación si la donación se modificó con éxito, o el mensaje de denegación si ocurrió un error.
     */

    public static MessageEmbed ModificarDonacion(Member miembro, String opcion) {
        Role rolSeleccionadoDiscord;
        MessageEmbed respuesta = DonationEmbedBuilder.DonationDeny("Error en la donacion", "Ha ocurrido un error con tu donacion, contacta a un Administrador.", miembro);
        SteamJugador jugador;
        Donador donador;

        try {
            rolSeleccionadoDiscord = DiscordJdbcExtensions.GetMatchingDonationRoleFromDB(miembro);
            if (!CommandCoolDown.IsOnCooldown(miembro)) {
                if (DiscordJdbcExtensions.haDonado(miembro)) {
                    if (rolSeleccionadoDiscord != null) {

                        jugador = SteamJugador.GetSteamJugador(opcion);

                        if (jugador != null) {

                            OperacionesCRUD.ActualizarDonacionBBDD(jugador, miembro, rolSeleccionadoDiscord.getId());
                            donador = obtenerDonador(miembro);
                            respuesta = DonationEmbedBuilder.DonationApprove("Donacion Modificada!", "Registro de donacion modificada con exito", miembro, jugador, donador);
                            CommandCoolDown.AddCooldown(miembro);


                        } else {
                            respuesta = DonationEmbedBuilder.DonationDeny("Error en la donacion!", "La id proporcionada no es valida, por favor, verificala!", miembro);
                        }
                    } else {
                        respuesta = DonationEmbedBuilder.DonationDeny("Error en la donacion!", "No tienes un rol de donador! Comprueba que tienes las cuentas del servicio de donacion conectadas con discord!", miembro);
                    }
                } else {
                    respuesta = DonationEmbedBuilder.DonationDeny("Donacion Previamente No Registrada!", "No existe una donacion previa! Registre su donacion!", miembro);
                }
            } else {
                respuesta = DonationEmbedBuilder.DonationDeny("Donacion No Modificada!", "No puedes ejecutar este comando todavia por que esta en cooldown, espera un tiempo para ejecutarlo!", miembro);
            }
        } catch (Exception e) {
            LoggerFactory.getLogger("Bot Donaciones - Discord Manager").error("Ha ocurrido un error al modificar una nueva donacion: " + e.getMessage());
        }
        return respuesta;
    }

    /**
     * Este método permite ver la información de la donación de un miembro específico.
     *
     * @param miembro El miembro de Discord cuya información de donación se desea ver.
     * @return Un objeto {@link MessageEmbed} que contiene la información de la donación del miembro.
     * Si el miembro ha hecho una donación, se devuelve un mensaje de agradecimiento con los detalles de la donación.
     * Si el miembro no ha hecho una donación, se devuelve un mensaje indicando que no se ha registrado ninguna donación.
     * En caso de error durante el proceso, se devuelve un mensaje de error genérico.
     */


    public static MessageEmbed VerDonacion(Member miembro) {
        MessageEmbed respuesta = DonationEmbedBuilder.DonationDeny("Error en la donacion", "Ha ocurrido un error con tu donacion, contacta a un Administrador.", miembro);
        SteamJugador jugador;
        Donador donador;

        try {
            if (DiscordJdbcExtensions.haDonado(miembro)) {
                donador = obtenerDonador(miembro);
                jugador = SteamJugador.GetSteamJugador(donador.getSteamid64());
                respuesta = DonationEmbedBuilder.DonationApprove("Tu Donacion!", "Gracias por donar a nuestro servidor!", miembro, jugador, donador);
            } else {
                respuesta = DonationEmbedBuilder.DonationDeny("Donacion Previamente No Registrada!", "No existe una donacion previa! Registre su donacion!", miembro);
            }
        } catch (Exception e) {
            LoggerFactory.getLogger("Bot Donaciones - Discord Manager").error("Ha ocurrido un error al ver una nueva donacion: " + e.getMessage());
        }

        return respuesta;
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
