/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot.commands.general;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Nils Blömeke
 */
public interface Command {
    
    void called(String[] args, MessageReceivedEvent evt);
    
}