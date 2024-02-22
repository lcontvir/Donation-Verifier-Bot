# Bot de Discord para Verificación de Donaciones del juego SCP Secret Laboratory

## Descripción General

Este bot de Discord, desarrollado en Java, tiene como objetivo verificar donaciones a través de las APIs de Discord y Steam, utilizando una base de datos MySQL para almacenar dichas donaciones. El proyecto sigue el patrón arquitectónico Modelo-Vista-Controlador (MVC) para asegurar un código estructurado y fácil de mantener.

## Dependencias

El bot utiliza varias dependencias gestionadas por Gradle:

- [JDA (Java Discord API)](https://github.com/DV8FromTheWorld/JDA) - versión 5.0.0-beta.19
- [Logback Classic](http://logback.qos.ch/) - versión 1.4.12
- [Apache HttpClient](https://hc.apache.org/httpcomponents-client-4.5.x/) - versión 4.5.13
- [Gson](https://github.com/google/gson) - versión 2.9.1
- [MySQL Connector/J](https://dev.mysql.com/doc/connector-j/) - versión 8.0.28

## Estructura del Proyecto

El código sigue el patrón Modelo-Vista-Controlador (MVC) para una mejor organización y separación de responsabilidades.

- **Modelo**: Contiene clases que representan la estructura de datos, como información de donaciones de Discord y Steam, e interactúa con la base de datos MySQL.

- **Vista**: Maneja componentes relacionados con la interfaz de usuario, aunque en el contexto de un bot de Discord, se centra principalmente en las interacciones con los usuarios de Discord.

- **Controlador**: Gestiona el flujo de datos entre el Modelo y la Vista, procesa solicitudes de las APIs de Discord y Steam, y maneja la lógica de verificación.

## Rutas Importantes

1. **/modelo/Bot**: Es la clase principal del bot, la cual se iniciará al ejecutar el programa.
2. **/modelo/PropsLoader**: Es la encargada de obtener las configuraciones del programa.
3. **/modelo/discord/DiscordManager**: Se centra en las operaciones de las donaciones, su gestión y procesamiento.

## Licencia

Este proyecto está registrado bajo la [Licencia Pública General de GNU (GPL)](https://www.gnu.org/licenses/gpl-3.0.html). Consulta el archivo de licencia incluido para obtener más detalles.
