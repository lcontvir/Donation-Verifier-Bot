package com.lcontvir.bot.modelo.steam;

import com.google.gson.annotations.SerializedName;
import com.lcontvir.bot.controlador.steam.SteamAPI;

public class SteamJugador {
    @SerializedName("steamid")
    private String steamid;

    @SerializedName("avatarfull")
    private String avatarfull;

    @SerializedName("personaname")
    private String personaname;

    public SteamJugador() {
    }

    public SteamJugador(String steamid, String avatarfull, String personaname) {
        this.steamid = steamid;
        this.avatarfull = avatarfull;
        this.personaname = personaname;
    }

    public static SteamJugador GetSteamJugador(String url) {
        return SteamAPI.GetSteamJugador(url);
    }

    public String getSteamid() {
        return steamid;
    }

    public String getAvatarfull() {
        return avatarfull;
    }

    public String getPersonaname() {
        return personaname;
    }
}
