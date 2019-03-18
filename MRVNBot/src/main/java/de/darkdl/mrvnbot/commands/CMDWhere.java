/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot.commands;

import de.darkdl.mrvnbot.Core;
import de.darkdl.mrvnbot.LFGHandler;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Nils
 */
public class CMDWhere implements Command {

    @Override
    public void called(String[] args, MessageReceivedEvent evt) {
        
        VoiceChannel vc = LFGHandler.whereIsUser(args[0]);
        if (vc != null) {
            Core.sendMessageToChannel(vc.getName() + "  |  " + LFGHandler.createInvite(vc, evt.getAuthor()).getURL(), evt.getChannel());
            Core.outLFGInfo(evt.getAuthor(), "Executed where for user " + args[0]);
        } else {
            Core.sendMessageToChannel("The user is not in a LFG voice channel!", evt.getChannel());
        }
        
        Core.outLFGInfo(evt.getAuthor(), "Executed where for user " + args[0]);
    }

}
