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
package kuroodo.swagbot.command.chatcommand.moderation;

import kuroodo.swagbot.command.CommandKeys;
import kuroodo.swagbot.command.chatcommand.ChatCommand;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import kuroodo.swagbot.utils.BotUtility;
import kuroodo.swagbot.utils.Logger;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandUnmute extends ChatCommand {
	@Override
	protected void setCommandPermissiosn() {
		requiredPermissions.add(Permission.MANAGE_ROLES);
		isPermission0 = true;
		isPermission1 = true;
		isPermission2 = true;
	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!selfHasPermissions() || !memberHasPermissions(event.getMember())) {
			return;
		}

		Member member = findParamsMember();

		if (member == null) {
			sendMessage("Please mention a valid user");
			return;
		}
		performUnmute(member);
		// Delete the command message if permissions
		if (BotUtility.hasPermission(Permission.MESSAGE_MANAGE, BotUtility.getSelfMember(event.getGuild()))) {
			event.getMessage().delete().queue();
		}
	}

	private void performUnmute(Member member) {
		GuildSettings settings = GuildManager.getGuild(event.getGuild());
		Role muterole = settings.guild.getRoleById(settings.muteRole);

		if (muterole != null) {
			if (member.getRoles().contains(muterole)) {
				settings.guild.removeRoleFromMember(member, muterole).queue();
				logUnmute(settings, member);
				sendUnmuteMessage(member);
			}
		}
	}

	private void sendUnmuteMessage(Member member) {
		sendMessage(BotUtility.boldifyText(member.getUser().getAsTag() + " was unmuted"));
	}

	private void logUnmute(GuildSettings settings, Member member) {
		String logMessage = BotUtility
				.quotifyText(event.getAuthor().getName() + " has UNMUTED user " + member.getAsMention());
		Logger.sendLogMessage(settings, logMessage);
	}

	@Override
	public String commandDescription() {
		return "Remove the mute role from a member with the mute role";
	}

	@Override
	public String commandFormat() {
		return commandPrefix + CommandKeys.COMMAND_UNMUTE + " @user";
	}
	
	@Override
	public String commandUsageExample() {
		return commandPrefix + CommandKeys.COMMAND_UNMUTE + " @Person#1234";
	}
}
