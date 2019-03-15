/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot;

import java.util.List;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

/**
 * Storage for important variables, such as the token
 *
 * @author Nils Blömeke
 */
public class Vars {

    public List<String> OWNER_IDS;
    public String MOD_ROLE_ID;
    public String CMD_PREFIX = "";
    public String TOKEN = "";
    public String LFG_VOICE_IDENTIFIER = "";
    public String LFG_TEXT_IDENTIFIER = "";
    public String LFG_COMMAND_IDENTIFIER = "";
    public int INVITE_EXPIRE_SECONDS = 300;
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

    public String firstOwner() {
        return OWNER_IDS.get(0);
    }

    public boolean isMod(Member m) {
        List<Role> roles = m.getRoles();
        for (Role role : roles) {
            if (role.getId().equals(MOD_ROLE_ID)) {
                return true;
            }
        }
        return isOwner(m.getUser().getId());
    }

}
