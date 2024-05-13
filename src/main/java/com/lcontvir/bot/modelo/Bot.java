package com.lcontvir.bot.modelo;

import com.lcontvir.bot.modelo.jdbc.ConexionBD;
import com.lcontvir.bot.vista.discord.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.LoggerFactory;

public class Bot {


    public static void main(String[] arguments) {
        try {
            PropsLoader.loadAppProps();

            if(!PropsVerificator.VerifyDiscordConexion()){
                return;
            }

            if (PropsLoader.isConexionATercerosActive()) {

                if (!PropsVerificator.VerifySteamConexion() || !PropsVerificator.VerifyDatabaseConexion() || !ConexionBD.PrepareDatabase()){
                    return;
                }
                PropsVerificator.VerifyCooldownHours();
                PropsVerificator.VerifyExpireDays();
            }

            JDA jda = JDABuilder.createLight(PropsLoader.getJDAToken(), GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS).build();
            jda.addEventListener(new ReadyListener());
            jda.addEventListener(new SlashListener());
            jda.addEventListener(new ModalListener());
            jda.addEventListener(new ButtonListener());
            jda.addEventListener(new StringSelectListener());


            LoggerFactory.getLogger("M.I.M.I").info("Bot desplegado correctamente, escuchando donaciones!");
        } catch (Exception e) {
            LoggerFactory.getLogger("M.I.M.I").error("Ha ocurrido un problema al iniciar el bot: " + e.getMessage());
        }
    }
}