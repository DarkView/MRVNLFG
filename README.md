**MRVNLFG - MRVNBot**

A bot used to create instant invites into the voice channels the user is currently in with a simple command.
If you want to use the bot, you can go ahead. Please keep in mind that no support besides feature requests via this page is offered for this bot.

If the voice channel already has instant invites, the first one will be reused and the invite will only be created if the user is in a permitted voice channel and the command is executed in a permitted text channel. The variables to identify permitted channels are below.

Be sure to change the variables in the settings.json file.
 - OWNER_ID: The user that is allowed to control the bot. Currently only used for the !reload command
 - TOKEN: The bot accounts token, find it [here](https://discordapp.com/developers/applications/)
 - LFG_VOICE_IDENTIFIER: Text used to identify the voice channels for which the LFG command should work
 - LFG_TEXT_IDENTIFIER: Text used to identify the text channels in which the LFG command can be executed
 - COMMAND_IDENTIFIER: The exact text used to identify the command

This Discord bot requires Java 8 and uses [JDA](https://github.com/DV8FromTheWorld/JDA)
