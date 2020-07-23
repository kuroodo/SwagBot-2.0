
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
package kuroodo.swagbot.command.bot.chatcommand.fun;

import java.awt.Color;
import java.time.Instant;

import kuroodo.swagbot.command.CommandKeys;
import kuroodo.swagbot.command.bot.chatcommand.ChatCommand;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandSnipe extends ChatCommand {
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

		Message msg = GuildManager.getMessageFromDeletedCache(event.getTextChannel().getIdLong());

		if (msg == null) {
			sendMessage("No message to snipe. Git gud!");
			return;
		}

		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor(msg.getAuthor().getAsTag(), msg.getAuthor().getAvatarUrl(), msg.getAuthor().getAvatarUrl());
		eb.setDescription(msg.getContentDisplay());
		eb.setColor(new Color(BotUtility.EMBDED_USER_MESSAGE_COLOR));
		eb.setTimestamp(Instant.now());
		sendEmbed(eb);
	}

	@Override
	public String commandDescription() {
		return "Snipe/Show a recently deleted message.";
	}

	@Override
	public String commandFormat() {
		return "`" + commandPrefix + CommandKeys.COMMAND_SNIPE + "`";
	}

	@Override
	public String commandUsageExample() {
		return "`" + commandPrefix + CommandKeys.COMMAND_SNIPE + "`";
	}
}
