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
public class CMDMessage implements Command {

    @Override
    public void called(String[] args, MessageReceivedEvent evt) {
        if (!Core.VARS.isOwner(evt.getAuthor().getId())) {
            return;
        }

        boolean save = true;
        String status = "";
        try {
            switch (args[0]) {
                case "create":
                    Core.newMessage(args[1]);
                    status = "Message created.";
                    break;
                case "load":
                    save = false;
                    Core.loadMessage(args[1]);
                    status = "Message loaded.";
                    break;
                case "unload":
                    save = false;
                    Core.unloadMessage();
                    status = "Message unloaded.";
                    break;
                case "setchannels":
                    Core.setChannels(args);
                    status = "Channels resolved and set.";
                    break;
                case "addchannels":
                    Core.addChannels(args);
                    status = "Channels resolved and added.";
                    break;
                case "setmessage":
                    Core.setMessage(args);
                    status = "Message set.";
                    break;
                case "setpin":
                    boolean pinStatus = Core.setPin((args[1].equals("t") || args[1].equals("true")) || args[1].equals("1"));
                    status = "Pin status set to " + String.valueOf(pinStatus) + ".";
                    break;
                case "post":
                    evt.getChannel().sendTyping().complete();
                    Core.postMessage();
                    status = "Message posted/edited in all channels.";
                    break;
                case "edit":
                    evt.getChannel().sendTyping().complete();
                    Core.postMessage();
                    status = "Message posted/edited in all channels.";
                    break;
                case "delete":
                    evt.getChannel().sendTyping().complete();
                    Core.deleteMrvnMessage();
                    status = "Message deleted from all channels.\nNote: This does not delete the message object, you can still access it with the name";
                    break;
                case "info":
                    Core.postMRVNMessageInfo(evt.getChannel());
                    status = "All of the messages information is above.";
                    break;
                default:
                    Core.sendMessageToChannel(evt.getAuthor().getAsMention() + " unknown command!", evt.getChannel());
            }
        } catch (Exception e) {
            Core.outError(e.getMessage(), e);
            Core.sendMessageToChannel("Error! " + e.getMessage() + " - is a message loaded?", evt.getChannel());
        }

        Core.sendMessageToChannel(":wrench: Done! " + status, evt.getChannel());
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
