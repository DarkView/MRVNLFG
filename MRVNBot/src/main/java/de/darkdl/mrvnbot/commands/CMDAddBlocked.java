/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot.commands;

import de.darkdl.mrvnbot.Core;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Nils
 */
public class CMDAddBlocked implements Command{

    @Override
    public void called(String[] args, MessageReceivedEvent evt) {
        String regex = args[0];
        try {
        Pattern.compile(regex);
        Core.addBlockedWord(regex);
        Core.sendMessageToChannel("Added to list of blocked regexs!", evt.getChannel());
        } catch (PatternSyntaxException ex) {
            Core.sendMessageToChannel("Sorry, but that is not a valid regex in the java regex implementation! "
                    + evt.getAuthor().getAsMention(), evt.getChannel());
        }
    }
    
}
