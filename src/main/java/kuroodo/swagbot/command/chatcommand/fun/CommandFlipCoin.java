
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
package kuroodo.swagbot.command.chatcommand.fun;

import java.util.Random;

import kuroodo.swagbot.command.CommandKeys;
import kuroodo.swagbot.command.chatcommand.ChatCommand;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandFlipCoin extends ChatCommand {
	private Random rand;

	public CommandFlipCoin() {
		rand = new Random();
	}

	@Override
	protected void setCommandPermissiosn() {
		requiredPermissions.add(Permission.MESSAGE_WRITE);
	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!selfHasPermissions()) {
			return;
		}

		// Delete the command message if permissions
		if (BotUtility.hasPermission(Permission.MESSAGE_MANAGE, BotUtility.getSelfMember(event.getGuild()))) {
			event.getMessage().delete().queue();
		}

		int sides = 2;
		rand.setSeed(System.nanoTime());
		int x = rand.nextInt(sides);

		if (x == 0) {
			sendMessage(event.getAuthor().getAsMention() + "```css\nflips a coin and it lands on tails\n```");
		} else {
			sendMessage(event.getAuthor().getAsMention() + "```css\nflips a coin and it lands on heads\n```");
		}
	}

	@Override
	public String commandDescription() {
		return "Flip a coin between heads or tails";
	}

	@Override
	public String commandFormat() {
		return commandPrefix + CommandKeys.COMMAND_FLIPCOIN;
	}

	@Override
	public String commandUsageExample() {
		return commandPrefix + CommandKeys.COMMAND_FLIPCOIN;
	}
}
