/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.darkdl.mrvnbot.commands;

import de.darkdl.mrvnbot.Core;
import de.darkdl.mrvnbot.Vars;
import java.lang.reflect.Field;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Nils
 */
public class CMDListVars implements Command {

    @Override
    public void called(String[] args, MessageReceivedEvent evt) {
        String allVars = "";
        Field[] fields = Vars.class.getFields();

        for (Field field : fields) {
            String name = field.getName();
            if (!(name.equals("OWNER_IDS") || name.equals("TOKEN"))) {
                allVars += field.getName() + " | " + field.getType() + "\n";
            }
        }

        Core.sendMessageToChannel(allVars, evt.getChannel());
    }

}
