/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot;

/**
 * Storage for important variables, such as the token
 * @author Nils Blömeke
 */
public class Vars {

    public String OWNER_ID = "";
    public String TOKEN = "";
    public String LFG_VOICE_IDENTIFIER = "";
    public String LFG_TEXT_IDENTIFIER = ""; //These variables must be lowercase since we convert the Strings to lowercase for the checks
    public String COMMAND_IDENTIFIER = "";
    
    public void allToLowerCase() {
        OWNER_ID = OWNER_ID.toLowerCase();
        TOKEN = TOKEN.toLowerCase();
        LFG_VOICE_IDENTIFIER = LFG_VOICE_IDENTIFIER.toLowerCase();
        LFG_TEXT_IDENTIFIER = LFG_TEXT_IDENTIFIER.toLowerCase();
        COMMAND_IDENTIFIER = COMMAND_IDENTIFIER.toLowerCase();
    }
    
}
