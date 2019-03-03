/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot.Listeners;

import de.darkdl.mrvnbot.Core;
import de.darkdl.mrvnbot.LFGHandler;
import de.darkdl.mrvnbot.Vars;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * VoiceListener with the 3 relevant events for knowing which channel a user is in
 * @author Nils Blömeke
 */
public class VoiceListener extends ListenerAdapter {

    /**
     * Calls for an overwrite/add of the user and his channel if it matches the identifier for channels
     * @param evt - The event passed on by JDA
     */
    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent evt) {
        
        User u = evt.getMember().getUser();
        VoiceChannel c = evt.getChannelJoined();

        if (c.getName().toLowerCase().contains(Core.VARS.LFG_VOICE_IDENTIFIER)) {
            LFGHandler.userConnected(u.getId(), c);
        }
        
    }

    /**
     * Calls for an overwrite/add on the users associated channel if his new channel passes the voice identifiert
     * Else, it removes the user and its association from the list as he left the LFG voice channels and we dont allow LFGs there
     * @param evt - The event passed on by JDA
     */
    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent evt) {
        
        User u = evt.getMember().getUser();
        VoiceChannel c = evt.getChannelJoined();

        if (c.getName().toLowerCase().contains(Core.VARS.LFG_VOICE_IDENTIFIER)) {
            LFGHandler.userConnected(u.getId(), c);
        } else {
            LFGHandler.userDisconnected(u.getId());
        }
        
    }

    /**
     * Calls for removal of the user from the Map, even if they were not in the LFG channels anymore
     * @param evt - The event passed on by JDA
     */
    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent evt) {

        LFGHandler.userDisconnected(evt.getMember().getUser().getId());
        
    }

}
