/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot.commands;

import de.darkdl.mrvnbot.Core;
import de.darkdl.mrvnbot.Listeners.VoiceListener;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Nils
 */
public class CMDNotify implements Command {

    @Override
    public void called(String[] args, MessageReceivedEvent evt) {
        
        if (args.length >= 1) {
            VoiceListener.addNotify(args[0], evt.getAuthor());
            Core.sendMessageToChannel("If <@!" + args[0] + "> joins or switches"
                    + " VC in the next " + Core.VARS.NOTIF_EXPIRE_SECONDS + " seconds, i will notify you.", evt.getChannel());
        } else {
            VoiceListener.removeNotifiesForMod(evt.getAuthor());
            Core.sendMessageToChannel("I have cleared all of your notifications.", evt.getChannel());
        }
        
    }
    
}
