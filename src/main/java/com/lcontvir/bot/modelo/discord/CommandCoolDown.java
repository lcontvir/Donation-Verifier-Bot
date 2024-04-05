package com.lcontvir.bot.modelo.discord;

import com.lcontvir.bot.modelo.PropsLoader;
import net.dv8tion.jda.api.entities.Member;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

public class CommandCoolDown {

    private static final HashMap<String, Instant> UsuariosEnCooldDown = new HashMap<>();

    /**
     * Comprueba si un miembro está en período de enfriamiento.
     * <p>
     * Este método verifica si el miembro proporcionado está en el HashMap `UsuariosEnCooldDown`. Si el miembro está presente,
     * entonces comprueba si ha pasado menos de una hora determinada desde que se añadió. Si es así, el método devuelve true, indicando que el
     * miembro está en período de enfriamiento. Si ha pasado más de una hora determinada, el miembro se elimina del HashMap y el método devuelve false.
     * @param member El miembro a comprobar.
     * @return {@link Boolean} Devuelve true si el miembro está en período de enfriamiento, false en caso contrario.
     */
    public static boolean IsOnCooldown(Member member) {

        boolean cd = false;

        try {
            if (UsuariosEnCooldDown.containsKey(member.getId())) {

                if (ChronoUnit.HOURS.between(UsuariosEnCooldDown.get(member.getId()), Instant.now()) < PropsLoader.getCoolDownModificacion()) {
                    cd = true;
                } else {
                    UsuariosEnCooldDown.remove(member.getId());
                }
            }
        } catch (Exception e) {
            LoggerFactory.getLogger("Bot Donaciones - Command Cooldown").error("Ha ocurrido un error al compprobar el cooldown de un usuario: " + e.getMessage());
        }

        return cd;
    }

    /**
     * Añade un miembro al período de enfriamiento.
     * <p>
     * Este método verifica si el miembro proporcionado ya está en el HashMap UsuariosEn CooldDown. Si el miembro no está presente,
     * entonces se añade al HashMap con la hora actual como valor. Si ocurre alguna excepción durante el proceso, se imprime el mensaje de error.
     *
     * @param member El miembro a añadir al período de enfriamiento.
     */
    public static void AddCooldown(Member member) {
        try {
            if (!UsuariosEnCooldDown.containsKey(member.getId())) {
                UsuariosEnCooldDown.put(member.getId(), Instant.now());
            }
        } catch (Exception e) {
            LoggerFactory.getLogger("Bot Donaciones - Command Cooldown").error("Ha ocurrido un error al añadir el cooldown de un usuario: " + e.getMessage());
        }
    }
}
