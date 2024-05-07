package com.lcontvir.bot.vista.discord;

import com.lcontvir.bot.modelo.PropsVerificator;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.slf4j.LoggerFactory;

public class ReadyListener extends ListenerAdapter {

    /**
     * Este método se activa cuando el bot está listo para recibir comandos.
     * Registra los comandos slash `/registrar-donacion`, `/ver-donacion` y `/modificar-donacion` en todos los servidores donde el bot está presente.
     *
     * @param event El evento de estar listo que se dispara cuando el bot está listo para recibir comandos.
     */
    @Override
    public void onReady(ReadyEvent event) {
        try {
            event.getJDA().getGuildById("1037337265917153320").updateCommands().addCommands(
                    Commands.slash("registrar-donacion", "Registra tu donacion")
                            .addOption(OptionType.STRING, "steamid64", "Tu steamid", true),
                    Commands.slash("ver-donacion", "Muestra los datos de tu donacion"),
                    Commands.slash("modificar-donacion", "Modifica los datos de tu donacion")
                            .addOption(OptionType.STRING, "steamid64", "Tu steamid", true),
                    Commands.slash("soporte", "Obten soporte en el servidor")
            ).queue();

            PropsVerificator.VerifyFeedbackChannelId(event.getJDA());

        } catch (Exception e) {
            LoggerFactory.getLogger("Bot Donaciones - Ready Listener").error("Ha ocurrido un problema al registrar los comandos: " + e.getMessage());
        }
    }
}
