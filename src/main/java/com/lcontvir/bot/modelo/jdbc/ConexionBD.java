package com.lcontvir.bot.modelo.jdbc;

import com.lcontvir.bot.modelo.PropsLoader;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {
    private static String URL = "";
    private static String USUARIO = "";
    private static String PASS = "";

    /**
     * Este método se utiliza para obtener una conexión a la base de datos.
     *
     * @return {@link Connection} - Retorna una conexión a la base de datos.
     * @throws RuntimeException si ocurre alguna SQLException durante la conexión a la base de datos o si no se encuentra el driver de la base de datos.
     *
     *                          <p>
     *                          Ejemplo de uso:
     *                          <pre>
     *                          {@code
     *                          Connection conn = ConexionBD.obtenerConexion();
     *                          }
     *                          </pre>
     *                          </p>
     */
    public static Connection obtenerConexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USUARIO, PASS);
        } catch (SQLException e) {
            LoggerFactory.getLogger("Bot Donaciones  Conexion DB").error("Ha ocurrido un error al conectarse a la base de datos: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            LoggerFactory.getLogger("Bot Donaciones  Props Loader").error("No se ha encontrado un driver para la base de datos: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void PrepareConnection(String URLBaseDeDatos, String USUARIOBaseDeDatos, String PASSBaseDeDatos) {
        URL = URLBaseDeDatos;
        USUARIO = USUARIOBaseDeDatos;
        PASS = PASSBaseDeDatos;
    }

    /**
     * Este método se utiliza para preparar la base de datos. Lee un archivo SQL y ejecuta las sentencias que contiene.
     * <p>
     * El método realiza las siguientes operaciones:
     * 1. Obtiene la ruta del archivo `format.sql` en el directorio actual del usuario.
     * 2. Verifica si el archivo existe.
     * 3. Si el archivo existe, lo lee línea por línea.
     * 4. Si la línea contiene una sentencia `CREATE` o `DROP`, ejecuta la sentencia en la base de datos.
     * 5. Si la linea contiene la palabra %EXPIRE_DAYS%, se remplazara por el tiempo de expiracion configurado
     * 6. Si ocurre una excepción durante la lectura del archivo o la ejecución de la sentencia, se imprime un mensaje de error y se retorna `false`.
     * </p>
     *
     * @return {@link Boolean} - Retorna `true` si la preparación de la base de datos se completó correctamente, `false` en caso contrario.
     *
     *
     * <p>
     * Ejemplo de uso:
     * <pre>
     * {@code
     * boolean preparacionExitosa = ConexionBD.PrepareDatabase();
     * }
     * </pre>
     * </p>
     * @see ConexionBD#EjecutarSentencia(String)
     * @see java.nio.file.Files#exists(java.nio.file.Path, java.nio.file.LinkOption...)
     * @see java.io.BufferedReader#readLine()
     */
    public static boolean PrepareDatabase() {
        LoggerFactory.getLogger("Bot Donaciones  Conexion DB").info(" - [Preparacion Base de Datos]: Comenzando preparativos");

        boolean respuesta = false;
        String directorioActual = System.getProperty("user.dir");
        String separador = File.separator;
        String nombreArchivoConfiguracion = "format.sql";
        String rutaArchivoConfiguracion = directorioActual + separador + nombreArchivoConfiguracion;

        LoggerFactory.getLogger("Bot Donaciones  Conexion DB").info(" - [Preparacion Base de Datos]: Preparativos completados");
        LoggerFactory.getLogger("Bot Donaciones  Conexion DB").info(" - [Preparacion Base de Datos]: Leyendo archivo de uso de Base de Datos");


        if (Files.exists(Paths.get(rutaArchivoConfiguracion))) {
            try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivoConfiguracion))) {
                StringBuilder sb = new StringBuilder();
                String linea;

                while ((linea = br.readLine()) != null) {
                    if (linea.contains("CREATE") || linea.contains("DROP")) {
                        if (sb.length() > 0) {
                            if(linea.contains("%EXPIRE_DAYS%")){
                                linea = linea.replace("%EXPIRE_DAYS%", String.valueOf(PropsLoader.getExpireDays()));
                            }
                            LoggerFactory.getLogger("Bot Donaciones  Conexion DB").info(" - [Preparacion Base de Datos]: Preparando Insercion");
                            EjecutarSentencia(sb.toString());
                            LoggerFactory.getLogger("Bot Donaciones  Conexion DB").info(" - [Preparacion Base de Datos]: Insercion correcta!");
                            respuesta = true;
                            sb.setLength(0);
                        }
                    }
                    sb.append(linea).append("\n");
                }

                if (sb.length() > 0) {
                    EjecutarSentencia(sb.toString());
                    respuesta = true;
                }

            } catch (IOException e) {
                LoggerFactory.getLogger("Bot Donaciones  Conexion DB").error(" - [Preparacion Base de Datos]: Error al leer las sentencias de preparacion de la base de datos: " + e.getMessage());
                respuesta = false;
            }
        } else {
            LoggerFactory.getLogger("Bot Donaciones  Conexion DB").error(" - [Preparacion Base de Datos]: No se ha encontrado el archivo de las sentencias de preparacion de la base de datos");
        }
        LoggerFactory.getLogger("Bot Donaciones  Conexion DB").info(" - [Preparacion Base de Datos]: Finalizacion de Preparacion de Base de Datos");
        return respuesta;
    }

    /**
     * Este método se utiliza para ejecutar una sentencia SQL en la base de datos.
     *
     * @param sentencia La sentencia SQL que se va a ejecutar.
     *                  <p>
     *                  Utiliza la conexión a la base de datos obtenida a través del método {@link ConexionBD#obtenerConexion()}.
     *                  Luego, crea una sentencia a través de {@link Connection#createStatement()} y la ejecuta usando {@link Statement#executeUpdate(String)}.
     *                  <p>
     *                  Si ocurre una {@link SQLException}, se muestra un mensaje de error.
     *                  Si ocurre una {@link RuntimeException}, se muestra un mensaje de error.
     *
     *                  <p>
     *                  Ejemplo de uso:
     *                  <pre>
     *                  {@code
     *                  ConexionBD.EjercutarSentencia("INSERT INTO tabla (columna) VALUES ('valor`)");
     *                  }
     *                  </pre>
     *                  </p>
     */
    private static void EjecutarSentencia(String sentencia) {
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            conexion.createStatement().executeUpdate(sentencia);
        } catch (SQLException e) {
            LoggerFactory.getLogger("Bot Donaciones  Conexion DB").error(" - [Preparacion Base de Datos]: Error al insertar una de las sentencias de preparacion en la base de datos: " + e.getMessage());
        } catch (RuntimeException ex) {
            LoggerFactory.getLogger("Bot Donaciones  Conexion DB").error(" - [Preparacion Base de Datos]: Ha ocurrido un error al contactar una sentencia de preparacion de la base de datos: " + ex.getMessage());
        }
    }
}
