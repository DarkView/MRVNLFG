/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot;

import de.darkdl.mrvnbot.utils.FileUtils;
import de.darkdl.mrvnbot.utils.Vars;
import de.darkdl.mrvnbot.Listeners.MessageListener;
import de.darkdl.mrvnbot.Listeners.VoiceListener;
import de.darkdl.mrvnbot.commands.CMDAddBlocked;
import de.darkdl.mrvnbot.commands.CMDDelay;
import de.darkdl.mrvnbot.commands.CMDListBlocked;
import de.darkdl.mrvnbot.commands.CMDListVars;
import de.darkdl.mrvnbot.commands.CMDMessage;
import de.darkdl.mrvnbot.commands.CMDReload;
import de.darkdl.mrvnbot.commands.CMDRemoveBlocked;
import de.darkdl.mrvnbot.commands.CMDUpdateVar;
import de.darkdl.mrvnbot.commands.CMDVersion;
import de.darkdl.mrvnbot.commands.CMDWhere;
import de.darkdl.mrvnbot.utils.CommandHandler;
import de.darkdl.mrvnbot.utils.MRVNMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nils Blömeke
 */
public class Core {

    private static JDABuilder builder;
    public static JDA bot;
    public static Vars VARS;
    private static Connector conn = null;
    private static TextChannel infoChannel = null;
    private static List<String> BLOCKED_WORDS;
    private static final Logger LOGGER = LoggerFactory.getLogger(Core.class);
    public static String VERSION = "1.6";

    private static MRVNMessage currentMessage;

    public static void main(String[] args) throws LoginException, InterruptedException {
        outInfo("Starting up " + VERSION + "...");
        long startTime = System.currentTimeMillis();

        VARS = FileUtils.deserializeVars();
        VARS.allToLowerCase();
        VARS.initSQL();
        outInfo("Loaded the config");

        BLOCKED_WORDS = FileUtils.deserializeBlocked();
        outInfo("Loaded blocked words");

        if (VARS.TOKEN.equals("") || VARS.TOKEN == null) {
            outInfo("There is no token in the settings.json! Stopping...");
            System.exit(0);
        }
        
        if (VARS.MYSQL_ENABLED) {
            outInfo("We are in MySQL mode, connecting...");
            conn = new Connector();
        }
        
        builder = new JDABuilder(AccountType.BOT);

        builder.setToken(VARS.TOKEN);
        builder.setAutoReconnect(true);
        builder.setContextEnabled(true);
        builder.setGame(Game.playing(VARS.LFG_COMMAND_IDENTIFIER));

        outInfo("Scanning all voice-channels known...");
        bot = builder.buildBlocking();
        
        if (!VARS.INFO_CHANNEL_ID.equals("")) {
            infoChannel = bot.getTextChannelById(VARS.INFO_CHANNEL_ID);
        }
        outInfoChannel("Loading... Scanning all voice-channels");
        
        LFGHandler.loadVoiceChannels();
        addListeners();
        addCommands();

        outInfo("Done loading! [Took " + (System.currentTimeMillis() - startTime) + "ms]");
        outInfoChannel("Done loading!");
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
        CommandHandler.commands.put("version", new CMDVersion());
        CommandHandler.commands.put("updatevar", new CMDUpdateVar());
        CommandHandler.commands.put("listvars", new CMDListVars());
        CommandHandler.commands.put("addblocked", new CMDAddBlocked());
        CommandHandler.commands.put("removeblocked", new CMDRemoveBlocked());
        CommandHandler.commands.put("listblocked", new CMDListBlocked());
        CommandHandler.commands.put("delay", new CMDDelay());
        CommandHandler.commands.put("message", new CMDMessage());
        CommandHandler.commands.put("where", new CMDWhere());
    }

    /**
     * Sends a public message to the channel
     *
     * @param msg - The message to send
     * @param channel - The channel in which to send the message
     */
    public static void sendMessageToChannel(String msg, MessageChannel channel) {
        channel.sendMessage(msg).queue();
    }

    /**
     * Sends a public message to the channel
     *
     * @param embed - The embed to send
     * @param channel - The channel in which to send the embed
     */
    public static void sendMessageToChannel(MessageEmbed embed, MessageChannel channel) {
        channel.sendMessage(embed).queue();
    }

    /**
     * Deletes a specific {@link net.dv8tion.jda.core.entities.Message Message}
     *
     * @param msg - The Message to be deleted
     */
    public static void deleteMessage(Message msg) {
        msg.delete().queue();
    }

    /**
     * Uses the log4j binding to output a message and the Exceptions stacktrace
     * if an Exception is present
     *
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
     *
     * @param message - The message to be logged
     */
    public static void outInfo(String message) {
        LOGGER.info(message);
    }

    /**
     * Logs a specific message accompanied by the users name and snowflake
     *
     * @param u - The user to be logged alongside the message, in this case the
     * one that executed the request
     * @param message - The message to log
     */
    public static void outLFGInfo(User u, String message) {
        LOGGER.info(userInfo(u) + " | " + message);
    }

    /**
     * Formats a users information for logging
     *
     * @param u - The user for which we want the formatted information
     * @return - String - The formatted information
     */
    public static String userInfo(User u) {
        String uInf;

        uInf = "" + u.getName() + "#" + u.getDiscriminator() + " (" + u.getId() + ")";

        return uInf;
    }

    public static void updateVars() {
        VARS = FileUtils.deserializeVars();
        VARS.allToLowerCase();
    }

    public static void createVarsFile() {
        VARS = new Vars();
        FileUtils.serializeVars(VARS);
    }

    public static void saveVars() {
        FileUtils.serializeVars(VARS);
    }

    public static List<String> getBlockedWords() {
        return BLOCKED_WORDS;
    }

    public static void addBlockedWord(String regex) {
        BLOCKED_WORDS.add(regex);
        saveBlocked();
    }

    public static boolean removeBlockedWord(String toRemove) {
        boolean success = BLOCKED_WORDS.remove(toRemove);
        saveBlocked();
        return success;
    }

    public static void updateBlocked() {
        BLOCKED_WORDS = FileUtils.deserializeBlocked();
    }

    public static void createBlockedFile() {
        BLOCKED_WORDS = new ArrayList<>();
        BLOCKED_WORDS.add("(?i)n[il1]gg(er|a|@)");
        FileUtils.serializeBlocked(BLOCKED_WORDS);
    }

    public static void saveBlocked() {
        FileUtils.serializeBlocked(BLOCKED_WORDS);
    }

    public static long getPing() {
        return bot.getPing();
    }

    public static String getLatestStableVersion() {
        try {

            String url = "https://api.github.com/repos/DarkView/MRVNLFG/releases/latest";
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            String response = client.newCall(request).execute().body().string();
            JSONObject jObjects = new JSONObject(response);

            return jObjects.getString("tag_name");

        } catch (Exception ex) {
            Core.outError(ex.getMessage(), ex);
        }

        return "Error";
    }

    public static void newMessage(String title) {
        currentMessage = new MRVNMessage();
        currentMessage.setTitle(title);
    }

    public static void loadMessage(String toLoad) {
        currentMessage = FileUtils.deserializeMRVNMessage(toLoad);
    }

    public static void setChannels(String[] args) {
        Map<String, String> channels = new HashMap<>();

        for (int i = 1; i < args.length; i++) {
            String current = args[i];
            if (current.startsWith("c")) {
                current = current.replaceAll("[^0-9]", "");
                List<Channel> catChannels = bot.getCategoryById(current).getChannels();
                for (Channel catChannel : catChannels) {
                    channels.putIfAbsent(catChannel.getId(), "");
                }
            } else {
                channels.putIfAbsent(args[i], "");
            }
        }

        currentMessage.setChannelAndMessageIDs(channels);
    }

    public static void addChannels(String[] args) {
        Map<String, String> channels = currentMessage.getChannelAndMessageIDs();

        for (int i = 1; i < args.length; i++) {
            String current = args[i];
            if (current.startsWith("c")) {
                current = current.replaceAll("[^0-9]", "");
                List<Channel> catChannels = bot.getCategoryById(current).getChannels();
                for (Channel catChannel : catChannels) {
                    channels.putIfAbsent(catChannel.getId(), "");
                }
            } else {
                channels.putIfAbsent(args[i], "");
            }
        }

        currentMessage.setChannelAndMessageIDs(channels);
    }

    public static void setMessage(String[] args) {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < args.length; i++) {
            sb.append(args[i] + " ");
        }

        currentMessage.setMessage(sb.toString());
    }

    public static void postMessage() {
        Map<String, String> channelAndMessageIDs = currentMessage.getChannelAndMessageIDs();
        Set<String> keySet = channelAndMessageIDs.keySet();
        boolean toPin = currentMessage.isPin();

        for (String key : keySet) {

            String messageID = channelAndMessageIDs.get(key);
            if (!messageID.equals("")) {

                Message msg = bot.getTextChannelById(key).getMessageById(messageID).complete();
                msg.editMessage(currentMessage.getMessage()).queue();

                if (toPin) {
                    if (!msg.isPinned()) {
                        msg.pin().queue();
                    }
                } else {
                    if (msg.isPinned()) {
                        msg.pin().queue();
                    }
                }

            } else {

                Message msg = bot.getTextChannelById(key).sendMessage(currentMessage.getMessage()).complete();
                if (toPin) {
                    msg.pin().queue();
                }

                channelAndMessageIDs.put(key, msg.getId());
            }
        }
    }

    public static void deleteMrvnMessage() {
        Map<String, String> channelAndMessageIDs = currentMessage.getChannelAndMessageIDs();
        Map<String, String> newIDs = new HashMap<>();
        Set<String> keySet = channelAndMessageIDs.keySet();

        for (String key : keySet) {
            bot.getTextChannelById(key).deleteMessageById(channelAndMessageIDs.get(key)).queue();
            newIDs.put(key, "");
        }

        currentMessage.setChannelAndMessageIDs(newIDs);
    }

    public static void saveMessage() {
        FileUtils.serializeMRVNMessage(currentMessage);
    }

    public static void postMRVNMessageInfo(MessageChannel channel) {
        sendMessageToChannel(currentMessage.toString(), channel);
    }
    
    public static void outInfoChannel(String s) {
        //if (infoChannel != null) {
        //    infoChannel.sendMessage(s).queue();
        //}
    }

    public static void setPin(boolean b) {
        currentMessage.setPin(b);
    }

    public static void unloadMessage() {
        currentMessage = new MRVNMessage();
    }

    public static void dbUserConnected(String userID, String channelID) {
        conn.userConnected(userID, channelID);
    }

    public static void dbUserDisconnected(String userID) {
        conn.userDiconnected(userID);
    }

    static String dbGetChannel(String userID) {
        return conn.getChannel(userID);
    }

}
