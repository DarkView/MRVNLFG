/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot;

import java.util.List;
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

    public static final long[] MESSAGE_DELAY = new long[5];
    private static int CURRENT_POS = 0;

    public static VoiceChannel getChannelForUserID(String userID) {
        
        Member m = Core.getMemberForID(userID);
        if (m == null) {
            Core.outError("Error retrieving the user for the specified ID " + userID, null);
            return null;
        } 
        
        return m.getVoiceState().getChannel();
        
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

        VoiceChannel vc = getChannelForUserID(user.getId());
        if (vc != null && vc.getName().toLowerCase().contains(Core.VARS.LFG_VOICE_IDENTIFIER)) {

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

                    msgContent = msgContent.replaceFirst("(?i)" + Core.VARS.LFG_COMMAND_IDENTIFIER, "");
                    if (msgContent.length() <= Core.VARS.MESSAGE_CHARACTER_LIMIT && getNewLines(msgContent) <= Core.VARS.MESSAGE_LINE_LIMIT) {

                        try {

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
                        Core.sendMessageToChannel("Sorry, but that message is too long! " + user.getAsMention(), channel);
                        Core.outLFGInfo(user, "Stopped LFG request, message exceeded character or newline limit");
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
     * Handles all of the things we need for creating the message to put in the LFG channel
     * @param vc - The voice chat the requestor is in
     * @param user - The requestors UserID
     * @param message - The message we should post
     * @return The finished message
     * @throws RateLimitedException - If we are exceeding a limit, which should not happen due to the scheduler
     */
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

    /**
     * Gets an invite for the Voice Channel or creates one if none exist
     * @param vc - The Voice Channel for which we want the invite
     * @param user - The User that requested the invite to be sent
     * @return an Invite object for the specified channel
     */
    public static Invite createInvite(VoiceChannel vc, User user) {
        try {

            List<Invite> invites = vc.getInvites().complete(true);

            if (!invites.isEmpty()) {
                return invites.get(0);
            } else {
                return vc.createInvite().setMaxAge(Core.VARS.INVITE_EXPIRE_SECONDS).reason(user.getId()).complete(true);
            }

        } catch (RateLimitedException ex) {
            Core.outError(ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * Retrieves the VoiceChannel the specified user is in
     * @param userID - the ID of the User we want to know the location of
     * @return The VoiceChannel object the user is in 
     */
    public static VoiceChannel whereIsUser(String userID) {
        VoiceChannel vc = getChannelForUserID(userID);
        if (vc != null) {
            return vc;
        } else {
            return null;
        }
    }

    /**
     * Checks how many Newlines a message has
     * @param msgContent - the content of the message we want to check the newlines of
     * @return an int with the amount of newlines
     */
    private static int getNewLines(String msgContent) {
        String[] lines = msgContent.split("\r\n|\r|\n");
        return lines.length;
    }

}
