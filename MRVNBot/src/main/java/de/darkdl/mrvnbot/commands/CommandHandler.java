/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot.commands;

import java.util.HashMap;

/**
 *
 * @author Nils
 */
public class CommandHandler {
    
    public static final CommandParser parse = new CommandParser();
    public static HashMap<String, Command> commands = new HashMap<>();
    
    public static void handleCommand(CommandParser.commandContainer cmd) {
        
        if (commands.containsKey(cmd.invoke)) {
            commands.get(cmd.invoke).called(cmd.args, cmd.evt);
        }
        
    }
    
}
