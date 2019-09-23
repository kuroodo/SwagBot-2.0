/*
 * Copyright 2019 Leandro Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package kuroodo.swagbot.listeners;

import kuroodo.swagbot.command.Command;
import kuroodo.swagbot.command.CommandKeys;
import kuroodo.swagbot.command.CommandRegistry;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

// Listens for chat specific events like commands
public class ChatListener extends ListenerAdapter {

	@Override
	public void onGenericGuild(GenericGuildEvent event) {
		super.onGenericGuild(event);
		GuildManager.verifyGuildIntegrity(event.getGuild().getIdLong());
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		super.onMessageReceived(event);
		if (event.getAuthor().isFake() || event.getAuthor().isBot()) {
			return;
		}
		HandleCommandRequest(event);
		// TODO: LogChat or any other functionality()
	}

	private void HandleCommandRequest(MessageReceivedEvent event) {
		if (event.isFromGuild()) {
			GuildSettings guild = GuildManager.getGuild(event.getGuild().getIdLong());

			String commandName = "";
			String[] commandParams = BotUtility.splitString(event.getMessage().getContentRaw());

			// If starts with command prefix
			if (commandParams[0].startsWith(guild.commandPrefix)) {
				commandName = BotUtility.removePrefix(guild.commandPrefix, commandParams[0]);
				// Else starts with bot mention
			} else if (commandParams[0].equals((BotUtility.getSelfUser().getAsMention()))) {
				// If entered parameters
				if (commandParams.length > 1) {
					commandName = commandParams[1];
					// Remove bot mention
					commandParams = BotUtility.removeElement(commandParams, 0);

					// If not help or setuphelp command
					if (!commandName.equals(CommandKeys.COMMAND_HELP)
							&& !commandName.equals(CommandKeys.COMMAND_SETUPHELP)) {
						
						// Set to magicball command
						commandName = CommandKeys.COMMAND_MAGICBALL;
					}
				} else {// If just a blank mention, do magicball
					commandName = CommandKeys.COMMAND_MAGICBALL;
				}
			} else {
				return;
			}

			Command command = CommandRegistry.getCommand(commandName);
			command.executeCommand(commandParams, event);

		} else if (event.isFromType(ChannelType.PRIVATE)) {
			// TODO: Private commands
		}
	}
}