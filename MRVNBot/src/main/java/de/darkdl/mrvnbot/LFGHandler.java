/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot;

import static de.darkdl.mrvnbot.Core.outInfo;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import net.dv8tion.jda.core.entities.Invite;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

/**
 * The main class used for dealing with the LFG requests
 *
 * @author Nils Blömeke
 */
public class LFGHandler {

    private static final HashMap<String, VoiceChannel> CONNECTED_USERS = new HashMap<String, VoiceChannel>();

    public static final long[] MESSAGE_DELAY = new long[5];
    private static int CURRENT_POS = 0;

    /**
     * Adds or overwrites the users current voice channel
     *
     * @param userID - The snowflake of the user for which the association is to
     * be added or overwritten
     * @param channel - The VoiceChannel object the user is currently in
     */
    public static void userConnected(String userID, VoiceChannel channel) {
        CONNECTED_USERS.put(userID, channel);
    }

    /**
     * Removes a user and its association from the Map
     *
     * @param userID - The user that is to be removed
     */
    public static void userDisconnected(String userID) {
        CONNECTED_USERS.remove(userID);
    }

    /**
     * Handles the creation of the message and the invite to the channel the
     * user is in If the channel already has a direct invite it is reused. Also
     * cancels if the user is either not in a channel or it is full
     *
     * @param msg - The message the user sent to initiate the processing
     * @param channel - The channel in which the message was sent
     */
    public static void createLFG(Message msg, MessageChannel channel) {

        User user = msg.getAuthor();
        Long startTime = System.currentTimeMillis();

        VoiceChannel vc = CONNECTED_USERS.getOrDefault(user.getId(), null);
        if (vc != null && vc.getName().contains(Core.VARS.LFG_VOICE_IDENTIFIER)) {

            int vcMembers = vc.getMembers().size();
            if (vcMembers < vc.getUserLimit()) {

                String msgContent = msg.getContentStripped();
                String hasBlockedWord = "";
                for (String blockedWord : Core.getBlockedWords()) {
                    if (Pattern.compile(blockedWord).matcher(msgContent).find()) {
                        hasBlockedWord = blockedWord;
                    }
                }

                if (hasBlockedWord.equals("")) {

                    try {

                        msgContent = msgContent.replaceFirst("(?i)" + Core.VARS.LFG_COMMAND_IDENTIFIER, "");
                        String channelInfo = "";

                        if (!msgContent.equals("")) {
                            if (Core.VARS.MESSAGE_COMPACT) {
                                msgContent = "`".concat(msgContent.trim()).concat("`");
                            } else {
                                msgContent = "```".concat(msgContent.trim()).concat("```");
                            }
                        }

                        channelInfo = handleMessageCreation(vc, user, msgContent);
                        Core.sendMessageToChannel(channelInfo, channel);

                        Core.outLFGInfo(user, "Succesfully completed LFG request");

                        MESSAGE_DELAY[CURRENT_POS] = System.currentTimeMillis() - startTime;
                        CURRENT_POS++;
                        if (CURRENT_POS >= 5) {
                            CURRENT_POS = 0;
                        }

                    } catch (RateLimitedException ex) {
                        Core.outError(ex.getMessage(), ex);
                    }

                } else {
                    Core.outLFGInfo(user, "Stopped LFG request, user triggered the following regex: " + hasBlockedWord);
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
     * Used to load all the visible Channels and its members into the Map on
     * startup
     *
     * @param channels - A list of all channels to be loaded
     */
    static void loadVoiceChannels(List<VoiceChannel> channels) {

        outInfo("Scanning all voice-channels known...");

        for (VoiceChannel c : channels) {

            List<Member> members = c.getMembers();
            for (Member m : members) {
                userConnected(m.getUser().getId(), c);
            }

        }

        outInfo("Done scanning!");

    }

    private static String handleMessageCreation(VoiceChannel vc, User user, String message) throws RateLimitedException {

        String channelInfo;
        Invite inv = createInvite(vc, user);

        if (Core.VARS.MESSAGE_COMPACT) {

            channelInfo = user.getAsMention();

            if (Core.VARS.LIST_OTHER_USERS) {

                List<Member> members = vc.getMembers();
                for (Member member : members) {
                    if (member.getUser() != user) {

                        String nick = member.getNickname();
                        if (nick == null) {
                            nick = member.getEffectiveName();
                        }
                        channelInfo += " + " + nick;

                    }
                }

            }

            channelInfo += " in " + vc.getName() + ": " + message + " " + inv.getURL();

        } else {

            channelInfo = "Join " + user.getAsMention();

            if (Core.VARS.LIST_OTHER_USERS) {

                List<Member> members = vc.getMembers();
                for (Member member : members) {
                    if (member.getUser() != user) {

                        String nick = member.getNickname();
                        if (nick == null) {
                            nick = member.getEffectiveName();
                        }
                        channelInfo += " + " + nick;

                    }
                }

            }

            channelInfo += " in " + vc.getName() + " via " + inv.getURL() + "\n" + message;

        }

        return channelInfo;
    }

    public static Invite createInvite(VoiceChannel vc, User user) {
        try {

            List<Invite> invites = vc.getInvites().complete(true);

            if (!invites.isEmpty()) {
                return invites.get(0);
            } else {
                return vc.createInvite().setMaxAge(Core.VARS.INVITE_EXPIRE_SECONDS).reason(user.getId()).complete(true);
            }

        } catch (RateLimitedException ex) {
            Logger.getLogger(LFGHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static VoiceChannel whereIsUser(String arg) {
        VoiceChannel vc = CONNECTED_USERS.getOrDefault(arg, null);
        if (vc != null) {
            return vc;
        } else {
            return null;
        }
    }

}
