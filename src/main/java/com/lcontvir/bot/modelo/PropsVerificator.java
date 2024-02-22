package com.lcontvir.bot.modelo;

import com.lcontvir.bot.controlador.steam.SteamAPI;
import com.lcontvir.bot.modelo.jdbc.ConexionBD;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.LoggerFactory;

public class PropsVerificator {

    /**
     * Verifica la conexión con Discord utilizando el token de Discord proporcionado en la configuración.
     *
     * <p>Este método intenta construir una instancia de {@link JDA} utilizando el token de Discord obtenido de {@link PropsLoader#getJDAToken()}.
     * Si el token es nulo o vacío, se informa al usuario para que lo especifique en la configuración.
     * Si el token es inválido, se informa al usuario y se cierra la instancia de JDA si se ha creado alguna.</p>
     *
     * <p>Finalmente, se informa al usuario si la verificación fue exitosa o no.</p>
     *
     * @return {@code true} si la verificación fue exitosa, {@code false} en caso contrario.
     * @see JDA
     * @see JDABuilder
     * @see PropsLoader
     * @see Bot
     */
    public static boolean VerifyDiscordConexion() {
        JDA api = null;
        boolean verificado = false;
        LoggerFactory.getLogger("Bot Donaciones - Props Verificator").info(" - [Fase Discord]: Comenzando Verificacion");
        LoggerFactory.getLogger("Bot Donaciones - Props Verificator").info(" - [Fase Discord]: Comprobando Conexion...");

        try {
            if (PropsLoader.getJDAToken() == null || PropsLoader.getJDAToken().isEmpty()) {
                LoggerFactory.getLogger("Bot Donaciones - Props Verificator").error(" - [Fase Discord]: No se ha encontrado un Token de Discord, por favor, especificalo en la configuracion");
            } else {
                try {
                    api = JDABuilder.createLight(PropsLoader.getJDAToken(), GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS).build();
                    LoggerFactory.getLogger("Bot Donaciones - Props Verificator").info(" - [Fase Discord]: Token de Discord validado!");
                    verificado = true;
                } catch (Exception e) {
                    if (api != null) {
                        api.shutdownNow();
                    }
                    LoggerFactory.getLogger("Bot Donaciones - Props Verificator").error(" - [Fase Discord]: El Token de Discord especificado es invalido, por favor, revise la configuracion");
                }
            }
        } catch (Exception e) {
            LoggerFactory.getLogger("Bot Donaciones - Props Verificator").error("[Fase Discord]: No se ha podido verificar la conexion de discord: " + e.getMessage());
        }
        if (verificado) {
            LoggerFactory.getLogger("Bot Donaciones - Props Verificator").info(" - [Fase Discord]: Verificacion Correcta!");
        } else {
            LoggerFactory.getLogger("Bot Donaciones - Props Verificator").warn(" - [Fase Discord]: Verificacion No Completada Correctamente");
        }

        LoggerFactory.getLogger("Bot Donaciones - Props Verificator").info(" - [Fase Discord]: Finalizada Verificacion");
        return verificado;
    }

    /**
     * Verifica la conexión con la API de Steam.
     *
     * <p>Este método realiza una serie de comprobaciones para asegurar que la conexión con la API de Steam es válida.
     * Primero, verifica si el token de la API de Steam está presente y no está vacío. Si el token está presente,
     * intenta obtener un jugador de Steam usando el método {@link SteamAPI#GetSteamJugador(String)} con una URL de perfil de Steam fija.
     * Si el método puede obtener un jugador de Steam sin lanzar una excepción, se considera que el token es válido y la conexión con la API de Steam es correcta.</p>
     *
     * <p>Si en cualquier punto del proceso se encuentra un problema (por ejemplo, el token de la API de Steam no está presente o es inválido),
     * el método informará del problema en la consola y devolverá {@code false}.</p>
     *
     * @return {@code true} si la conexión con la API de Steam es válida, {@code false} en caso contrario.
     * @see SteamAPI
     * @see PropsLoader#getSteamAPIToken()
     */
    public static boolean VerifySteamConexion() {
        boolean verificado = false;
        LoggerFactory.getLogger("Bot Donaciones - Props Verificator").info(" - [Fase Steam]: Comenzando Verificacion");
        LoggerFactory.getLogger("Bot Donaciones - Props Verificator").info(" - [Fase Steam]: Comprobando Conexion...");

        if (PropsLoader.getSteamAPIToken() == null | PropsLoader.getSteamAPIToken().isEmpty()) {
            LoggerFactory.getLogger("Bot Donaciones - Props Verificator").error(" - [Fase Steam]: No se ha encontrado un Token de Steam, por favor, especificalo en la configuracion");
        } else {
            SteamAPI.ApiSteamKey = PropsLoader.getSteamAPIToken();

            try {
                LoggerFactory.getLogger("Bot Donaciones - Props Verificator").info(" - [Fase Steam]: Token de Steam validado!");
                verificado = true;
            } catch (Exception e) {
                LoggerFactory.getLogger("Bot Donaciones - Props Verificator").error(" - [Fase Steam]: El Token de Steam especificado es invalido, por favor, revise la configuracion");
            }
        }
        if (verificado) {
            LoggerFactory.getLogger("Bot Donaciones - Props Verificator").info(" - [Fase Steam]: Verificacion Correcta!");
        } else {
            LoggerFactory.getLogger("Bot Donaciones - Props Verificator").warn(" - [Fase Steam]: Verificacion No Completada Correctamente");
        }

        LoggerFactory.getLogger("Bot Donaciones - Props Verificator").info(" - [Fase Steam]: Finalizada Verificacion");
        return verificado;
    }

    /**
     * Verifica la conexión a la base de datos.
     *
     * <p>Este método realiza una serie de comprobaciones para asegurarse de que la conexión a la base de datos se puede establecer correctamente.
     * Primero, verifica que los detalles de la conexión (URL, usuario y contraseña) no estén vacíos. Luego, intenta establecer una conexión a la base de datos.
     * Si la conexión es exitosa, el método devuelve true. Si la conexión falla por cualquier motivo, el método devuelve false.</p>
     *
     * @return {@code true} si la conexión a la base de datos se verifica correctamente, {@code false} en caso contrario.
     * @see PropsLoader#getBBDDConexion()
     * @see PropsLoader#getBBDDUser()
     * @see PropsLoader#getBBDDPassw()
     * @see ConexionBD#PrepareConnection(String, String, String)
     * @see ConexionBD#obtenerConexion()
     */
    public static boolean VerifyDatabaseConexion() {

        boolean verificado = false;
        LoggerFactory.getLogger("Bot Donaciones - Props Verificator").info(" - [Fase Base de Datos]: Comenzando Verificacion");
        LoggerFactory.getLogger("Bot Donaciones - Props Verificator").info(" - [Fase Base de Datos]: Comprobando Conexion...");

        if (PropsLoader.getBBDDConexion() == null || PropsLoader.getBBDDConexion().isEmpty()) {
            LoggerFactory.getLogger("Bot Donaciones - Props Verificator").error(" - [Fase Base de Datos]: No se ha encontrado una URL a la Base de Datos, por favor, especificalo en la configuracion");
        }
        if (PropsLoader.getBBDDUser() == null || PropsLoader.getBBDDUser().isEmpty()) {
            LoggerFactory.getLogger("Bot Donaciones - Props Verificator").error(" - [Fase Base de Datos]: No se ha encontrado un Usuario para la Base de Datos, por favor, especificalo en la configuracion");
        }
        if (PropsLoader.getBBDDPassw() == null || PropsLoader.getBBDDPassw().isEmpty()) {
            LoggerFactory.getLogger("Bot Donaciones - Props Verificator").error(" - [Fase Base de Datos]: No se ha encontrado una Password la Base de Datos, por favor, especificalo en la configuracion");
        }

        if (!PropsLoader.getBBDDConexion().isEmpty() && !PropsLoader.getBBDDUser().isEmpty() && !PropsLoader.getBBDDPassw().isEmpty()) {
            try {
                ConexionBD.PrepareConnection(PropsLoader.getBBDDConexion(), PropsLoader.getBBDDUser(), PropsLoader.getBBDDPassw());
                ConexionBD.obtenerConexion();
                LoggerFactory.getLogger("Bot Donaciones - Props Verificator").info(" - [Fase Base de Datos]: Conexion a la BBDD validada!");
                verificado = true;
            } catch (Exception e) {
                LoggerFactory.getLogger("Bot Donaciones - Props Verificator").error(" - [Fase Base de Datos]: La conexion a la BBDD especificada es invalida, por favor, revise la configuracion");
            }
        }

        if (verificado) {
            LoggerFactory.getLogger("Bot Donaciones - Props Verificator").info(" - [Fase Base de Datos]: Verificacion Correcta!");
        } else {
            LoggerFactory.getLogger("Bot Donaciones - Props Verificator").error(" - [Fase Base de Datos]: Verificacion No Completada Correctamente");
        }

        LoggerFactory.getLogger("Bot Donaciones - Props Verificator").info(" - [Fase Base de Datos]: Finalizada Verificacion");
        return verificado;
    }

    /**
     * Verifica el valor de cooldown de los comandos. Si el valor es -1, se establece un valor por defecto de 3 horas.
     *
     * @return {@code true} si el valor de cooldown es válido o se ha establecido un valor por defecto, {@code false} en caso contrario.
     *
     * @see PropsLoader#getCoolDownModificacion() Para obtener el valor actual de cooldown.
     * @see PropsLoader#setCooldownModificacion(int) Para establecer un nuevo valor de cooldown.
     */

    public static void VerifyCooldownHours() {
        System.out.println("-------------------------");
        LoggerFactory.getLogger("Bot Donaciones - Props Verificator").info(" - [Fase Comando Cooldown]: Comenzando Verificacion");
        LoggerFactory.getLogger("Bot Donaciones - Props Verificator").info(" - [Fase Comando Cooldown]: Comprobando Timing");
        if(PropsLoader.getCoolDownModificacion() == -1){
            LoggerFactory.getLogger("Bot Donaciones - Props Verificator").warn(" - [Fase Comando Cooldown]: Configure su cooldown, se usara 3h como valor default");
            PropsLoader.setCooldownModificacion(3);
        }

        LoggerFactory.getLogger("Bot Donaciones - Props Verificator").info(" - [Fase Comando Cooldown]: Finalizada Verificacion");
    }

}
