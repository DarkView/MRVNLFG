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
public class CMDUpdateVar implements Command {

    @Override
    public void called(String[] args, MessageReceivedEvent evt) {

        String var = args[0];
        String input = args[1];
        boolean success = true;

        switch (var.toUpperCase()) {
            case "CMD_PREFIX":
                Core.VARS.CMD_PREFIX = input;
                break;
            case "LFG_VOICE_IDENTIFIER":
                Core.VARS.LFG_VOICE_IDENTIFIER = input;
                break;
            case "LFG_TEXT_IDENTIFIER":
                Core.VARS.LFG_TEXT_IDENTIFIER = input;
                break;
            case "LFG_COMMAND_IDENTIFIER":
                Core.VARS.LFG_COMMAND_IDENTIFIER = input;
                break;
            case "INVITE_EXPIRE_SECONDS":
                Core.VARS.INVITE_EXPIRE_SECONDS = Integer.parseInt(input);
                break;
            case "LIST_OTHER_USERS":
                Core.VARS.LIST_OTHER_USERS = input.equals("true") || input.equals("1") || input.equals("t");
                break;
            case "MESSAGE_COMPACT":
                Core.VARS.MESSAGE_COMPACT = input.equals("true") || input.equals("1") || input.equals("t");
                break;
            default:
                Core.sendMessageToChannel("*Sorry, but that var doesnt exist or cannot be updated while running!*", evt.getMessage().getChannel());
                success = false;
        }
        
        Core.saveVars();
        if (success) {
            Core.sendMessageToChannel("*Updated succesfully.* Change is now in effect!", evt.getMessage().getChannel());
        }

    }

}
