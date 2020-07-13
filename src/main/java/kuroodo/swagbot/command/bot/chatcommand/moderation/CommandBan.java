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
package kuroodo.swagbot.command.bot.chatcommand.moderation;

import java.awt.Color;

import kuroodo.swagbot.command.CommandKeys;
import kuroodo.swagbot.command.bot.chatcommand.PunishmentCommand;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import kuroodo.swagbot.utils.BotUtility;
import kuroodo.swagbot.utils.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;

public class CommandBan extends PunishmentCommand {

	@Override
	protected void setCommandPermissiosn() {
		requiredPermissions.add(Permission.BAN_MEMBERS);
		isPermission0 = true;
		isPermission1 = true;
	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);
		if (!canExecute)
			return;

		if (GuildManager.canMemberBeRemoved(event.getGuild(), member)) {
			performBan(member);
		} else {
			sendMessage("This person is too important to be removed");
		}

	}

	private void performBan(Member member) {
		GuildSettings settings = GuildManager.getGuild(event.getGuild());
		String reason = getReason(3);
		int days = getDuration();
		if (days == -1) {
			sendMessage(BotUtility
					.codifyText("ERROR: Ban duration is incorrect. Please enter a NUMBER of DAYS or 0 for permanent"));
			return;
		}
		try {
			// Ban member
			member.ban(days, reason).queue();
			logBan(settings, days, reason, member);
			if (reason.isEmpty()) {
				sendBanMessage(member);
			} else {
				sendBanMessage(member, reason);
			}
		} catch (HierarchyException e) {
			sendHierarchyErrorMessage();
		}
	}

	private void sendBanMessage(Member member) {
		sendMessage(BotUtility.boldifyText(member.getUser().getAsTag() + " was banned"));

	}

	private void sendBanMessage(Member member, String reason) {
		sendMessage(BotUtility.boldifyText(member.getUser().getAsTag() + " was banned for " + reason));

	}

	private void logBan(GuildSettings settings, int days, String reason, Member member) {
		EmbedBuilder eb = new EmbedBuilder();

		eb.setTitle("A user has been BANNED");
		eb.setColor(new Color(BotUtility.EMBED_ALERT_COLOR));
		eb.addField("Banned User:", member.getAsMention(), true);
		eb.addField("Invoked by:", event.getAuthor().getAsMention(), true);
		eb.addField("Reason:", reason, false);

		if (days > 0) {
			eb.addField("Duration (days):", "" + days, false);
		} else {
			eb.addField("Duration (days):", "PERMANENT", false);
		}

		Logger.sendLogEmbed(settings, eb);
	}

	private int getDuration() {
		int duration = 0;
		// If entered a mute duration
		if (commandParams.length >= 3) {
			try {
				duration = Integer.parseInt(commandParams[2]);
			} catch (NumberFormatException e) {
				duration = -1;
			}
		}

		return duration;
	}

	@Override
	public String commandDescription() {
		return "Ban a member from the server";
	}

	@Override
	public String commandFormat() {
		return "`" + commandPrefix + CommandKeys.COMMAND_BAN
				+ "` @user <duration DAYS> <reason>(optional)\nEnter 0 for days for a permanent ban";
	}

	@Override
	public String commandUsageExample() {
		return "`" + commandPrefix + CommandKeys.COMMAND_BAN + "` @Person#1234 5 For disturbing the peace\n`" + commandPrefix
				+ CommandKeys.COMMAND_BAN + "` @Person#1234 5";
	}

}