/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot.commands;

import de.darkdl.mrvnbot.Core;
import java.util.Arrays;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Nils
 */
public class CMDMessage implements Command {

    @Override
    public void called(String[] args, MessageReceivedEvent evt) {
        if (!Core.VARS.isOwner(evt.getAuthor().getId())) {
            return;
        }

        boolean save = true;
        try {
            switch (args[0]) {
                case "create":
                    Core.newMessage(args[1]);
                    break;
                case "load":
                    save = false;
                    Core.loadMessage(args[1]);
                    break;
                case "setchannels":
                    Core.setChannels(args);
                    break;
                case "setmessage":
                    Core.setMessage(args);
                    break;
                case "setpin":
                    Core.setPin((args[1].equals("t") || args[1].equals("true")) || args[1].equals("1"));
                    break;
                case "post":
                    Core.postMessage();
                    break;
                case "delete":
                    Core.deleteMrvnMessage();
                    break;
                case "info":
                    Core.postMRVNMessageInfo(evt.getChannel());
                    break;
                default:
                    Core.sendMessageToChannel(evt.getAuthor().getAsMention() + " wrong syntax!", evt.getChannel());
            }
        } catch (Exception e) {
            Core.outError(e.getMessage(), e);
            Core.sendMessageToChannel("Error! " + e.getMessage() + " - is a message loaded?", evt.getChannel());
        }

        Core.sendMessageToChannel(":wrench: Done!", evt.getChannel());
        if (args.length >= 2) {
            Core.outLFGInfo(evt.getAuthor(), "Message: Executed " + args[0] + " " + args[1]);
        } else {
            Core.outLFGInfo(evt.getAuthor(), "Message: Executed " + args[0]);
        }
        if (save) {
            Core.saveMessage();
        }

    }

}
