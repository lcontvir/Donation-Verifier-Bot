package com.lcontvir.bot.controlador.steam;

import com.google.gson.Gson;
import com.lcontvir.bot.modelo.steam.SteamJugador;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SteamAPI {

    public static String ApiSteamKey = "";

    /**
     * Comprueba si la cadena proporcionada es un SteamID64 válido.
     *
     * @param cadena La cadena que se va a comprobar.
     * @return true si la cadena es un SteamID64 válido, false en caso contrario.
     */
    public static boolean esSteamID64(String cadena) {
        boolean esSteamID64 = false;
        String patronSteamID64;
        Pattern pattern;
        Matcher matcher;
        try {
            patronSteamID64 = "^7656\\d{13}$";
            pattern = Pattern.compile(patronSteamID64);
            matcher = pattern.matcher(cadena);
            esSteamID64 = matcher.matches();
        } catch (Exception e) {
            LoggerFactory.getLogger("Bot Donaciones - SteamAPI").error("Ha ocurrido un error al comprobar si una cadena era una SteamId64: " + e.getMessage());
        }
        return esSteamID64;
    }


    /**
     * Este método intenta encontrar un SteamID64 a partir de la entrada proporcionada.
     * Primero, intenta encontrar el SteamID64 directamente en la entrada.
     * Si no puede encontrarlo, intenta encontrar el SteamID64 a través del nombre de vanidad en la entrada.
     *
     * @param input La cadena de entrada de la que se intentará extraer el SteamID64.
     * @return El SteamID64 encontrado, o una cadena vacía si no se encuentra ninguno.
     */

    private static String EncontrarSteamId64(String input) {
        String Id64 = "";
        try {
            Id64 = EncontrarSteamID64ById(input);
            if (Id64.isEmpty()) {
                Id64 = EncontrarSteamId64ByVanityName(input);
            }
        } catch (Exception e) {
            LoggerFactory.getLogger("Bot Donaciones - SteamAPI").error("Ha ocurrido un error al encontrar una SteamID64: " + e.getMessage());
        }
        return Id64;
    }

    /**
     * Este método se utiliza para obtener un objeto {@link SteamJugador} a partir de una cadena de entrada proporcionada.
     * Primero, intenta encontrar un SteamID64 en la cadena de entrada. Si lo encuentra, obtiene el {@link SteamJugador} correspondiente a ese SteamID64.
     * Si no encuentra un SteamID64, asume que la cadena de entrada es un nombre de vanidad y trata de obtener el {@link SteamJugador} correspondiente a ese nombre de vanidad.
     *
     * @param opcion La cadena de entrada que puede ser un SteamID64 o un nombre de vanidad.
     * @return Un objeto {@link SteamJugador} si se encuentra un SteamID64 o un nombre de vanidad válido en la cadena de entrada, null en caso contrario.
     */

    public static SteamJugador GetSteamJugador(String opcion) {
        SteamJugador jugador = null;
        try {
            opcion = SteamAPI.EncontrarSteamId64(opcion);
            if (!opcion.isEmpty()) {
                if (esSteamID64(opcion)) {
                    jugador = GetSteamJugadorFromSteamId64(opcion);
                } else {
                    jugador = GetSteamJugadorFromVanity(opcion);
                }
            }
        } catch (Exception e) {
            LoggerFactory.getLogger("Bot Donaciones - SteamAPI").error("Ha ocurrido un error al obtener un jugador de steam: " + e.getMessage());
        }
        return jugador;
    }

    /**
     * Este método busca un SteamID64 en la cadena de entrada proporcionada.
     * Primero, busca la secuencia "7656" en la cadena de entrada. Si la encuentra, continúa buscando dígitos después de esta secuencia hasta que ya no encuentra más dígitos.
     * Finalmente, devuelve la secuencia "7656" seguida de los dígitos encontrados, que debería ser un SteamID64 válido.
     *
     * @param input La cadena de entrada en la que se buscará el SteamID64.
     * @return Un SteamID64 si se encuentra uno en la cadena de entrada, o una cadena vacía si no se encuentra ninguno.
     */

    private static String EncontrarSteamID64ById(String input) {
        int index7656;
        String numerosEncontrados = "";
        try {
            index7656 = input.indexOf("7656");

            if (index7656 != -1) {
                int startIndex = index7656 + 4;
                int endIndex = startIndex;

                while (endIndex < input.length() && Character.isDigit(input.charAt(endIndex))) {
                    endIndex++;
                }
                numerosEncontrados = "7656" + input.substring(startIndex, endIndex);
            }
        } catch (Exception e) {
            LoggerFactory.getLogger("Bot Donaciones - SteamAPI").error("Ha ocurrido un error al encontrar una SteamID64 que coincida con la dada: " + e.getMessage());
        }
        return numerosEncontrados;
    }

    /**
     * Este método intenta encontrar un SteamID64 a partir de un nombre de vanidad (Vanity Name) en la URL de entrada.
     * Utiliza una expresión regular para buscar en la URL de Steam y extraer el nombre de vanidad.
     *
     * @param input La URL de Steam que contiene el nombre de vanidad.
     * @return El nombre de vanidad encontrado, o una cadena vacía si no se encuentra ninguno.
     */

    private static String EncontrarSteamId64ByVanityName(String input) {
        Pattern pattern = Pattern.compile("://steamcommunity.com/id/(\\w+)");
        Matcher matcher = pattern.matcher(input);
        String find = "";

        try {
            if (matcher.find()) {
                find = matcher.group(1);
            }
        } catch (Exception e) {
            LoggerFactory.getLogger("Bot Donaciones - SteamAPI").error("Ha ocurrido un error al encontrar una SteamID64 mediante un VanityName: " + e.getMessage());
        }
        return find;
    }

    /**
     * Este método intenta obtener un objeto {@link SteamJugador} a partir de un SteamID64.
     * Realiza una petición HTTP GET a la API de Steam para obtener los detalles del jugador correspondiente al SteamID64 proporcionado.
     * Si la petición es exitosa, se obtiene la respuesta en formato JSON, se deserializa en un objeto {@link SteamJugadorID64Respuesta} y se extraen los detalles del jugador.
     *
     * @param SteamId64 El SteamID64 del jugador de Steam.
     * @return Un objeto {@link SteamJugador} si se encuentra un jugador correspondiente al SteamID64, null en caso contrario.
     */


    private static SteamJugador GetSteamJugadorFromSteamId64(String SteamId64) {
        String apiUrl = "https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=" + ApiSteamKey + "&steamids=" + SteamId64;
        SteamJugador jugadorEncontrado = null;
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(apiUrl);
        HttpResponse response;
        HttpEntity entity;
        String jsonResponse;
        Gson gson;
        SteamJugadorID64Respuesta playerSummary;

        try {
            response = httpClient.execute(httpGet);
            entity = response.getEntity();

            if (entity != null) {
                jsonResponse = EntityUtils.toString(entity);

                gson = new Gson();
                playerSummary = gson.fromJson(jsonResponse, SteamJugadorID64Respuesta.class);

                if (playerSummary.getResponse().getPlayers().length > 0) {
                    jugadorEncontrado = new SteamJugador(SteamId64, playerSummary.getResponse().getPlayers()[0].getAvatarfull(), playerSummary.getResponse().getPlayers()[0].getPersonaname());
                }
            }
        } catch (Exception e) {
            LoggerFactory.getLogger("Bot Donaciones - SteamAPI").error("Ha ocurrido un error al hacer la peticion al servicio de steam mediante una Id64: " + e.getMessage());
        }
        return jugadorEncontrado;
    }

    /**
     * Este método intenta obtener un objeto {@link SteamJugador} a partir de un nombre de vanidad (Vanity Name).
     * Realiza una petición HTTP GET a la API de Steam para resolver la URL de vanidad en un SteamID64.
     * Si la petición es exitosa, se obtiene el SteamID64 y se utiliza para obtener el objeto {@link SteamJugador} correspondiente.
     *
     * @param Vanity El nombre de vanidad (Vanity Name) del jugador de Steam.
     * @return Un objeto {@link SteamJugador} si se encuentra un SteamID64 correspondiente al nombre de vanidad, null en caso contrario.
     */

    private static SteamJugador GetSteamJugadorFromVanity(String Vanity) {
        String apiUrl = "https://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/?key=" + ApiSteamKey + "&vanityurl=" + Vanity;
        SteamJugador jugadorEncontrado = null;
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(apiUrl);
        HttpResponse response;
        HttpEntity entity;
        String jsonResponse;
        Gson gson;
        SteamJugadorVanityRespuesta playerSummary;

        try {
            response = httpClient.execute(httpGet);
            entity = response.getEntity();

            if (entity != null) {
                jsonResponse = EntityUtils.toString(entity);
                gson = new Gson();
                playerSummary = gson.fromJson(jsonResponse, SteamJugadorVanityRespuesta.class);
                jugadorEncontrado = GetSteamJugadorFromSteamId64(playerSummary.getResponse().getSteamid());
            }
        } catch (Exception e) {
            LoggerFactory.getLogger("Bot Donaciones - SteamAPI").error("Ha ocurrido un error al hacer la peticion al servicio de steam mediante un nombre personalizado o Vanity Name: " + e.getMessage());
        }
        return jugadorEncontrado;
    }
}
