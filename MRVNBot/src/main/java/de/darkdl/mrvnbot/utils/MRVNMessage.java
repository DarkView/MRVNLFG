/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot.utils;

import java.util.Map;

/**
 *
 * @author Nils
 */
public class MRVNMessage {
    
    private String title = "";
    private boolean pin = false;
    private String message = "";
    private Map<String, String> channelAndMessageIDs;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getChannelAndMessageIDs() {
        return channelAndMessageIDs;
    }

    public void setChannelAndMessageIDs(Map<String, String> channelsAndMessageID) {
        this.channelAndMessageIDs = channelsAndMessageID;
    }

    public boolean isPin() {
        return pin;
    }

    public void setPin(boolean pin) {
        this.pin = pin;
    }

    @Override
    public String toString() {
        return "**Title:** `" + title + "`\n**Message:** ```" + message + "```\n**Channel|Message IDs:** ```" + channelAndMessageIDs + "```";
    }
    
}
