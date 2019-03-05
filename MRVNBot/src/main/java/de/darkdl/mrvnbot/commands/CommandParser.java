/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot.commands;

import de.darkdl.mrvnbot.Core;
import java.util.ArrayList;
import java.util.Arrays;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Nils
 */
public class CommandParser {
    
    public static commandContainer parser(String raw, MessageReceivedEvent evt) {
        
        String beheaded = raw.replaceFirst("\\" + Core.VARS.CMD_PREFIX, "");
        String[] splitBeheaded = beheaded.split(" ");
        String invoke = splitBeheaded[0].toLowerCase();
        
        ArrayList<String> split = new ArrayList<>();
        split.addAll(Arrays.asList(splitBeheaded));
        
        String[] args = new String[split.size() - 1];
        split.subList(1, split.size()).toArray(args);
        
        return new commandContainer(raw, beheaded, splitBeheaded, invoke, args, evt);
    }

    public static class commandContainer {

        public final String raw;
        public final String beheaded;
        public final String[] splitBeheaded;
        public final String invoke;
        public final String[] args;
        public final MessageReceivedEvent evt;

        public commandContainer(String raw, String beheaded, String[] splitBeheaded, String invoke, String[] args, MessageReceivedEvent evt) {
            this.raw = raw;
            this.beheaded = beheaded;
            this.splitBeheaded = splitBeheaded;
            this.invoke = invoke;
            this.args = args;
            this.evt = evt;
        }
        
    }

}
