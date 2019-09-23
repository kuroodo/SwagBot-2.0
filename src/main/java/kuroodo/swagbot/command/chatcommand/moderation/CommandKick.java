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
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandKick extends ChatCommand {

	@Override
	protected void setCommandPermissiosn() {
		requiredPermissions.add(Permission.KICK_MEMBERS);
		isPermission0 = true;
		isPermission1 = true;
	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!selfHasPermissions() || !memberHasPermissions(event.getMember())) {
			return;
		}

		Member member = findParamsMember();

		if (member == null) {
			sendMessage("Please mention a valid user or ensure correct command format");
			return;
		}

		if (GuildManager.canMemberBeRemoved(event.getGuild(), member)) {
			performKick(member);
		} else {
			sendMessage("This person is too important to be removed");
		}

		// Delete the command message if permissions
		if (BotUtility.hasPermission(Permission.MESSAGE_MANAGE, BotUtility.getSelfMember(event.getGuild()))) {
			event.getMessage().delete().queue();
		}
	}

	private void performKick(Member member) {
		GuildSettings settings = GuildManager.getGuild(event.getGuild());
		String reason = getReason();

		member.kick(reason).queue();

		logKick(settings, reason, member);
		sendKickMessage(member);
	}

	private void sendKickMessage(Member member) {
		sendMessage(BotUtility.boldifyText(member.getUser().getAsTag() + " was kicked"));
	}

	private void logKick(GuildSettings settings, String reason, Member member) {
		String logMessage = event.getAuthor().getAsMention() + " has KICKED user " + member.getAsMention()
				+ " with reason: " + reason;

		Logger.sendLogMessage(settings, BotUtility.quotifyText(logMessage));
	}

	private String getReason() {
		String reason = "";
		for (int i = 3; i < commandParams.length; i++) {
			reason += commandParams[i] + " ";
		}

		return reason;
	}

	@Override
	public String commandDescription() {
		return "Kick a member from the server";
	}

	@Override
	public String commandFormat() {
		return commandPrefix + CommandKeys.COMMAND_KICK + " @user <reason>(optional)";
	}

	@Override
	public String commandUsageExample() {
		return commandPrefix + CommandKeys.COMMAND_KICK + " @Person#1234 For disturbing the peace\n"
				+ commandPrefix + CommandKeys.COMMAND_KICK + " @Person#1234";
	}
}