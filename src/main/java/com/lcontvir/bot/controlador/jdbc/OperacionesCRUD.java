package com.lcontvir.bot.controlador.jdbc;

import com.lcontvir.bot.modelo.discord.Donador;
import com.lcontvir.bot.modelo.discord.DonadorRole;
import com.lcontvir.bot.modelo.jdbc.ConexionBD;
import com.lcontvir.bot.modelo.steam.SteamJugador;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class OperacionesCRUD {

    /**
     * Este método se utiliza para obtener un objeto Donador a partir de un miembro de Discord mediante una consulta a la base de datos.
     *
     * @param miembro El miembro de Discord del que se desea obtener la información del donador.
     * @return Un objeto {@link Donador} que contiene la información del donador obtenida de la base de datos. Si no se encuentra ningún donador correspondiente al miembro proporcionado, se devuelve un objeto Donador vacío.
     */
    public static Donador obtenerDonador(Member miembro) {
        Donador donador = new Donador();
        String sql = "SELECT u.*, dr.IdDiscordRole, dr.IdSteamRole FROM UsuarioDiscordView u, Usuario ui, DonadorRoleView dr WHERE u.IdDiscord=? AND dr.Id = ui.RoleDonador";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, miembro.getId());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                donador.setId(rs.getInt("Id"));
                donador.setRoleDonador(new DonadorRole(rs.getString("RolDonador"), rs.getString("IdDiscordRole"), rs.getString("IdSteamRole")));
                donador.setIdDiscord(rs.getString("IdDiscord"));
                donador.setDiscordName(rs.getString("DiscordName"));
                donador.setSteamid64(rs.getString("SteamId64"));
                donador.setTimeStamp(rs.getTimestamp("TimeStamp"));
            }

        } catch (SQLException e) {
            LoggerFactory.getLogger("Bot Donaciones - Operaciones CRUD").error("Error al consultar un donador de la base de datos: " + e.getMessage());
        } catch (RuntimeException ex) {
            LoggerFactory.getLogger("Bot Donaciones - Operaciones CRUD").error("Ha ocurrido un error al contactar un donador de la base de datos: " + ex.getMessage());
        }
        return donador;
    }

    /**
     * Actualiza la información de un donador en la base de datos.
     *
     * @param steamJugador  El jugador de Steam que ha hecho la donación.
     * @param discordMember El miembro de Discord que ha hecho la donación.
     * @param idDiscordRole El ID del rol de Discord del donador.
     * @return `true` si la actualización se completo sin errores, `false` en caso contrario.
     */
    public static boolean RegistrarDonacionBBDD(SteamJugador steamJugador, Member discordMember, String idDiscordRole) {
        boolean registrado = false;
        int idDonador = DiscordJdbcExtensions.obtenerIdDonadorRole(idDiscordRole);
        if (idDonador != -1) {
            String sql = "INSERT INTO Usuario (IdDiscord, DiscordName, SteamId64, RoleDonador, TimeStamp) VALUES (?, ?, ?, ?, ?)";

            try (Connection conexion = ConexionBD.obtenerConexion();
                 PreparedStatement statement = conexion.prepareStatement(sql)) {

                statement.setString(1, discordMember.getId());
                statement.setString(2, discordMember.getEffectiveName());
                statement.setString(3, steamJugador.getSteamid());
                statement.setInt(4, idDonador);
                statement.setDate(5, new Date(System.currentTimeMillis()));

                statement.executeUpdate();
                registrado = true;

            } catch (SQLException e) {
                LoggerFactory.getLogger("Bot Donaciones - Operaciones CRUD").error("Error al insertar un donador de la base de datos: " + e.getMessage());
            } catch (RuntimeException ex) {
                LoggerFactory.getLogger("Bot Donaciones - Operaciones CRUD").error("Ha ocurrido un error al contactar la base de datos: " + ex.getMessage());
            }
        }
        return registrado;
    }

    /**
     * Actualiza la información de un donador en la base de datos.
     *
     * @param steamJugador  El jugador de Steam que ha hecho la donación.
     * @param discordMember El miembro de Discord que ha hecho la donación.
     * @param idDiscordRole El ID del rol de Discord del donador.
     * @return `true` si la actualización se completo sin errores, `false` en caso contrario.
     */
    public static boolean ActualizarDonacionBBDD(SteamJugador steamJugador, Member discordMember, String idDiscordRole) {
        boolean actualizado = false;
        int idDonador = -1;
        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());

        try{
            Role rolActual = discordMember.getJDA().getRoleById(idDiscordRole);
            Role rolAntiguo = discordMember.getJDA().getRoleById(obtenerDonador(discordMember).getRoleDonador().getIdDiscordRole());
            idDonador = DiscordJdbcExtensions.obtenerIdDonadorRole(idDiscordRole);

            if (rolAntiguo.getPosition() > rolActual.getPosition()) {
                idDonador = DiscordJdbcExtensions.obtenerIdDonadorRole(obtenerDonador(discordMember).getRoleDonador().getIdDiscordRole());
                timeStamp = obtenerDonador(discordMember).getTimeStamp();
            }
        }catch (NullPointerException e){
            LoggerFactory.getLogger("Bot Donaciones - Operaciones CRUD").error("Error al actualizar un donador de la base de datos, uno de los roles de la base de datos no es correcto: " + e.getMessage());
        }

        if (idDonador != -1) {
            String sql = "UPDATE Usuario SET DiscordName = ?, SteamId64 = ?, RoleDonador = ?, TimeStamp = ? WHERE IdDiscord =" + discordMember.getId();

            try (Connection conexion = ConexionBD.obtenerConexion();
                 PreparedStatement statement = conexion.prepareStatement(sql)) {

                statement.setString(1, discordMember.getEffectiveName());
                statement.setString(2, steamJugador.getSteamid());
                statement.setInt(3, idDonador);
                statement.setTimestamp(4, timeStamp);

                statement.executeUpdate();
                actualizado = true;

            } catch (SQLException e) {
                LoggerFactory.getLogger("Bot Donaciones - Operaciones CRUD").error("Error al actualizar un donador de la base de datos: " + e.getMessage());
            } catch (RuntimeException ex) {
                LoggerFactory.getLogger("Bot Donaciones - Operaciones CRUD").error("Ha ocurrido un error al contactar la base de datos: " + ex.getMessage());
            }
        }
        return actualizado;
    }
}
