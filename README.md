**MRVNLFG - MRVNBot**

A bot used to create instant invites into voice channels with a simple command.
This bot is currently rather hard-coded for the Apex Legends Discord server, but this will change in the future!

If you want to use it, you can go ahead. Please keep in mind that no support is offered for this bot at all!
Be sure to change the variables in the settings.json file.
 - OWNER_ID: The user that is allowed to control the bot. Currently only used for the !reload command
 - TOKEN: The bot accounts token, find it [here](https://discordapp.com/developers/applications/)
 - LFG_VOICE_IDENTIFIER: Text used to identify the voice channels for which the LFG command should work
 - LFG_TEXT_IDENTIFIERT: Text used to identify the text channels in whicha the LFG command can be executed (Currently has a spelling error)
 - COMMAND_IDENTIFIER: The exact text used to identify the command
 
All of the identifiers need to be in all lower-case letters, as the message and names get converted to lower-case automatically (this will likely change in the future)

This Discord bot requires Java 8 and uses [JDA](https://github.com/DV8FromTheWorld/JDA)
