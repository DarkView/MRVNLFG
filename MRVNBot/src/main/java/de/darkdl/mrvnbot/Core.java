/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot;

import de.darkdl.mrvnbot.utils.FileUtils;
import de.darkdl.mrvnbot.utils.Vars;
import de.darkdl.mrvnbot.listeners.MessageListener;
import de.darkdl.mrvnbot.listeners.VoiceListener;
import de.darkdl.mrvnbot.commands.wordfilter.CMDAddBlocked;
import de.darkdl.mrvnbot.commands.general.CMDDelay;
import de.darkdl.mrvnbot.commands.wordfilter.CMDListBlocked;
import de.darkdl.mrvnbot.commands.moderation.CMDListVars;
import de.darkdl.mrvnbot.commands.moderation.CMDMessage;
import de.darkdl.mrvnbot.commands.moderation.CMDNotify;
import de.darkdl.mrvnbot.commands.general.CMDReload;
import de.darkdl.mrvnbot.commands.wordfilter.CMDRemoveBlocked;
import de.darkdl.mrvnbot.commands.moderation.CMDUpdateVar;
import de.darkdl.mrvnbot.commands.general.CMDUptime;
import de.darkdl.mrvnbot.commands.general.CMDVersion;
import de.darkdl.mrvnbot.commands.moderation.CMDWhere;
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
import net.dv8tion.jda.core.entities.Member;
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
    public static TextChannel infoChannel = null;
    private static List<String> BLOCKED_WORDS;
    private static final Logger LOGGER = LoggerFactory.getLogger(Core.class);
    public static String VERSION = "1.8-beta2";
    public static long bootTime;

    private static MRVNMessage currentMessage;

    public static void main(String[] args) throws LoginException, InterruptedException {
        outInfo("Starting up " + VERSION + "...");
        bootTime = System.currentTimeMillis();

        VARS = FileUtils.deserializeVars();
        VARS.allToLowerCase();
        outInfo("Loaded the config");

        BLOCKED_WORDS = FileUtils.deserializeBlocked();
        outInfo("Loaded blocked words");

        if (VARS.TOKEN.equals("") || VARS.TOKEN == null) {
            outInfo("There is no token in the settings.json! Stopping...");
            System.exit(0);
        }

        builder = new JDABuilder(AccountType.BOT);

        builder.setToken(VARS.TOKEN);
        builder.setAutoReconnect(true);
        builder.setContextEnabled(true);
        builder.setGame(Game.playing(VARS.LFG_COMMAND_IDENTIFIER));

        bot = builder.build().awaitReady();
        if (!VARS.INFO_CHANNEL_ID.equals("")) {
            infoChannel = bot.getTextChannelById(VARS.INFO_CHANNEL_ID);
        }

        addListeners();
        addCommands();

        outInfo("Done loading! [Took " + (System.currentTimeMillis() - bootTime) + "ms]");
        java.lang.System.gc();
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
        CommandHandler.commands.put("uptime", new CMDUptime());
        CommandHandler.commands.put("updatevar", new CMDUpdateVar());
        CommandHandler.commands.put("listvars", new CMDListVars());
        CommandHandler.commands.put("addblocked", new CMDAddBlocked());
        CommandHandler.commands.put("removeblocked", new CMDRemoveBlocked());
        CommandHandler.commands.put("listblocked", new CMDListBlocked());
        CommandHandler.commands.put("delay", new CMDDelay());
        CommandHandler.commands.put("message", new CMDMessage());
        CommandHandler.commands.put("where", new CMDWhere());
        CommandHandler.commands.put("w", new CMDWhere());
        CommandHandler.commands.put("notify", new CMDNotify());
        CommandHandler.commands.put("notif", new CMDNotify());
        CommandHandler.commands.put("n", new CMDNotify());
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

    /**
     * Loads the .json settings file and properly imports it
     */
    public static void updateVars() {
        VARS = FileUtils.deserializeVars();
        VARS.allToLowerCase();
    }

    /**
     * Used to create the settings.json file (if it doesnt exit)
     */
    public static void createVarsFile() {
        VARS = new Vars();
        FileUtils.serializeVars(VARS);
    }

    /**
     * Used to save changes made to the settings to the .json
     */
    public static void saveVars() {
        FileUtils.serializeVars(VARS);
    }

    /**
     * Retrieves the currently loaded list of blocked words/regexs
     *
     * @return a List with all the blocke words/regexes
     */
    public static List<String> getBlockedWords() {
        return BLOCKED_WORDS;
    }

    /**
     * Adds a blocked word/regex to the filter and saves it
     *
     * @param regex - the (optionally regex) term to add to the block list
     */
    public static void addBlockedWord(String regex) {
        BLOCKED_WORDS.add(regex);
        saveBlocked();
    }

    /**
     * Removes a blocked word/regex from the filter and saves it
     *
     * @param toRemove - the exact term/word/regex to remove, no deviance
     * permitted
     * @return true if removed successfully
     */
    public static boolean removeBlockedWord(String toRemove) {
        boolean success = BLOCKED_WORDS.remove(toRemove);
        saveBlocked();
        return success;
    }

    /**
     * Reloads the blocked word list from the blocked words file
     */
    public static void updateBlocked() {
        BLOCKED_WORDS = FileUtils.deserializeBlocked();
    }

    /**
     * Creates the blocked words file if it doesnt exist With
     * "(?i)n[il1]gg(er|a|@)" as a default regex
     */
    public static void createBlockedFile() {
        BLOCKED_WORDS = new ArrayList<>();
        BLOCKED_WORDS.add("(?i)n[il1]gg(er|a|@)");
        FileUtils.serializeBlocked(BLOCKED_WORDS);
    }

    /**
     * Saves the blocked words to the blocked words file
     */
    public static void saveBlocked() {
        FileUtils.serializeBlocked(BLOCKED_WORDS);
    }

    /**
     * Gets the delay for the bots last hearbeat
     *
     * @return - the delay of the bots last heartbeat
     */
    public static long getPing() {
        return bot.getPing();
    }

    /**
     * Queries githubs API to retreive the latest (non-pre-release) version
     * number
     *
     * @return - a String with the latest version number
     */
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

    /**
     * Creates a new MRVN Message
     *
     * @param title - The messages title (filename)
     */
    public static void newMRVNMessage(String title) {
        currentMessage = new MRVNMessage();
        currentMessage.setTitle(title);
    }

    /**
     * Loads an existing MRVN Message
     *
     * @param toLoad - The title of the message to load
     */
    public static void loadMRVNMessage(String toLoad) {
        currentMessage = FileUtils.deserializeMRVNMessage(toLoad);
    }

    /**
     * Sets (overrides) the channels the message will be posted to Putting a "c"
     * in front of the ID tells the bot to resolve a category ID to channel IDs
     *
     * @param args - The channel IDs of the channels to send to
     */
    public static void setMRVNMessageChannels(String[] args) {
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

    /**
     * Adds (does NOT override) channels the message will be posted to
     *
     * @param args - the channels or categorys to add
     */
    public static void addMRVNMessageChannels(String[] args) {
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

    /**
     * Sets the message that will be posted
     *
     * @param args - The message
     */
    public static void setMRVNMessageMessage(String[] args) {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < args.length; i++) {
            sb.append(args[i] + " ");
        }

        currentMessage.setMessage(sb.toString());
    }

    /**
     * Posts or edit the message to all channels that have been set
     */
    public static void postMRVNMessageMessage() {
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

    /**
     * Deletes the message from all channels
     */
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

    /**
     * Saves the message to its file
     */
    public static void saveMRVNMessage() {
        FileUtils.serializeMRVNMessage(currentMessage);
    }

    /**
     * Displays all the information of the current message
     *
     * @param channel - the channel to post it to
     */
    public static void postMRVNMessageInfo(MessageChannel channel) {
        sendMessageToChannel(currentMessage.toString(), channel);
    }

    /**
     * Sets whether or not the message should be pinned
     *
     * @param b - true if yes
     * @return param b
     */
    public static boolean setMRVNMessagePin(boolean b) {
        currentMessage.setPin(b);
        return b;
    }

    /**
     * Unloads the message to prevent accidental edits
     */
    public static void unloadMRVNMessage() {
        currentMessage = new MRVNMessage();
    }

    /**
     * Returns the Member object for the specified userID (if we have a mutual
     * guild)
     *
     * @param userID - The userID we want to get the Member object of
     * @return the member object
     */
    public static Member getMemberForID(String userID) {
        if (userID.matches("^[0-9]+$")) {
            return bot.getUserById(userID).getMutualGuilds().get(0).getMemberById(userID);
        } else {
            return null;
        }
    }

}
