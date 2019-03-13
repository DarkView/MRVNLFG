**MRVNLFG - MRVNBot**

A bot used to create instant invites into the voice channels the user is currently in with a simple command.
If you want to use the bot, you can go ahead. Please keep in mind that no support besides feature requests via this page is offered for this bot.

If the voice channel already has instant invites, the first one will be reused. The command will only be executed if the user is in a permitted voice channel and the command is executed in a permitted text channel. The variables to identify permitted channels are below.

Be sure to change the variables in the settings.json file.
 - `OWNER_IDS`: The users that are allowed to control the bot. Currently only used for the !reload command
 - `CMD_PREFIX`: Used to identify commands like reload and updatevar
 - `TOKEN`: The bot accounts token, find it [here](https://discordapp.com/developers/applications/)
 - `LFG_VOICE_IDENTIFIER`: Text used to identify the voice channels for which the LFG command should work
 - `LFG_TEXT_IDENTIFIER`: Text used to identify the text channels in which the LFG command can be executed
 - `LFG_COMMAND_IDENTIFIER`: The exact text used to identify the command
 - `INVITE_EXPIRE_SECONDS`: The time in seconds after which the invite should expire
 - `LIST_OTHER_USERS`: true if the other users in the voice channel of the requestor should be listed. Else set to false
 - `MESSAGE_COMPACT`: true if the compact style should be used for the invite messages
 
Public commands for this bot:
 - `<COMMAND_IDENTIFIER>`: If the command is sent in a channel with `<LFG_TEXT_IDENTIFIER>` and the user is in a channel with `<LFG_VOICE_IDENTIFIER>` in its name, the bot will create or reuse an invite into that channel and post it into the text channel
 
Commands only usable by the owners defined in the settings.json:
- `reload`: Reloads the settings.json
- `version`: Displays the version of the bot currently running
- `addblocked`: Adds the specified regex to the list of blocked words (cant contain space)
- `removeblocked`: Removes the specified regex from the list. Needs to be the exact same to remove
- `listvars`: Lists all the vars that can be updated via the command below
- `updatevar <var_name> <input>`: Updates the var with the specified name to the given input. For booleans, `[true, t, 1]` result in true, anything else in false

This Discord bot requires Java 8 and uses [JDA](https://github.com/DV8FromTheWorld/JDA)
