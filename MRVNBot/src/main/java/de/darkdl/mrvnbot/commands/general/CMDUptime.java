/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot.commands.general;

import de.darkdl.mrvnbot.Core;
import java.awt.Color;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Nils
 */
public class CMDUptime implements Command {

    @Override
    public void called(String[] args, MessageReceivedEvent evt) {
        long uptimeMillis = System.currentTimeMillis() - Core.bootTime;
        
        String uptimeFormatted = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(uptimeMillis),
                TimeUnit.MILLISECONDS.toMinutes(uptimeMillis)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(uptimeMillis)),
                TimeUnit.MILLISECONDS.toSeconds(uptimeMillis)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(uptimeMillis))
        );
        
        EmbedBuilder eb = new EmbedBuilder().setTitle("Uptime");
        eb.setColor(Color.green);
        eb.setDescription(uptimeFormatted);
        
        Core.sendMessageToChannel(eb.build(), evt.getChannel());
    }

}
