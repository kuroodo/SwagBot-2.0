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

import java.awt.Color;

import kuroodo.swagbot.command.CommandKeys;
import kuroodo.swagbot.command.chatcommand.ChatCommand;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandAvatar extends ChatCommand {

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
		Member member = findParamsMember();

		// If no parameters get the message author
		if (commandParams.length == 1) {
			member = event.getMember();
		}

		if (member == null) {
			sendMessage("Please mention a valid user");
			return;
		}

		// Delete the command message if permissions
		if (BotUtility.hasPermission(Permission.MESSAGE_MANAGE, BotUtility.getSelfMember(event.getGuild()))) {
			event.getMessage().delete().queue();
		}

		sendEmbed(makeEmbed(member));
	}

	private EmbedBuilder makeEmbed(Member member) {
		EmbedBuilder eb = new EmbedBuilder();

		String name = member.getUser().getName();
		String avatarURL = member.getUser().getAvatarUrl();

		eb.setColor(Color.RED);
		eb.addField(name + "'s Avatar: ", member.getAsMention(), false);
		eb.setImage(avatarURL);
		eb.setFooter("Request made by: " + event.getAuthor().getAsTag());
		return eb;
	}

	@Override
	public String commandDescription() {
		return "Get the avatar of yourself or a user";
	}

	@Override
	public String commandFormat() {
		return commandPrefix + CommandKeys.COMMAND_AVATAR + "\n" + commandPrefix + CommandKeys.COMMAND_AVATAR
				+ " @user";
	}

	@Override
	public String commandUsageExample() {
		return commandPrefix + CommandKeys.COMMAND_AVATAR + "\n" + commandPrefix + CommandKeys.COMMAND_AVATAR
				+ " @Person#1234";
	}

}
