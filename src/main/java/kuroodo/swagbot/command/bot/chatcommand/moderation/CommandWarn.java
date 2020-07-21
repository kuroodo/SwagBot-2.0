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

import kuroodo.swagbot.command.CommandKeys;
import kuroodo.swagbot.command.bot.chatcommand.PunishmentCommand;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import kuroodo.swagbot.json.GuildMetaReader;
import kuroodo.swagbot.json.GuildMetaWriter;
import kuroodo.swagbot.utils.BotUtility;
import kuroodo.swagbot.utils.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandWarn extends PunishmentCommand {

	@Override
	protected void setCommandPermissiosn() {
		isPermission0 = true;
		isPermission1 = true;
		isPermission2 = true;
	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);
		if (!canExecute)
			return;

		String reason = getReason(2);
		long guildID = event.getGuild().getIdLong();

		if (!GuildMetaReader.metaFileExists(guildID)) {
			GuildMetaWriter.createNewFile(guildID);
		}

		String val = GuildMetaReader.getValue(guildID, member.getIdLong() + "_warns");
		int warnings = 0;

		if (!val.isEmpty()) {
			try {
				warnings = Integer.parseInt(val);
				warnings += 1;
			} catch (NumberFormatException e) {
				warnings = 1;
			}
		} else {
			warnings = 1;
		}

		GuildMetaWriter.writeVal(guildID, member.getIdLong() + "_warns", warnings + "");
		sendWarnMessage(member, reason, warnings);
		logWarn(GuildManager.getGuild(event.getGuild()), reason, warnings, member);

	}

	private void sendWarnMessage(Member member, String reason, int warnings) {
		String reasonString = reason.isEmpty() ? "" : " **for:** " + reason;
		sendMessage(BotUtility.boldifyText(member.getUser().getAsTag() + " was warned") + reasonString + "\n`(Warned "
				+ warnings + " times)`");
	}

	private void logWarn(GuildSettings settings, String reason, int warnings, Member member) {
		EmbedBuilder eb = new EmbedBuilder();

		eb.setAuthor(member.getUser().getAsTag(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
		eb.setColor(new Color(BotUtility.EMBED_ALERT_COLOR));
		eb.setDescription(member.getAsMention() + " has been WARNED");
		eb.addField("Invoked by:", event.getAuthor().getAsMention(), true);
		if (!reason.isEmpty()) {
			eb.addField("Reason:", reason, true);
		} else {
			eb.addField("Reason:", "No reason given", true);
		}
		eb.addField("Total warns:", warnings + "", false);
		eb.setFooter("User ID: " + member.getIdLong());
		eb.setTimestamp(Instant.now());
		Logger.sendLogEmbed(settings, eb);
	}

	@Override
	public String commandDescription() {
		return "Give a member a warning, and track it. To see amount of warnings, use `" + commandPrefix
				+ CommandKeys.COMMAND_WARNCOUNT + "`";
	}

	@Override
	public String commandFormat() {
		return "`" + commandPrefix + CommandKeys.COMMAND_WARN + "` @user <reason>(optional)";
	}

	@Override
	public String commandUsageExample() {
		return "`" + commandPrefix + CommandKeys.COMMAND_WARN + "` @Person#1234 For disturbing the peace\n`"
				+ commandPrefix + CommandKeys.COMMAND_WARN + "` @Person#1234";
	}
}
