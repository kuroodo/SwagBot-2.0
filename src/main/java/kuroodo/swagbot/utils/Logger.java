/*
 * Copyright [2019] Leandro Gaspar

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
package kuroodo.swagbot.utils;

import kuroodo.swagbot.guild.GuildSettings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

public class Logger {
	public static void sendLogMessage(GuildSettings guild, String message) {
		TextChannel logChannel = guild.guild.getTextChannelById(guild.logChannel);

		if (logChannel != null) {
			if (BotUtility.hasPermission(Permission.MESSAGE_WRITE, logChannel, BotUtility.getSelfMember(guild.guild))) {
				logChannel.sendMessage(message).queue();
			}
		}
	}

	public static void sendLogEmbed(GuildSettings guild, EmbedBuilder eb) {
		TextChannel logChannel = guild.guild.getTextChannelById(guild.logChannel);

		if (logChannel != null) {
			boolean canWrite = BotUtility.hasPermission(Permission.MESSAGE_WRITE, logChannel,
					BotUtility.getSelfMember(guild.guild));
			boolean canEmbed = BotUtility.hasPermission(Permission.MESSAGE_EMBED_LINKS, logChannel,
					BotUtility.getSelfMember(guild.guild));
			if (canWrite && canEmbed) {
				logChannel.sendMessage(eb.build()).queue();
			}
		}
	}
}
