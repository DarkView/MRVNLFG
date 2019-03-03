/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot;

import de.darkdl.mrvnbot.Listeners.MessageListener;
import de.darkdl.mrvnbot.Listeners.VoiceListener;
import java.time.LocalTime;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nils
 */
public class Core {

    private static JDABuilder builder;
    private static JDA bot;
    private static final Logger LOGGER = LoggerFactory.getLogger(Core.class);

    public static void main(String[] args) throws LoginException, InterruptedException {
        outInfo("Starting up... [" + LocalTime.now().toString() + "]");

        builder = new JDABuilder(AccountType.BOT);

        builder.setToken(Vars.TOKEN);
        builder.setAutoReconnect(true);
        builder.setContextEnabled(true);
        builder.setGame(Game.playing("!lfg"));

        bot = builder.buildBlocking();
        LFGHandler.loadVoiceChannels(bot.getVoiceChannels());
        addListeners();

        outInfo("Done loading! [" + LocalTime.now().toString() + "]");
    }

    private static void addListeners() {
        bot.addEventListener(new MessageListener());
        bot.addEventListener(new VoiceListener());
    }
    
    public static void sendMessageToChannel(String msg, MessageChannel channel) {
        channel.sendMessage(msg).queue();
    }
    
    public static void deleteMessage(Message msg) {
        msg.delete().queue();
    }

    public static void outError(String message, RateLimitedException ex) {
        if (ex == null) {
            LOGGER.error(message);
        } else {
            LOGGER.trace(message, ex);
        }
    }
    
    public static void outInfo(String message) {
        LOGGER.info(message);
    }
    
    public static void outLFGInfo(User u, String message) {
        LOGGER.info(userInfo(u) + " | " + message);
    }

    public static String userInfo(User u) {
        String uInf;
        
        uInf = "" + u.getName() + "#" + u.getDiscriminator() + " (" + u.getId() + ")";
        
        return uInf;
    }
    
}
