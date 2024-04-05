package com.lcontvir.bot.modelo.discord;

import com.lcontvir.bot.modelo.PropsLoader;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Donador {

    private int Id;
    private String IdDiscord;
    private String DiscordName;
    private String Steamid64;
    private DonadorRole RoleDonador;
    private Timestamp TimeStamp;

    public Donador(int id, String idDiscord, String discordName, String steamid64, DonadorRole donadorRole, Timestamp timeStamp) {
        Id = id;
        IdDiscord = idDiscord;
        DiscordName = discordName;
        Steamid64 = steamid64;
        RoleDonador = donadorRole;
        TimeStamp = timeStamp;
    }

    public Donador() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getIdDiscord() {
        return IdDiscord;
    }

    public void setIdDiscord(String idDiscord) {
        IdDiscord = idDiscord;
    }

    public String getDiscordName() {
        return DiscordName;
    }

    public void setDiscordName(String discordName) {
        DiscordName = discordName;
    }

    public String getSteamid64() {
        return Steamid64;
    }

    public void setSteamid64(String steamid64) {
        Steamid64 = steamid64;
    }

    public DonadorRole getRoleDonador() {
        return RoleDonador;
    }

    public void setRoleDonador(DonadorRole roleDonador) {
        RoleDonador = roleDonador;
    }

    public Timestamp getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        TimeStamp = timeStamp;
    }


    /**
     * Calcula los días restantes desde la fecha y hora actual hasta la fecha y hora almacenada en el objeto Donador.
     * <p>
     * Este método convierte el atributo TimeStamp a un objeto Instant y obtiene el Instant actual.
     * Luego, calcula la diferencia en días entre estos dos Instantes.
     * <p>
     * Si ocurre alguna excepción durante el cálculo, se captura y se imprime un mensaje de error en la consola.
     * <p>
     * Finalmente, se devuelve el resultado de restar los días calculados a 30.
     *
     * @return {@link Integer} El número de días restantes hasta alcanzar los días configurados en el programa desde la fecha y hora almacenada en TimeStamp.
     * Si ocurre una excepción, se devuelve el número de días restantes asumiendo que no ha pasado ningún día.
     */
    public long calcularDiasRestantes() {
        long diasRestantes = 0;
        try {
            Instant actual = this.getTimeStamp().toInstant();
            Instant db = Instant.now();

            diasRestantes = ChronoUnit.DAYS.between(actual, db);
        } catch (Exception e) {
            LoggerFactory.getLogger("Bot Donaciones - Donador").error("Ha ocurrido un error al calcular los dias restantes de una donacion: " + e.getMessage());
        }
        return PropsLoader.getExpireDays() - diasRestantes;
    }
}