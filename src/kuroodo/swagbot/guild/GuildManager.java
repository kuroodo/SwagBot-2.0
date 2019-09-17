package kuroodo.swagbot.guild;

import java.util.HashMap;

import kuroodo.swagbot.config.GuildConfig;
import net.dv8tion.jda.api.entities.TextChannel;

public class GuildManager {
	public static final HashMap<Long, GuildConfig> GUILDS = new HashMap<Long, GuildConfig>();

	public static TextChannel getTextChannel(long guildID, long channelID) {
		if (GUILDS.containsKey(guildID)) {
			TextChannel channel = GUILDS.get(guildID).guild.getTextChannelById(channelID);
			if (channel != null) {
				return channel;
			}
			System.err.print("ERROR: Could not find TextChannel " + channelID);
		}

		System.err.print("ERROR: Could not find Guild " + guildID);
		return null;
	}
}
