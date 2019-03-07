/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot;

import de.darkdl.mrvnbot.Listeners.MessageListener;
import de.darkdl.mrvnbot.Listeners.VoiceListener;
import de.darkdl.mrvnbot.commands.CMDListVars;
import de.darkdl.mrvnbot.commands.CMDReload;
import de.darkdl.mrvnbot.commands.CMDUpdateVar;
import de.darkdl.mrvnbot.commands.CommandHandler;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nils Blömeke
 */
public class Core {

    private static JDABuilder builder;
    private static JDA bot;
    public static Vars VARS;
    private static final Logger LOGGER = LoggerFactory.getLogger(Core.class);
    public static String VERSION = "1.2.2";

    public static void main(String[] args) throws LoginException, InterruptedException {
        outInfo("Starting up...");
        long startTime = System.currentTimeMillis();
        VARS = VarsJSON.deserialize();
        VARS.allToLowerCase();
        outInfo("Loaded the config");

        if (VARS.TOKEN.equals("") || VARS.TOKEN == null) {
            outInfo("There is no token in the settings.json! Stopping...");
            System.exit(0);
        }
        
        builder = new JDABuilder(AccountType.BOT);

        builder.setToken(VARS.TOKEN);
        builder.setAutoReconnect(true);
        builder.setContextEnabled(true);
        builder.setGame(Game.playing(VARS.LFG_COMMAND_IDENTIFIER));

        bot = builder.buildBlocking();
        outInfo("Scanning all voice-channels known...");
        LFGHandler.loadVoiceChannels(bot.getVoiceChannels());
        outInfo("Done scanning!");
        addListeners();
        addCommands();

        outInfo("Done loading! [Took " + (System.currentTimeMillis() - startTime) + "ms]");
    }

    /**
     * Used to add the listeners to the running bot
     */
    private static void addListeners() {
        bot.addEventListener(new MessageListener());
        bot.addEventListener(new VoiceListener());
    }
    
    /**
     * Used to add the commands to the running bot
     */
    private static void addCommands() {
        CommandHandler.commands.put("reload", new CMDReload());
        CommandHandler.commands.put("version", new CMDReload());
        CommandHandler.commands.put("updatevar", new CMDUpdateVar());
        CommandHandler.commands.put("listvars", new CMDListVars());
    }
    
    /**
     * Sends a public message to the channel
     * @param msg - The message to send
     * @param channel  - The channel in which to send the message
     */
    public static void sendMessageToChannel(String msg, MessageChannel channel) {
        channel.sendMessage(msg).queue();
    }
    
    /**
     * Deletes a specific {@link net.dv8tion.jda.core.entities.Message Message}
     * @param msg - The Message to be deleted
     */
    public static void deleteMessage(Message msg) {
        msg.delete().queue();
    }

    /**
     * Uses the log4j binding to output a message and the Exceptions stacktrace if an Exception is present
     * @param message - The message to be logged
     * @param ex - The Exception to be traced. Can be null
     */
    public static void outError(String message, Exception ex) {
        if (ex == null) {
            LOGGER.error(message);
        } else {
            LOGGER.trace(message, ex);
        }
    }
    
    /**
     * Outputs a message through the log4j binding
     * @param message - The message to be logged
     */
    public static void outInfo(String message) {
        LOGGER.info(message);
    }
    
    /**
     * Logs a specific message accompanied by the users name and snowflake
     * @param u - The user to be logged alongside the message, in this case the one that executed the request
     * @param message - The message to log
     */
    public static void outLFGInfo(User u, String message) {
        LOGGER.info(userInfo(u) + " | " + message);
    }

    /**
     * Formats a users information for logging
     * @param u - The user for which we want the formatted information
     * @return - String - The formatted information
     */
    public static String userInfo(User u) {
        String uInf;
        
        uInf = "" + u.getName() + "#" + u.getDiscriminator() + " (" + u.getId() + ")";
        
        return uInf;
    }
    
    public static void updateVars() {
        VARS = VarsJSON.deserialize();
        VARS.allToLowerCase();
    }

    static void createVarsFile() {
        VARS = new Vars();
        VarsJSON.serialize(VARS);
    }

    public static void saveVars() {
        VarsJSON.serialize(VARS);
    }
    
}
