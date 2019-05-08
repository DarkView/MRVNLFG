/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot.Listeners;

import de.darkdl.mrvnbot.Core;
import de.darkdl.mrvnbot.LFGHandler;
import de.darkdl.mrvnbot.commands.CMDWhere;
import de.darkdl.mrvnbot.utils.NotifyInfo;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * VoiceListener with the 3 relevant events for knowing which channel a user is
 * in
 *
 * @author Nils Blömeke
 */
public class VoiceListener extends ListenerAdapter {

    private static ArrayList<NotifyInfo> activeNotifies = new ArrayList<>();
    private static Timer timer = new Timer("MRVN-NotifTimer");

    private static String SEPARATOR = " - ";

    /**
     * Calls for an overwrite/add of the user and his channel if it matches the
     * identifier for channels
     *
     * @param evt - The event passed on by JDA
     */
    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent evt) {

        String userID = evt.getMember().getUser().getId();
        LFGHandler.userConnected(userID, evt.getChannelJoined().getId());
        sendNotifiesForUser(userID);

    }

    /**
     * Calls for an overwrite/add on the users associated channel if his new
     * channel passes the voice identifiert Else, it removes the user and its
     * association from the list as he left the LFG voice channels and we dont
     * allow LFGs there
     *
     * @param evt - The event passed on by JDA
     */
    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent evt) {

        String userID = evt.getMember().getUser().getId();
        LFGHandler.userConnected(userID, evt.getChannelJoined().getId());
        sendNotifiesForUser(userID);

    }

    /**
     * Calls for removal of the user from the Map, even if they were not in the
     * LFG channels anymore
     *
     * @param evt - The event passed on by JDA
     */
    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent evt) {

        LFGHandler.userDisconnected(evt.getMember().getUser().getId());

    }

    public static void addNotify(String userID, User modUser, int expire) {
        NotifyInfo newNotif = new NotifyInfo(modUser, userID, System.currentTimeMillis() + (expire * 1000));
        activeNotifies.add(newNotif);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (removeNotifiesForNotifID(newNotif.getId())) {
                    Core.sendMessageToChannel("The notification for <@!" + userID + "> requested by " + modUser.getName() + " expired.", Core.infoChannel);
                }
            }
        }, expire * 1000);
        timer.purge();

        Core.outLFGInfo(modUser, "Executed notify for " + userID + " for " + expire + " seconds");
    }

    public static void removeNotifiesForMod(User modUser) {
        ArrayList<NotifyInfo> replacement = new ArrayList<>();

        for (NotifyInfo notify : activeNotifies) {
            if (!notify.getModUser().equals(modUser)) {
                replacement.add(notify);
            }
        }
        activeNotifies = replacement;
    }

    public static void removeNotifiesForUserID(String userId) {
        ArrayList<NotifyInfo> replacement = new ArrayList<>();

        for (NotifyInfo notify : activeNotifies) {
            if (!notify.getUserID().equals(userId)) {
                replacement.add(notify);
            }
        }
        activeNotifies = replacement;
    }

    public static boolean removeNotifiesForNotifID(int notifID) {
        ArrayList<NotifyInfo> replacement = new ArrayList<>();
        boolean removed = false;

        for (NotifyInfo notify : activeNotifies) {
            if (notify.getId() != notifID) {
                replacement.add(notify);
            } else {
                removed = true;
            }
        }
        activeNotifies = replacement;
        return removed;
    }

    public static void sendNotifiesForUser(String userID) {
        for (NotifyInfo notif : activeNotifies) {
            if (notif.getUserID().equals(userID)) {
                Core.outInfo("Notification tripped. User: " + userID + " | Moderator: " + notif.getModUser().getId());
                CMDWhere.sendWhere(userID, Core.infoChannel, notif.getModUser(), "<@!" + notif.getModUser().getId() + ">, the user <@!"
                        + userID + "> joined this channel:");
            }
        }
        removeNotifiesForUserID(userID);
    }

    public static String listNotifies() {

        StringBuilder listBuilder = new StringBuilder();

        if (activeNotifies.size() > 0) {

            listBuilder.append("Listing `").append(activeNotifies.size()).append("` notifications:\n");
            listBuilder.append("Notify ID - User ID - Requestor - Time Remaining\n");
            long requestTime = System.currentTimeMillis();

            for (NotifyInfo notify : activeNotifies) {
                long timeLeft = notify.getStartTime() - requestTime;
                listBuilder.append(String.format("%1s" + SEPARATOR, notify.getId()));
                listBuilder.append(String.format("%1s" + SEPARATOR, notify.getUserID()));
                listBuilder.append(String.format("%1s" + SEPARATOR, notify.getModUser().getName()));
                listBuilder.append(timeLeft / 1000);
                listBuilder.append(" seconds\n");
            }

        } else {
            listBuilder.append("There are currently no active notifications.");
        }

        return listBuilder.toString();
    }

}
