# M.I.M.I. - Bot Multifuncional de Discord

## Descripción General

**M.I.M.I** (Multifunctional Interactive Messaging Interface) es un bot de Discord multifunción diseñado para gestionar donaciones, soporte y feedback. M.I.M.I está centrado en proporcionar automatización a los usuarios, simplificando y unificando diversas acciones en un mismo lugar: Discord. Este bot, desarrollado en Java, interactúa con las APIs de Discord y Steam, utilizando una base de datos MySQL para almacenar la información pertinente. El proyecto sigue el patrón arquitectónico Modelo-Vista-Controlador (MVC) para asegurar un código estructurado y fácil de mantener.
## Dependencias

El bot utiliza varias dependencias gestionadas por Gradle:

- [JDA (Java Discord API)](https://github.com/DV8FromTheWorld/JDA) - versión 5.0.0-beta.19
- [Logback Classic](http://logback.qos.ch/) - versión 1.4.12
- [Apache HttpClient](https://hc.apache.org/httpcomponents-client-4.5.x/) - versión 4.5.13
- [Gson](https://github.com/google/gson) - versión 2.9.1
- [MySQL Connector/J](https://dev.mysql.com/doc/connector-j/) - versión 8.0.28
- [JSON](https://github.com/stleary/JSON-java) - versión 20210307

## Estructura del Proyecto

El código sigue el patrón Modelo-Vista-Controlador (MVC) para una mejor organización y separación de responsabilidades.

- **Modelo**: Contiene clases que representan la estructura de datos, como información de donaciones de Discord y Steam, e interactúa con la base de datos MySQL.

- **Vista**: Maneja componentes relacionados con la interfaz de usuario, aunque en el contexto de un bot de Discord, se centra principalmente en las interacciones con los usuarios de Discord.

- **Controlador**: Gestiona el flujo de datos entre el Modelo y la Vista, procesa solicitudes de las APIs de Discord y Steam, y maneja la lógica de verificación.

## Rutas Importantes

1. **/modelo/Bot**: Es la clase principal del bot, la cual se iniciará al ejecutar el programa.
2. **/modelo/PropsLoader**: Es la encargada de obtener las configuraciones del programa.
3. **/modelo/discord/DiscordDonationManager**: Se centra en las operaciones de las donaciones, su gestión y procesamiento.
4. **/modelo/discord/DiscordEntradaSoporteManager**: Se centra en las operaciones relacionadas con la solicitud de soporte, su gestión y procesamiento.
5. **/modelo/discord/DiscordTicketManager**: Se centra en las operaciones relacionada con la creacion de tickets, su gestión y procesamiento.

## Puesta en Marcha

Para poner en funcionamiento el bot de Discord para la verificación de donaciones, sigue estos pasos:

1. **Configuración de Archivos**

   Deberás rellenar dos archivos de configuración importantes antes de ejecutar el bot. Estos archivos se proporcionan con ejemplos (`.example`) para facilitar la configuración.

    - **donation_bot.properties**: Contiene la configuración básica del bot, como el token de Discord, las credenciales de la base de datos y las claves de API necesarias.
        - Ejemplo de `donation_bot.properties`:
          ```properties
            # Archivo de configuración - Bot de Verificación de Donaciones
            
            # Las líneas que empiecen por # serán indicadas como solo lectura
            
            # -- Propiedades --
            
            # Token de conexión a Discord
            JDA_TOKEN =
            
       
            # --- --- Apartado configuracion conexion a terceros  --- ---
            
            #Deberia de estar activado la conexion a terceros?
            ConexionATercerosActive = True
            
            # Token de conexión a Steam API
            STEAM_TOKEN =
            
            # Ruta de la Base de Datos MySQL que se va a acceder, junto a su puerto y la base de datos proporcionada
            BBDDConexion =
            
            # Usuario de la Base de Datos
            BBDDUser =
            
            # Contraseña del usuario proporcionado
            BBDDPassw =
            
            # Cantidad de tiempo en horas en las que el usuario no podra modificar su donacion
            CoolDownModificacion = 3
            
            # Cantidad de tiempo en dias en los cuales la donacion caducara
            ExpireDays = 30
            
            
            
            # --- --- Apartado configuracion canal de feedback  --- ---
            
            #Deberia de estar activado el feedback?
            FeedbackActive = True
            
            #Identificar del canal donde ira destinado el feedback
            FeedbackChannelId =
            
            
            
            # --- --- Apartado configuracion canal de soporte automatico  --- ---
            
            #Deberia de estar activado el soporte?
            SoporteActive = True
            
            #Identificar del canal donde ira destinado el soporte
            SupportChannelId =
            
            #Identificar de la categoria donde se crearan los tickets
            TicketCategoryId =
          ```

    - **supportquestions.json**: Este archivo es necesario si deseas utilizar la funcionalidad de soporte del bot. Debe contener las preguntas y respuestas predefinidas para el sistema de tickets de soporte.
        - Ejemplo de `supportquestions.json`:
          ```json
            {
            "Menu_Seleccion": {
            "0": {
            "Titulo": "Discord",
            "Descripcion": "Por que un servidor de discord?",
            "Emoji": "U+1F4DC",
            "Path_to_message": "ejemplo.md",
            "Support_block": "base"
            },
            "1": {
            "Titulo": "Profesionales",
            "Descripcion": "Existe una categoria dedicada a profesionales?",
            "Emoji": "U+1F3F0",
            "Path_to_message": "ejemplo2.md",
            "Support_block": "base"
            },
            "2": {
            "Titulo": "Nosotros",
            "Descripcion": "Quieres saber un poco de nosotros?",
            "Emoji": "U+1F3F0",
            "Path_to_message": "ejemplo3.md",
            "Support_block": "base"
            },
            "3": {
            "Titulo": "Contactarnos",
            "Descripcion": "Como me puedo comunicar con vosotros?",
            "Emoji": "U+1F3F0",
            "Path_to_message": "ejemplo4.md",
            "Support_block": "base"
            },
            "5": {
            "Titulo": "Ferias",
            "Descripcion": "Quiero ver mas informacion de las ferias",
            "Emoji": "U+1F3F0",
            "Support_block": "base",
            "Path_to_message": "ejemplo6.md",
            "Id_to_next_supportblock": "1"
            },
            "4": {
            "Titulo": "Feria 11/5/22",
            "Descripcion": "Informacion sobre la feria 11/5/22",
            "Emoji": "U+1F3F0",
            "Path_to_message": "ejemplo5.md",
            "Support_block": "1"
            }
            }
            }
          ```

2. **Base de Datos**

   El bot requiere una base de datos MariaDB para almacenar las donaciones. Asegúrate de tener una base de datos configurada y accesible, y de proporcionar las credenciales correctas en el archivo `donation_bot.properties`.

3. **Construcción del Proyecto**

   Construye el proyecto utilizando Gradle para obtener el archivo ejecutable `.jar`. Ejecuta el siguiente comando en la raíz del proyecto:
   ```bash
   gradle build
   ```
   Esto generará el archivo `.jar` en el directorio `build/libs`.

4. **Ejecución del Bot**

   Una vez que hayas configurado los archivos necesarios y construido el proyecto, puedes ejecutar el bot utilizando el archivo `.jar` generado:
   ```bash
   java -jar build/libs/discord-verification-bot.jar
   ```

## Licencia

Este proyecto está registrado bajo la [Licencia Pública General de GNU (GPL)](https://www.gnu.org/licenses/gpl-3.0.html). Consulta el archivo de licencia incluido para obtener más detalles.