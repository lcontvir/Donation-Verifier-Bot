package com.lcontvir.bot.vista.discord;

import com.lcontvir.bot.controlador.discord.SupportEmbedBuilder;
import com.lcontvir.bot.modelo.PropsLoader;
import com.lcontvir.bot.modelo.discord.DiscordEntradaSoporteManager;
import com.lcontvir.bot.modelo.discord.EntradaSoporte;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.LoggerFactory;

public class StringSelectListener extends ListenerAdapter {

    @Override
    /**
     * Este método se activa cuando se selecciona una opción en un componente de selección de cadena en Discord.
     *
     * @param event El evento de interacción de selección de cadena que contiene la información sobre la selección realizada por el usuario.
     *
     * Primero, se obtiene el ID de la opción seleccionada por el usuario.
     * Luego, se obtiene el objeto EntradaSoporte correspondiente a ese ID.
     *
     * Si el objeto EntradaSoporte no tiene un nodo siguiente (es decir, es el final de una secuencia de soporte),
     * se edita el mensaje del evento para mostrar un embed de soporte con el contenido del archivo de soporte correspondiente
     * y se establece una fila de acción con botones para finalizar la secuencia de soporte.
     *
     * Si el objeto EntradaSoporte tiene un nodo siguiente, se edita el mensaje del evento para mostrar un embed de soporte con el contenido del archivo de soporte correspondiente
     * y se establece una fila de acción con un menú de selección de cadena que contiene las opciones de soporte correspondientes al nodo siguiente.
     */
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        try {
            if (PropsLoader.isSoporteActive()) {
                String IdSeleccionado = event.getValues().get(0);
                EntradaSoporte EntradaSoporteSeleccionada = EntradaSoporte.getEntradaSoporteByRowID(IdSeleccionado);
                System.out.println(EntradaSoporteSeleccionada.getPathToMessage());
                if (EntradaSoporteSeleccionada.getNextNode() == null) {
                    event.editMessageEmbeds(SupportEmbedBuilder.RequestSupportEmbed(EntradaSoporte.readSupportSpecificFile(EntradaSoporteSeleccionada.getPathToMessage()))).setActionRow(SupportEmbedBuilder.RequestEndBaseSupportActionRowButtons()).queue();
                } else {
                    event.editMessageEmbeds(SupportEmbedBuilder.RequestSupportEmbed(EntradaSoporte.readSupportSpecificFile(EntradaSoporteSeleccionada.getPathToMessage()))).setActionRow(SupportEmbedBuilder.RequestSupportActionRowMenu(DiscordEntradaSoporteManager.getEntradasSoportebyNodeName(EntradaSoporteSeleccionada.getNextNode()))).queue();
                }
            }
        } catch (Exception ex) {
            LoggerFactory.getLogger("M.I.M.I - Discord String Select Listener").error("Ha ocurrido un error al procesar la interacción de selección de cadena: " + ex.getMessage());
        }
    }
}
