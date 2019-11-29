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

import java.awt.Color;

import kuroodo.swagbot.command.CommandKeys;
import kuroodo.swagbot.command.chatcommand.PunishmentCommand;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import kuroodo.swagbot.utils.BotUtility;
import kuroodo.swagbot.utils.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;

public class CommandKick extends PunishmentCommand {

	@Override
	protected void setCommandPermissiosn() {
		requiredPermissions.add(Permission.KICK_MEMBERS);
		isPermission0 = true;
		isPermission1 = true;
	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (member == null) {
			sendEmbed(getCommandInfoAsEmbed());
			return;
		}

		if (GuildManager.canMemberBeRemoved(event.getGuild(), member)) {
			performKick(member);
		} else {
			sendMessage("This person is too important to be removed");
		}

	}

	private void performKick(Member member) {
		GuildSettings settings = GuildManager.getGuild(event.getGuild());
		String reason = getReason(2);

		try {
			member.kick(reason).queue();
			logKick(settings, reason, member);
			if (reason.isEmpty()) {
				sendKickMessage(member);
			} else {
				sendKickMessage(member, reason);
			}
		} catch (HierarchyException e) {
			sendHierarchyErrorMessage();
		}
	}

	private void sendKickMessage(Member member) {
		sendMessage(BotUtility.boldifyText(member.getUser().getAsTag() + " was kicked"));
	}

	private void sendKickMessage(Member member, String reason) {
		sendMessage(BotUtility.boldifyText(member.getUser().getAsTag() + " was kicked for " + reason));
	}

	private void logKick(GuildSettings settings, String reason, Member member) {
		EmbedBuilder eb = new EmbedBuilder();

		eb.setTitle("A user has been KICKED");
		eb.setColor(new Color(BotUtility.EMBED_ALERT_COLOR));
		eb.addField("Kicked User:", member.getAsMention(), true);
		eb.addField("Invoked by:", event.getAuthor().getAsMention(), true);
		eb.addField("Reason:", reason, false);

		Logger.sendLogEmbed(settings, eb);
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
		return commandPrefix + CommandKeys.COMMAND_KICK + " @Person#1234 For disturbing the peace\n" + commandPrefix
				+ CommandKeys.COMMAND_KICK + " @Person#1234";
	}
}