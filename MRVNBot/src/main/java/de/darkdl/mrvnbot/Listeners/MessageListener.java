/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot.Listeners;

import de.darkdl.mrvnbot.utils.CommandHandler;
import de.darkdl.mrvnbot.utils.CommandParser;
import de.darkdl.mrvnbot.Core;
import de.darkdl.mrvnbot.LFGHandler;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Standard MessageListener
 *
 * @author Nils Blömeke
 */
public class MessageListener extends ListenerAdapter {

    /**
     * Upon receiving a message, we check it for validity against the command
     * identifier Everything else is handled by the LFGHandler if the message is
     * in fact a LFG message
     *
     * @param evt - The event passed on by JDA
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent evt) {
        Message msg = evt.getMessage();
        String msgContent = msg.getContentStripped().trim();

        if (msg.getChannel().getName().contains(Core.VARS.LFG_TEXT_IDENTIFIER)
                && msgContent.toLowerCase().startsWith(Core.VARS.LFG_COMMAND_IDENTIFIER)) {

            Core.outLFGInfo(msg.getAuthor(), "Started LFG request in " + msg.getChannel().getName());
            LFGHandler.createLFG(msg, msg.getChannel());

        } else if (msgContent.startsWith(Core.VARS.CMD_PREFIX) && Core.VARS.isMod(msg.getMember())) {

            CommandHandler.handleCommand(CommandParser.parser(msg.getContentRaw(), evt));

        }

    }

}
