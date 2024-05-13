package com.lcontvir.bot.vista.discord;

import com.lcontvir.bot.controlador.discord.SupportEmbedBuilder;
import com.lcontvir.bot.controlador.discord.TicketEmbedBuilder;
import com.lcontvir.bot.modelo.PropsLoader;
import com.lcontvir.bot.modelo.discord.DiscordEntradaSoporteManager;
import com.lcontvir.bot.modelo.discord.DiscordTicketManager;
import com.lcontvir.bot.modelo.discord.EntradaSoporte;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        if(PropsLoader.isSoporteActive()){
            switch (event.getButton().getId()) {
                case "AutomaticSupport":
                    event.replyEmbeds(SupportEmbedBuilder.RequestSupportEmbed(EntradaSoporte.readSupportSpecificFile("ejemplo6.md"))).addActionRow(SupportEmbedBuilder.RequestSupportActionRowMenu(DiscordEntradaSoporteManager.getEntradasSoporteBase())).setEphemeral(true).queue();
                    break;
                case "CloseAutomaticSupport":
                    event.editMessage("Gracias por usar nuestros servicios!").setReplace(true).queue();
                    break;
                case "AutomaticSupportContinue":
                    event.editMessageEmbeds(SupportEmbedBuilder.RequestSupportEmbed(EntradaSoporte.readSupportSpecificFile("ejemplo6.md"))).setActionRow(SupportEmbedBuilder.RequestSupportActionRowMenu(DiscordEntradaSoporteManager.getEntradasSoporteBase())).queue();
                    break;
                case "ManualSupport":
                    TextChannel ticket = DiscordTicketManager.CreateTicket(event.getMember(), "1238812869206741032");
                    ticket.sendMessageEmbeds(TicketEmbedBuilder.ShowTicketPanelEmbed()).addActionRow(TicketEmbedBuilder.CloseTicketActionRowButtons()).queue();
                    event.replyEmbeds(TicketEmbedBuilder.RequestTicketEmbed(ticket)).setEphemeral(true).queue();
                    break;
                case "ManualSupportContinue":
                    TextChannel ticketContinue = DiscordTicketManager.CreateTicket(event.getMember(), "1238812869206741032");
                    ticketContinue.sendMessageEmbeds(TicketEmbedBuilder.ShowTicketPanelEmbed()).addActionRow(TicketEmbedBuilder.CloseTicketActionRowButtons()).queue();
                    event.editMessageEmbeds(TicketEmbedBuilder.RequestTicketEmbed(ticketContinue)).setReplace(true).queue();
                    break;
                case "CloseTicket":
                    event.getChannel().asTextChannel().delete().queue();
                    break;
            }
        }
    }
}
