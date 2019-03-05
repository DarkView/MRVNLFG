/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Nils
 */
public class VarsJSON {

    private static final File FILE = new File("settings.json");

    public static void serialize(Vars v) {

        ObjectMapper mapper = new ObjectMapper();

        try {

            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("settings.json"), v);

        } catch (IOException e) {
            Core.outError("", e);
        }

    }

    public static Vars deserialize() {

        ObjectMapper mapper = new ObjectMapper();

        try {
            if (!FILE.exists()) {
                Core.outInfo("Creating the " + FILE.getName() + " file if it does not exist!");
                Core.createVarsFile();
            }
            return mapper.readValue(new File("settings.json"), Vars.class);
        } catch (IOException ex) {
            Core.outError(ex.getMessage(), ex);
        }

        return null;
    }

}
