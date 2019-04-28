/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot.commands;

import de.darkdl.mrvnbot.Core;
import de.darkdl.mrvnbot.LFGHandler;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Nils
 */
public class CMDWhere implements Command {

    @Override
    public void called(String[] args, MessageReceivedEvent evt) {

        if (args.length >= 1) {

            sendWhere(args[0], evt.getChannel(), evt.getAuthor(), "");

        } else {
            Core.sendMessageToChannel("No userID specified!", evt.getChannel());
        }

    }

    public static void sendWhere(String userID, MessageChannel channel, User auth, String addition) {
        VoiceChannel vc = LFGHandler.whereIsUser(userID);

        if (vc != null) {
            Core.sendMessageToChannel(addition + "\n" + vc.getName() + "  |  " + LFGHandler.createInvite(vc, auth).getURL(), channel);
        } else {
            Core.sendMessageToChannel("The user is not in a voice channel!", channel);
        }
        
        Core.outLFGInfo(auth, "Executed where for user " + userID);
    }

}
