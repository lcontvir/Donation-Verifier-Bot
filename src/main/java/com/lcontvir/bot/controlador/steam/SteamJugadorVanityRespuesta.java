package com.lcontvir.bot.controlador.steam;

import com.google.gson.annotations.SerializedName;

public class SteamJugadorVanityRespuesta {

    @SerializedName("response")
    private PlayerResponseVanity response;

    public PlayerResponseVanity getResponse() {
        return response;
    }

    public void setResponse(PlayerResponseVanity response) {
        this.response = response;
    }

    public static class PlayerResponseVanity {
        @SerializedName("steamid")
        private String steamid;

        public String getSteamid() {
            return steamid;
        }

        public void setSteamid(String steamid) {
            this.steamid = steamid;
        }
    }
}
