package com.lcontvir.bot.vista.discord;

import com.lcontvir.bot.controlador.discord.SupportEmbedBuilder;
import com.lcontvir.bot.modelo.PropsLoader;
import com.lcontvir.bot.modelo.PropsVerificator;
import com.lcontvir.bot.modelo.json.JsonManager;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

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

            if(PropsVerificator.isFeedbackActive()){

                if(!PropsVerificator.VerifyFeedbackChannelId(event.getJDA())){
                    return;
                }
            }

            if(PropsVerificator.isSoporteActive()){
                if(!PropsVerificator.VerifySoporteChannelId(event.getJDA())){
                    return;
                }
                if(!PropsVerificator.VerifyTicketCategoryId(event.getJDA())){
                    return;
                }
            }

            ArrayList<SlashCommandData> Comandos = new ArrayList<>();

            if(PropsLoader.isConexionATercerosActive()){
                Comandos.add(Commands.slash("registrar-donacion", "Registra tu donacion")
                        .addOption(OptionType.STRING, "steamid64", "Tu steamid", true));
                Comandos.add(Commands.slash("ver-donacion", "Muestra los datos de tu donacion"));
                Comandos.add(Commands.slash("modificar-donacion", "Modifica los datos de tu donacion")
                        .addOption(OptionType.STRING, "steamid64", "Tu steamid", true));
            }

            if (PropsLoader.isSoporteActive()) {
                if (JsonManager.VerifyJsonConfig()){
                    Comandos.add(Commands.slash("soporte", "Obten soporte en el servidor"));
                    event.getJDA().getTextChannelById(PropsLoader.getSupportChannelId()).sendMessageEmbeds(SupportEmbedBuilder.RequestBaseSupportEmbed()).addActionRow(SupportEmbedBuilder.RequestBaseSupportActionRowButtons()).queue();
                }
            }

            if (PropsLoader.isFeedbackActive()){
                Comandos.add(Commands.slash("feedback", "Rellena un formulario de feedback"));
            }

            event.getJDA().updateCommands().addCommands(Comandos).queue();

        } catch (Exception e) {
            LoggerFactory.getLogger("M.I.M.I - Ready Listener").error("Ha ocurrido un problema al registrar los comandos: " + e.getMessage());
        }
    }
}
