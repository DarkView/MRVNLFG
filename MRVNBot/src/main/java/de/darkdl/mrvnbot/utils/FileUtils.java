/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public static void serializeVars(Vars v) {

        ObjectMapper mapper = new ObjectMapper();

        try {

            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("settings.json"), v);

        } catch (IOException e) {
            Core.outError(e.getMessage(), e);
        }

    }

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

    public static void serializeBlocked(List<String> l) {

        ObjectMapper mapper = new ObjectMapper();

        try {

            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("blocked-words.json"), l);

        } catch (IOException e) {
            Core.outError(e.getMessage(), e);
        }

    }

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

}
