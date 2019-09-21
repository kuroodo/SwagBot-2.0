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
