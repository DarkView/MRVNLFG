/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot.Listeners;

import de.darkdl.mrvnbot.LFGHandler;
import de.darkdl.mrvnbot.Vars;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 *
 * @author Nils
 */
public class VoiceListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent evt) {
        
        User u = evt.getMember().getUser();
        VoiceChannel c = evt.getChannelJoined();

        if (c.getName().toLowerCase().contains(Vars.TEAM_VOICE_IDENTIFIER)) {
            LFGHandler.userConnected(u.getId(), c);
        }
        
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent evt) {
        
        User u = evt.getMember().getUser();
        VoiceChannel c = evt.getChannelJoined();

        if (c.getName().toLowerCase().contains(Vars.TEAM_VOICE_IDENTIFIER)) {
            LFGHandler.userConnected(u.getId(), c);
        } else {
            LFGHandler.userDisconnected(u.getId());
        }
        
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent evt) {

        LFGHandler.userDisconnected(evt.getMember().getUser().getId());
        
    }

}
