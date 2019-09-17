package kuroodo.swagbot.guild;

import java.util.HashMap;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class GuildManager {
	public static final HashMap<Long, Guild> GUILDS = new HashMap<Long, Guild>();

	public static TextChannel getTextChannel(long guild, long channelID) {
		if (GUILDS.containsKey(guild)) {
			TextChannel channel = GUILDS.get(guild).getTextChannelById(channelID);
			if (channel != null) {
				return channel;
			}
			System.err.print("ERROR: Could not find TextChannel " + channelID);
		}

		System.err.print("ERROR: Could not find Guild " + guild);
		return null;
	}
}
