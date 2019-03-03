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
    
    public static void serialize(Vars v) {
        
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("settings.json"), v);
            
        } catch (IOException e) {
            
        }
        
    }
    
    public static Vars deserialize() {
        
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            return mapper.readValue(new File("settings.json"), Vars.class);
        } catch (IOException ex) {
            Core.outError(ex.getMessage(), ex);
        }
        
        return null;
    }
    
}
