/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot.commands.general;

import de.darkdl.mrvnbot.Core;
import de.darkdl.mrvnbot.LFGHandler;
import java.awt.Color;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Nils
 */
public class CMDDelay implements Command{

    @Override
    public void called(String[] args, MessageReceivedEvent evt) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor("Delays");
        eb.setColor(Color.YELLOW);
        
        long ping = Core.getPing();
        long[] delays = LFGHandler.MESSAGE_DELAY;
        long delayAvg = 0;
        
        for (long delay : delays) {
            delayAvg += delay;
        }
        delayAvg = delayAvg / delays.length;
        if (delayAvg >= 1000 || ping >= 250) {
            eb.setColor(Color.RED);
        } else if (delayAvg <= 500 && ping <= 150) {
            eb.setColor(Color.GREEN);
        }
        
        eb.setDescription("**Ping:** " + ping + "ms\n**LFG Delay:** " + delayAvg + "ms");
        Core.sendMessageToChannel(eb.build(), evt.getChannel());
        Core.outLFGInfo(evt.getAuthor(), "Requested bot delays");
    }
    
}
