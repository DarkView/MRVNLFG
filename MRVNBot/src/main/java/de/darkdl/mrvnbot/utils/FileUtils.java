/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.darkdl.mrvnbot.Core;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Nils
 */
public class FileUtils {

    private static final File VARS_FILE = new File("settings.json");
    private static final File BLOCKED_FILE = new File("blocked-words.json");

    /**
     * Serializes/saves the variables (settings) to a file
     * @param v - The Vars object we want to save
     */
    public static void serializeVars(Vars v) {

        ObjectMapper mapper = new ObjectMapper();

        try {

            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("settings.json"), v);

        } catch (IOException e) {
            Core.outError(e.getMessage(), e);
        }

    }

    /**
     * Deserializes/loads the settings file
     * @return the Vars object we loaded
     */
    public static Vars deserializeVars() {

        ObjectMapper mapper = new ObjectMapper();

        try {
            if (!VARS_FILE.exists()) {
                Core.outInfo("Creating the " + VARS_FILE.getName() + " file as it does not exist!");
                Core.createVarsFile();
            }
            return mapper.readValue(new File("settings.json"), Vars.class);
        } catch (IOException ex) {
            Core.outError(ex.getMessage(), ex);
        }

        return null;
    }

    /**
     * Serializes/saves the blocked words to a file
     * @param l - the list of blocked words we want to save
     */
    public static void serializeBlocked(List<String> l) {

        ObjectMapper mapper = new ObjectMapper();

        try {

            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("blocked-words.json"), l);

        } catch (IOException e) {
            Core.outError(e.getMessage(), e);
        }

    }

    /**
     * Deserializes/loads the blocked words file
     * @return a List representation of the blocked words we loaded
     */
    public static List<String> deserializeBlocked() {

        ObjectMapper mapper = new ObjectMapper();

        try {
            if (!BLOCKED_FILE.exists()) {
                Core.outInfo("Creating the " + BLOCKED_FILE.getName() + " file as it does not exist!");
                Core.createBlockedFile();
            }
            TypeReference<List<String>> listReference = new TypeReference<List<String>>() {};
            return mapper.readValue(new File("blocked-words.json"), listReference);
        } catch (IOException ex) {
            Core.outError(ex.getMessage(), ex);
        }

        return null;
    }
    
    /**
     * Serializes/saves a specific mrvn message to a file
     * @param msg - the MRVN Message Object we want to save
     * @return true if we succeeded in saving
     */
    public static boolean serializeMRVNMessage(MRVNMessage msg) {

        ObjectMapper mapper = new ObjectMapper();

        try {

            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(msg.getTitle().concat("-message.json")), msg);
            return true;

        } catch (IOException e) {
            Core.outError(e.getMessage(), e);
        }

        return false;
    }

    /**
     * Deserializes/loads a specific mrvn message file
     * @param messageName - the name of the mrvn message we want to load
     * @return the MRVNMessage object we loaded
     */
    public static MRVNMessage deserializeMRVNMessage(String messageName) {

        ObjectMapper mapper = new ObjectMapper();
        File messageFile = new File(messageName.concat("-message.json"));

        try {
            return mapper.readValue(messageFile, MRVNMessage.class);
        } catch (IOException ex) {
            Core.outError(ex.getMessage(), ex);
        }

        return null;
    }

}
