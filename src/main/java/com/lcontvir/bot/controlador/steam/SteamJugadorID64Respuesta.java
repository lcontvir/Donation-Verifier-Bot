package com.lcontvir.bot.controlador.steam;

import com.google.gson.annotations.SerializedName;
import com.lcontvir.bot.modelo.steam.SteamJugador;

public class SteamJugadorID64Respuesta {

    @SerializedName("response")
    private PlayerResponseID64 response;

    public PlayerResponseID64 getResponse() {
        return response;
    }

    public void setResponse(PlayerResponseID64 response) {
        this.response = response;
    }

    public static class PlayerResponseID64 {
        @SerializedName("players")
        private SteamJugador[] players;

        public SteamJugador[] getPlayers() {
            return players;
        }

        public void setPlayers(SteamJugador[] players) {
            this.players = players;
        }
    }


}
