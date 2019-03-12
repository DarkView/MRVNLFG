/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot;

import java.util.List;

/**
 * Storage for important variables, such as the token
 * @author Nils Blömeke
 */
public class Vars {

    public List<String> OWNER_IDS;
    public String CMD_PREFIX = "";
    public String TOKEN = "";
    public String LFG_VOICE_IDENTIFIER = "";
    public String LFG_TEXT_IDENTIFIER = "";
    public String LFG_COMMAND_IDENTIFIER = "";
    public boolean LIST_OTHER_USERS = true;
    public boolean MESSAGE_COMPACT = false;
    
    public void allToLowerCase() {
        LFG_VOICE_IDENTIFIER = LFG_VOICE_IDENTIFIER.toLowerCase();
        LFG_TEXT_IDENTIFIER = LFG_TEXT_IDENTIFIER.toLowerCase();
        LFG_COMMAND_IDENTIFIER = LFG_COMMAND_IDENTIFIER.toLowerCase();
    }
    
    public boolean isOwner(String userID) {
        return OWNER_IDS.contains(userID);
    }
    
}
