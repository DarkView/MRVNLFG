/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot;

import java.util.HashMap;
import java.util.List;
import net.dv8tion.jda.core.entities.Invite;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

/**
 * The main class used for dealing with the LFG requests
 * @author Nils Blömeke
 */
public class LFGHandler {

    private static final HashMap<String, VoiceChannel> CONNECTED_USERS = new HashMap<String, VoiceChannel>();

    /**
     * Adds or overwrites the users current voice channel
     * @param userID - The snowflake of the user for which the association is to be added or overwritten
     * @param channel - The VoiceChannel object the user is currently in
     */
    public static void userConnected(String userID, VoiceChannel channel) {
        CONNECTED_USERS.put(userID, channel);
    }

    /**
     * Removes a user and its association from the Map
     * @param userID - The user that is to be removed
     */
    public static void userDisconnected(String userID) {
        CONNECTED_USERS.remove(userID);
    }

    /**
     * Handles the creation of the message and the invite to the channel the user is in
     * If the channel already has a direct invite it is reused.
     * Also cancels if the user is either not in a channel or it is full
     * @param msg - The message the user sent to initiate the processing
     * @param channel - The channel in which the message was sent
     */
    public static void createLFG(Message msg, MessageChannel channel) {

        User user = msg.getAuthor();

        if (CONNECTED_USERS.containsKey(user.getId())) {

            VoiceChannel vc = CONNECTED_USERS.get(user.getId());
            if (vc.getMembers().size() < vc.getUserLimit()) {

                try {

                    String msgContent = msg.getContentStripped().replaceFirst("(?i)!?"+Vars.COMMAND_IDENTIFIER, "");

                    if (!msgContent.equals("")) {
                        msgContent = "```".concat(msgContent.trim()).concat("```");
                    }

                    List<Invite> invites = vc.getInvites().complete(true);
                    String channelInfo;

                    if (!invites.isEmpty()) {

                        channelInfo = "Join " + user.getAsMention() + " in "
                                + vc.getName() + " via " + invites.get(0).getURL() + "\n";

                    } else {

                        Invite inv = vc.createInvite().complete(true);
                        channelInfo = "Join " + user.getAsMention() + " in "
                                + vc.getName() + " via " + inv.getURL() + "\n";

                    }

                    msgContent = channelInfo.concat(msgContent);
                    Core.sendMessageToChannel(msgContent, channel);

                    Core.outLFGInfo(user, "Succesfully completed LFG request");

                } catch (RateLimitedException ex) {
                    Core.outError(ex.getMessage(), ex);
                }
            } else {
                Core.sendMessageToChannel("Sorry, but that voice channel is full! " + user.getAsMention(), channel);
                Core.outLFGInfo(user, "Stopped LFG request, user is in a full channel");
            }
        } else {
            Core.sendMessageToChannel("Sorry, but you have to be in a lfg voice channel! " + user.getAsMention(), channel);
            Core.outLFGInfo(user, "Stopped LFG request, user is not in a channel");
        }

        Core.deleteMessage(msg);

    }

    /**
     * Used to load all the visible Channels and its members into the Map on startup
     * @param channels - A list of all channels to be loaded
     */
    static void loadVoiceChannels(List<VoiceChannel> channels) {

        for (VoiceChannel c : channels) {
            if (c.getName().toLowerCase().contains("team")) {

                List<Member> members = c.getMembers();
                for (Member m : members) {
                    userConnected(m.getUser().getId(), c);
                }

            }
        }

        Core.outInfo("Loaded voice channels");

    }

}
