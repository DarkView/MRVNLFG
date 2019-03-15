/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot.commands;

import de.darkdl.mrvnbot.Core;
import java.util.List;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Nils
 */
public class CMDListBlocked implements Command{

    @Override
    public void called(String[] args, MessageReceivedEvent evt) {
        List<String> blockedWords = Core.getBlockedWords();
        Core.sendMessageToChannel(blockedWords.toString(), evt.getChannel());
    }
    
}
