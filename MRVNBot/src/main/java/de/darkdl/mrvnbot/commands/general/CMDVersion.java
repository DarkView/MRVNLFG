/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot.commands.general;

import de.darkdl.mrvnbot.Core;
import java.awt.Color;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Nils
 */
public class CMDVersion implements Command{

    @Override
    public void called(String[] args, MessageReceivedEvent evt) {
        
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor("Version");
        eb.setColor(Color.GREEN);
        eb.setDescription("**Current Version:** " + Core.VERSION
                + "\n**Latest Stable Version:** " + Core.getLatestStableVersion());
        
        Core.sendMessageToChannel(eb.build(), evt.getChannel());
        Core.outLFGInfo(evt.getAuthor(), "Requested bot version");
    }
    
}
