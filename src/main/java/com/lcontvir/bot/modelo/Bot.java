package com.lcontvir.bot.modelo;

import com.lcontvir.bot.modelo.jdbc.ConexionBD;
import com.lcontvir.bot.vista.discord.ReadyListener;
import com.lcontvir.bot.vista.discord.SlashListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.LoggerFactory;

public class Bot {


    public static void main(String[] arguments) {
        try {
            PropsLoader.loadAppProps();

            if (PropsVerificator.VerifySteamConexion() && PropsVerificator.VerifyDatabaseConexion() && ConexionBD.PrepareDatabase() && PropsVerificator.VerifyDiscordConexion()) {
                PropsVerificator.VerifyCooldownHours();
                PropsVerificator.VerifyExpireDays();
                JDA jda = JDABuilder.createLight(PropsLoader.getJDAToken(), GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS).build();
                jda.addEventListener(new SlashListener());
                jda.addEventListener(new ReadyListener());
                LoggerFactory.getLogger("Bot Donaciones").info("Bot desplegado correctamente, escuchando donaciones!");
            }
        } catch (Exception e) {
            LoggerFactory.getLogger("Bot Donaciones").error("Ha ocurrido un problema al iniciar el bot: " + e.getMessage());
        }
    }
}