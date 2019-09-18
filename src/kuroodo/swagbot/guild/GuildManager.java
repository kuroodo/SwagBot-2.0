package kuroodo.swagbot.guild;

import java.util.HashMap;

import kuroodo.swagbot.config.GuildSettings;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class GuildManager {
	private static final HashMap<Long, GuildSettings> GUILDS = new HashMap<Long, GuildSettings>();

	public static final void addGuild(long guildID) {
		addGuild(new GuildSettings(guildID));
	}

	public static final void addGuild(GuildSettings guild) {
		if (!GUILDS.containsKey(guild.guildID)) {
			GUILDS.put(guild.guildID, guild);
		} else {
			System.err.println("Guild " + guild.guildID + " already exists in map");
		}
	}

	public static TextChannel getTextChannel(long guildID, long channelID) {
		if (GUILDS.containsKey(guildID)) {
			TextChannel channel = GUILDS.get(guildID).guild.getTextChannelById(channelID);
			if (channel != null) {
				return channel;
			}
			System.err.print("ERROR: Could not find TextChannel " + channelID + " in guild " + guildID);
		}

		System.err.print("ERROR: Could not find Guild " + guildID);
		return null;
	}

	public static Role getRole(long guildID, long roleID) {
		if (GUILDS.containsKey(guildID)) {
			Role role = GUILDS.get(guildID).guild.getRoleById(roleID);
			if (role != null) {
				return role;
			}
			System.err.print("ERROR: Could not find Role " + roleID + " in guild " + guildID);
		}

		System.err.print("ERROR: Could not find Guild " + guildID);
		return null;
	}

	public static GuildSettings getGuild(long guildID) {
		if (GUILDS.containsKey(guildID)) {
			return GUILDS.get(guildID);
		}

		System.out.println("ERROR: Guild " + guildID + " does not exist in map");
		return null;
	}
}
