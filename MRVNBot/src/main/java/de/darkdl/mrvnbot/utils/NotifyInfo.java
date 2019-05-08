/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot.utils;

import net.dv8tion.jda.core.entities.User;

/**
 *
 * @author Nils
 */
public class NotifyInfo {
    
    private User modUser;
    private String userID;
    private long endTime;
    
    private int id;
    private static int nextId;

    public NotifyInfo(User modUser, String userID, long endTime) {
        this.modUser = modUser;
        this.userID = userID;
        this.endTime = endTime;
        
        this.id = nextId;
        nextId++;
    }

    public User getModUser() {
        return modUser;
    }

    public String getUserID() {
        return userID;
    }

    public int getId() {
        return id;
    }

    public long getStartTime() {
        return endTime;
    }
    
}
