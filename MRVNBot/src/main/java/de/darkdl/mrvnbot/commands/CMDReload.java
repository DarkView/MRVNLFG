/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot.commands;

import de.darkdl.mrvnbot.Core;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Nils
 */
public class CMDReload implements Command {

    @Override
    public void called(String[] args, MessageReceivedEvent evt) {
        Message msg = evt.getMessage();
        
        msg.delete().queue();
        Core.sendMessageToChannel("*Reloading config...*", msg.getChannel());
        Core.updateVars();
        Core.sendMessageToChannel("*Reloaded!*", msg.getChannel());
        Core.outLFGInfo(msg.getAuthor(), "Reloaded the config");

    }

}
