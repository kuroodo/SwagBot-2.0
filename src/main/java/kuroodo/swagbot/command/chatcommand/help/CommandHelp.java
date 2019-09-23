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
package kuroodo.swagbot.command.chatcommand.help;

import kuroodo.swagbot.command.Command;
import kuroodo.swagbot.command.CommandKeys;
import kuroodo.swagbot.command.CommandRegistry;
import kuroodo.swagbot.command.chatcommand.ChatCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandHelp extends ChatCommand {

	@Override
	protected void setCommandPermissiosn() {
		requiredPermissions.add(Permission.MESSAGE_WRITE);
		requiredPermissions.add(Permission.MESSAGE_EMBED_LINKS);
	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!selfHasPermissions()) {
			return;
		}
		EmbedBuilder eb = null;

		// If no parameters
		if (commandParams.length == 1) {
			eb = sendHelpOverview();
			// If asking for help with specific command
		} else if (commandParams.length == 2) {
			eb = sendCommandHelp();
		}
		if (eb != null) {
			sendEmbed(eb);
		}
	}

	private EmbedBuilder sendHelpOverview() {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Bot Help");
		eb.setDescription(
				"[click here to see the manual for help on commands and bot functions: ](https://github.com/kuroodo/SwagBot-2.0/blob/master/help.txt)");

		return eb;
	}

	private EmbedBuilder sendCommandHelp() {
		EmbedBuilder eb = new EmbedBuilder();
		String commandName = commandParams[1];
		Command command = CommandRegistry.getCommand(commandParams[1]);
		command.commandPrefix = commandPrefix;

		eb.setTitle(commandName);
		eb.setDescription(command.commandDescription());
		eb.addField("Usage", command.commandFormat(), false);
		eb.addField("Example", command.commandUsageExample(), false);
		return eb;
	}

	@Override
	public String commandDescription() {
		return "Gives information on how to use certain commands.";
	}

	@Override
	public String commandFormat() {
		return "For general help, " + commandPrefix + CommandKeys.COMMAND_HELP + "\nFor help with a specific command: "
				+ commandPrefix + CommandKeys.COMMAND_HELP + "<commandname>";
	}

	@Override
	public String commandUsageExample() {
		return commandPrefix + CommandKeys.COMMAND_HELP + " magicball";
	}
}
