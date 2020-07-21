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
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;

import kuroodo.swagbot.command.CommandKeys;
import kuroodo.swagbot.command.bot.chatcommand.PunishmentCommand;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import kuroodo.swagbot.utils.BotUtility;
import kuroodo.swagbot.utils.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;

public class CommandMute extends PunishmentCommand {

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
		if (!canExecute)
			return;

		if (GuildManager.canMemberBeRemoved(event.getGuild(), member)) {
			GuildSettings settings = GuildManager.getGuild(event.getGuild());
			Role muteRole = settings.guild.getRoleById(settings.muteRole);
			if (muteRole == null) {
				sendMessage(BotUtility.codifyText("Error: mute role is not set up. Use " + commandPrefix
						+ CommandKeys.COMMAND_SETUPHELP + " for more information."));
				return;
			}
			if (member.getRoles().contains(muteRole)) {
				sendMessage(BotUtility.quotifyText(member.getUser().getAsTag() + " is already muted"));
				return;
			}

			performMute(member, settings, muteRole);
		} else {
			sendMessage("This person is too important to be muted");
		}
	}

	private void performMute(Member member, GuildSettings settings, Role muteRole) {
		String reason = getReason(3);
		long duration = getDuration();
		if (duration == -1) {
			sendMessage(BotUtility.codifyText(
					"ERROR: Mute duration is incorrect. Please enter a NUMBER of MINUTES or 0 for permanent"));
			return;
		}
		try {
			// Give mute role
			settings.guild.addRoleToMember(member, muteRole).queue();
			if (duration > 0)
				startUnmuteTimer(duration, member);

			logMute(settings, duration, reason, member);
			if (reason.isEmpty()) {
				sendMuteMessage(member);
			} else {
				sendMuteMessage(member, reason);
			}
		} catch (HierarchyException e) {
			sendHierarchyErrorMessage();
		}
	}

	private void unMute(Member member) {
		GuildSettings settings = GuildManager.getGuild(event.getGuild());
		Role muterole = settings.guild.getRoleById(settings.muteRole);

		if (muterole != null) {
			if (member.getRoles().contains(muterole)) {
				settings.guild.removeRoleFromMember(member, muterole).queue();
				logUnmute(settings, member);
			}
		}
	}

	private void sendMuteMessage(Member member) {
		sendMessage(BotUtility.boldifyText(member.getUser().getAsTag() + " was muted"));
	}

	private void sendMuteMessage(Member member, String reason) {
		sendMessage(BotUtility.boldifyText(member.getUser().getAsTag() + " was muted for " + reason));
	}

	private void logMute(GuildSettings settings, long duration, String reason, Member member) {
		EmbedBuilder eb = new EmbedBuilder();

		eb.setAuthor(member.getUser().getAsTag(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
		eb.setColor(new Color(BotUtility.EMBED_ALERT_COLOR));
		eb.setTitle("USER has been MUTED");
		eb.setDescription(member.getUser().getAsMention());
		eb.addField("Invoked by:", event.getAuthor().getAsMention(), true);
		if (!reason.isEmpty()) {
			eb.addField("Reason:", reason, true);
		} else {
			eb.addField("Reason:", "No reason given", true);
		}

		if (duration > 0) {
			eb.addField("Duration (minutes):", "" + duration, false);
		} else {
			eb.addField("Duration (minutes):", "PERMANENT", false);
		}
		eb.setFooter("User ID: " + member.getIdLong());
		eb.setTimestamp(Instant.now());
		Logger.sendLogEmbed(settings, eb);
	}

	private void startUnmuteTimer(long duration, Member member) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				unMute(member);
			}
		}, duration * 60000);
	}

	private void logUnmute(GuildSettings settings, Member member) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("User mute duration expired");
		eb.setColor(new Color(BotUtility.EMBED_ALERT_COLOR));
		eb.addField("User", member.getAsMention(), true);
		Logger.sendLogEmbed(settings, eb);
	}

	private long getDuration() {
		long duration = 0;
		// If entered a mute duration
		if (commandParams.length >= 3) {
			try {
				duration = Long.parseLong(commandParams[2]);
			} catch (NumberFormatException e) {
				duration = -1;
			}
		}

		return duration;
	}

	@Override
	public String commandDescription() {
		return "Give a member the mute role";
	}

	@Override
	public String commandFormat() {
		return "`" + commandPrefix + CommandKeys.COMMAND_MUTE
				+ "1 @user <duration MINUTES>(enter 0 for permanent) <reason>(optional)";
	}

	@Override
	public String commandUsageExample() {
		return "`" + commandPrefix + CommandKeys.COMMAND_MUTE + "` @Person#1234 5 For disturbing the peace\n`"
				+ commandPrefix + CommandKeys.COMMAND_MUTE + "` @Person#1234";
	}
}
