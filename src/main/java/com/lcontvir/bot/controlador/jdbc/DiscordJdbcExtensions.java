package com.lcontvir.bot.controlador.jdbc;

import com.lcontvir.bot.modelo.jdbc.ConexionBD;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DiscordJdbcExtensions {

    /**
     * Este método se utiliza para obtener el rol de donación que coincide con un miembro desde la base de datos.
     *
     * @param member El miembro de Discord para el que se va a buscar el rol de donación.
     * @return {@link Role} - Retorna el rol de donación que coincide con el miembro si se encuentra en la base de datos, de lo contrario retorna `null`.
     *
     * <p>
     * Ejemplo de uso:
     * <pre>
     * {@code
     * Role roleDonacion = DiscordJdbcExtensions.GetMatchingDonationRoleFromDB(miembro);
     * }
     * </pre>
     * </p>
     */
    public static Role GetMatchingDonationRoleFromDB(Member member) {
        String sql = "SELECT r.IdDiscordRole FROM DonadorRoleView r";
        ArrayList<Role> Roles = new ArrayList<>();
        Role RoleSeleccionado = null;

        try (Connection conexion = ConexionBD.obtenerConexion(); PreparedStatement statement = conexion.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Roles.add(member.getJDA().getRoleById(resultSet.getString("IdDiscordRole")));
            }

            for (Role rol : Roles) {
                if (member.getRoles().contains(rol)) {
                    if (RoleSeleccionado != null) {
                        if (RoleSeleccionado.getPosition() < rol.getPosition()) {
                            RoleSeleccionado = rol;
                        }
                    } else {
                        RoleSeleccionado = rol;
                    }
                }
            }

        } catch (SQLException e) {
            LoggerFactory.getLogger("Bot Donaciones - Discord JDBC Extensions").error("Error al consultar datos para obtener un rol que correspondiente a los roles del miembro: " + e.getMessage());
        } catch (RuntimeException ex) {
            LoggerFactory.getLogger("Bot Donaciones - Discord JDBC Extensions").error("Ha ocurrido un error al contactar con la base de datos: " + ex.getMessage());
        }

        return RoleSeleccionado;
    }

    /**
     * Este método se utiliza para verificar si un miembro ha hecho una donación.
     *
     * @param miembro El miembro de Discord que se va a verificar en la base de datos.
     * @return {@link Boolean} - Retorna `true` si el miembro ha hecho una donación, de lo contrario retorna `false`.
     *
     * <p>
     * Ejemplo de uso:
     * <pre>
     * {@code
     * boolean haDonado = DiscordJdbcExtensions.haDonado(miembro);
     * }
     * </pre>
     * </p>
     */

    public static boolean haDonado(Member miembro) {
        String sql = "SELECT * FROM UsuarioDiscordView WHERE IdDiscord = ?";
        boolean existDato = false;

        try (Connection conexion = ConexionBD.obtenerConexion(); PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, miembro.getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                existDato = true;
            }

        } catch (SQLException e) {
            LoggerFactory.getLogger("Bot Donaciones - Discord JDBC Extensions").error("Error al consultar datos para saber si ha donado: " + e.getMessage());
        } catch (RuntimeException ex) {
            LoggerFactory.getLogger("Bot Donaciones - Discord JDBC Extensions").error("Ha ocurrido un error al contactar con la base de datos: " + ex.getMessage());
        }
        return existDato;
    }

    /**
     * Este método se utiliza para obtener el ID de un rol de donador en la base de datos.
     *
     * @param idDiscordRole El ID del rol de Discord que se va a buscar en la base de datos.
     * @return {@link Integer} - Retorna el ID del rol de donador si se encuentra en la base de datos, de lo contrario retorna -1.
     *
     *
     * <p>
     * Ejemplo de uso:
     * <pre>
     * {@code
     * int idDonadorRole = DiscordJdbcExtensions.obtenerIdDonadorRole("1234567890");
     * }
     * </pre>
     * </p>
     */
    public static int obtenerIdDonadorRole(String idDiscordRole) {
        int idDonadorRole = -1;
        String sql = "SELECT Id FROM DonadorRoleView WHERE IdDiscordRole = ?";

        try (Connection connection = ConexionBD.obtenerConexion(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, idDiscordRole);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    idDonadorRole = resultSet.getInt("Id");
                }
            }

        } catch (SQLException e) {
            LoggerFactory.getLogger("Bot Donaciones - Discord JDBC Extensions").error("Error al consultar datos para saber el Id de donador: " + e.getMessage());
        } catch (RuntimeException ex) {
            LoggerFactory.getLogger("Bot Donaciones - Discord JDBC Extensions").error("Ha ocurrido un error al contactar con la base de datos: " + ex.getMessage());
        }

        return idDonadorRole;
    }
}
