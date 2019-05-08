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

        if (args.length == 1 && args[0].matches("^[0-9]+$") && args[0].length() < 20) {

            VoiceListener.addNotify(args[0], evt.getAuthor(), Core.VARS.NOTIF_EXPIRE_SECONDS);
            Core.sendMessageToChannel("If <@!" + args[0] + "> joins or switches"
                    + " VC in the next " + Core.VARS.NOTIF_EXPIRE_SECONDS + " seconds, i will notify you.", evt.getChannel());

        } else if (args.length >= 2 && args[0].matches("^[0-9]+$") && args[1].matches("^[0-9]+$") && args[0].length() < 20 && args[1].length() < 5) {

            VoiceListener.addNotify(args[0], evt.getAuthor(), Integer.parseInt(args[1]));
            Core.sendMessageToChannel("If <@!" + args[0] + "> joins or switches"
                    + " VC in the next " + args[1] + " seconds, i will notify you.", evt.getChannel());

        } else if (args.length == 1 && args[0].startsWith("l")) {

            Core.sendMessageToChannel(VoiceListener.listNotifies(), evt.getChannel());
            
        } else {
            
            VoiceListener.removeNotifiesForMod(evt.getAuthor());
            Core.sendMessageToChannel("I have cleared all of your notifications.", evt.getChannel());

        }

    }

}
