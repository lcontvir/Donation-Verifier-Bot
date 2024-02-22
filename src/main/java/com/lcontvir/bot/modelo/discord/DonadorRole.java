package com.lcontvir.bot.modelo.discord;

public class DonadorRole {

    private String Nombre;
    private String IdDiscordRole;
    private String IdSteamRole;

    public DonadorRole(String nombre, String idDiscordRole, String idSteamRole) {
        Nombre = nombre;
        IdDiscordRole = idDiscordRole;
        IdSteamRole = idSteamRole;
    }

    public DonadorRole() {
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getIdDiscordRole() {
        return IdDiscordRole;
    }

    public void setIdDiscordRole(String idDiscordRole) {
        IdDiscordRole = idDiscordRole;
    }

    public String getIdSteamRole() {
        return IdSteamRole;
    }

    public void setIdSteamRole(String idSteamRole) {
        IdSteamRole = idSteamRole;
    }
}
