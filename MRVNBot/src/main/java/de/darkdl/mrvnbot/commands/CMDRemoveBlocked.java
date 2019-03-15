/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot.commands;

import de.darkdl.mrvnbot.Core;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Nils
 */
public class CMDRemoveBlocked implements Command{

    @Override
    public void called(String[] args, MessageReceivedEvent evt) {
        
        if (Core.removeBlockedWord(args[0])) {
            Core.sendMessageToChannel("Removed the regex from the list!", evt.getChannel());
        } else {
            Core.sendMessageToChannel("That regex is not in the list!", evt.getChannel());
        }
    }
    
}
