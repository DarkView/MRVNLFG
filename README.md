**MRVNLFG - MRVNBot**

A bot used to create instant invites into the voice channels the user is currently in with a simple command.
If you want to use the bot, you can go ahead. Please keep in mind that no support besides feature requests via this page is offered for this bot.

If the voice channel already has instant invites, the first one will be reused. The command will only be executed if the user is in a permitted voice channel and the command is executed in a permitted text channel. The variables to identify permitted channels are below.

Be sure to change the variables in the settings.json file.
 - `OWNER_IDS`: The users that are allowed to control the bots more powerful functions
 - `MOD_ROLE_ID`: The role identifying users that are allowed to execute commands with moderator access level
 - `CMD_PREFIX`: Used to identify commands like reload and updatevar
 - `TOKEN`: The bot accounts token, find it [here](https://discordapp.com/developers/applications/)
 - `LFG_VOICE_IDENTIFIER`: Text used to identify the voice channels for which the LFG command should work
 - `LFG_TEXT_IDENTIFIER`: Text used to identify the text channels in which the LFG command can be executed
 - `LFG_COMMAND_IDENTIFIER`: The exact text used to identify the command
 - `INVITE_EXPIRE_SECONDS`: The time in seconds after which the invite should expire
 - `LIST_OTHER_USERS`: true if the other users in the voice channel of the requestor should be listed. Else set to false
 - `MESSAGE_COMPACT`: true if the compact style should be used for the invite messages
 - `MYSQL_ENABLED`: Whether or not we want to connect to a database instead of using RAM. Recommended for 50+ voice channels. If the connection cant be established, the bot will automatically revert to RAM mode
 - `MYSQL_INFO`: All the information needed for the connection: `dbPort, dbName, dbUser, dbHost, dbPassword, dbTableName`
 
Public commands for this bot:
 - `<COMMAND_IDENTIFIER>`: If the command is sent in a channel with `<LFG_TEXT_IDENTIFIER>` and the user is in a channel with `<LFG_VOICE_IDENTIFIER>` in its name, the bot will create or reuse an invite into that channel and post it into the text channel
 
Commands only usable by the mods defined in the settings.json:
- `reload`: Reloads the settings.json
- `version`: Displays the version of the bot currently running
- `delay`: Displays the ping from Bot -> Discord and the Delay of creating/sending the last 5 LFG responses

- `where <userid>`: Displays the name of the voice channel the user is currently in and posts an invite link to it
- `addblocked <regex>`: Adds the specified regex to the list of blocked words (cant contain space)
- `removeblocked <regex>`: Removes the specified regex from the list. Needs to be the exact same to remove
- `listblocked`: Lists all the currently blocked regexs

Commands only usable by the owners defined in the settings.json:
- `listvars`: Lists all the vars that can be updated via the command below
- `updatevar <var_name> <input>`: Updates the var with the specified name to the given input. For booleans, `[true, t, 1]` result in true, anything else in false
- `message <subcommand [params]`: Used to post the same message to multiple channels. Read [this file](https://github.com/DarkView/MRVNLFG/blob/master/MESSAGE_SUBCOMMANDS.MD) to get info on the subcommands

This Discord bot requires Java 8 and uses [JDA](https://github.com/DV8FromTheWorld/JDA)
