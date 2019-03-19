This is the information for the subcommands of  MRVNs bulk message feature (Admin/Owner only):

- `create <name>`  — Creates a new MRVNMessage with the specified name, which is also used for the load command. Creating two messages of the same name overrides the old one.

- `load <name>`  — Loads the MRVNMessage of that specific name.

- `unload`  — Unloads the current message to prevent accidental edits.

- `info`  — Displays information about the currently loaded message.

- `setchannels [id1, id2, ...]`  — Sets the channels the message will be posted in. If they are not already included they get added, if they exist but are not in the new parameters it will be removed. Having no parameters clears the channels fully.
Putting anything that starts with a c in front of the ID will tell the program it is a category ID. This will get resolved to all the channels in that category

- `addchannels <id1> [id2, ...]`  — Adds the specified channels to the list, does not delete old ones if not included and will not override old ones.
Putting anything that starts with a c in front of the ID will tell the program it is a category ID. This will get resolved to all the channels in that category

- `setmessage <message>`  — Sets the message the bot will post. It gets resolved to the markdown text to it will keep markdown. Formatting will also stay the same.

- `setpin <param>`  — Sets whether or not the message should be pinned to the channel its posted in. [t, true, 1] will all get resolved to true. Anything else is false automatically. False by default.

- `post`  — Posts or edits the current message in all channels listed via the set/addchannels command. Also pins or un-pins the message in case setpin was set to true.
Use this in the following cases:
  - You are done creating a new message and want to post it. This includes: Setting the message and channels (and optionally the pin)
  - You edit the message via setmessage
  - You add new channels
  - You changed whether or not the message should be pinned
  
- `delete`  — Deletes the current message from all channels listed via the set/addchannels command if it exists. Deleting the messages will not remove the message from the system. This is only possible by deleting the file created for it.
Use this in the following cases:
  - You are about to remove channels from the message. Why? Else you’ll have to delete the messages manually. Don’t forget to use post afterwards
  - You want all of the messages gone
