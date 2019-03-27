/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot.utils;

import de.darkdl.mrvnbot.Core;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public String INFO_CHANNEL_ID = "";
    public int INVITE_EXPIRE_SECONDS = 300;
    public int MESSAGE_LINE_LIMIT = 5;
    public int MESSAGE_CHARACTER_LIMIT = 150;
    public boolean LIST_OTHER_USERS = true;
    public boolean MESSAGE_COMPACT = false;
    public boolean MYSQL_ENABLED = false;
    public Map<String, String> MYSQL_INFO;

    public void allToLowerCase() {
        LFG_VOICE_IDENTIFIER = LFG_VOICE_IDENTIFIER.toLowerCase();
        LFG_TEXT_IDENTIFIER = LFG_TEXT_IDENTIFIER.toLowerCase();
        LFG_COMMAND_IDENTIFIER = LFG_COMMAND_IDENTIFIER.toLowerCase();
    }

    public void initSQL() {

        if (!MYSQL_INFO.containsKey("dbHost")) {
            
            Map<String, String> map = new HashMap<>();
            map.put("dbHost", "mysql-db-url");
            map.put("dbPort", "3306");
            map.put("dbName", "mysql-db-name");
            map.put("dbUser", "mysql-user-name");
            map.put("dbPassword", "mysql-db-pw");
            map.put("dbTableName", "mysql-table-name");
            MYSQL_INFO = map;
            Core.saveVars();
            
        }

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
