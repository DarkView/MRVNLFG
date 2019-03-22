/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 *
 * @author Nils
 */
public class Connector {

    private Connection conn = null;
    private final String dbHost;
    private final String dbPort;
    private final String dbName;
    private final String dbUser;
    private final String dbPassword;
    private final String dbTableName;

    public Connector() {

        Map<String, String> info = Core.VARS.MYSQL_INFO;
        this.dbHost = info.get("dbHost");
        this.dbPort = info.get("dbPort");
        this.dbName = info.get("dbName");
        this.dbUser = info.get("dbUser");
        this.dbPassword = info.get("dbPassword");
        this.dbTableName = info.get("dbTableName");

        connect();

    }

    private void connect() {

        try {

            conn = (Connection) DriverManager.getConnection("jdbc:mysql://" + dbHost + ":"
                    + dbPort + "/" + dbName + "?" + "user=" + dbUser + "&"
                    + "password=" + dbPassword + "&autoReconnect=true&useSSL=false");
            Statement stm = conn.createStatement();
            stm.executeUpdate("TRUNCATE " + dbTableName);

            Core.outInfo("Connection established!");

        } catch (SQLException ex) {
            Core.outError(ex.getMessage(), ex);
            Core.outInfo("Connection could NOT be established! Turning off MySQL mode...");
            Core.VARS.MYSQL_ENABLED = false;
            Core.saveVars();
        }

    }

    private void reconnect() {

        try {

            if (!conn.isValid(250)) {
                connect();
            }

        } catch (SQLException ex) {
            Core.outError(ex.getMessage(), ex);
        }

    }

    public void userConnected(String userID, String channelID) {
        reconnect();
        Statement stm = null;
        try {

            stm = conn.createStatement();
            stm.executeUpdate("INSERT INTO `" + dbTableName + "`"
                    + "(`userID`, `channelID`)"
                    + " VALUES (" + userID + ", " + channelID + ")"
                    + " ON DUPLICATE KEY UPDATE `channelID` = " + channelID);

        } catch (SQLException ex) {
            ex.printStackTrace();
            Core.outError(ex.getMessage(), ex);
        } finally {
            try {
                stm.close();
            } catch (SQLException ex) {
                Core.outError(ex.getMessage(), ex);
            }
        }
    }

    public void userDiconnected(String userID) {
        reconnect();
        Statement stm = null;
        try {

            stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM `" + dbTableName + "`"
                    + " WHERE userID = " + userID);

        } catch (SQLException ex) {
            Core.outError(ex.getMessage(), ex);
        } finally {
            try {
                stm.close();
            } catch (SQLException ex) {
                Core.outError(ex.getMessage(), ex);
            }
        }

    }

    public String getChannel(String userID) {
        Statement stm = null;
        ResultSet rs = null;
        String cID = "999999999999999999";
        try {

            stm = conn.createStatement();
            rs = stm.executeQuery("SELECT `channelID`"
                    + " FROM `" + dbTableName + "`"
                    + " WHERE userID = " + userID);

            if (rs.first()) {
                cID = rs.getString("channelID");
                rs.close();
            }

        } catch (SQLException ex) {
                Core.outError(ex.getMessage(), ex);
                cID = "999999999999999999";
        } finally {
            try {
                stm.close();
            } catch (SQLException ex) {
                Core.outError(ex.getMessage(), ex);
            }
        }
        return cID;
    }

}
