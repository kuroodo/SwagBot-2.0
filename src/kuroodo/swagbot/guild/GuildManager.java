package kuroodo.swagbot.guild;

import java.util.HashMap;

import kuroodo.swagbot.json.GuildSettingsReader;
import kuroodo.swagbot.json.GuildSettingsWriter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class GuildManager {
	private static final HashMap<Long, GuildSettings> GUILDS = new HashMap<Long, GuildSettings>();

	public static void verifyGuildIntegrity(long guildID) {
		if (!GuildSettingsReader.settingsFileExists(guildID)) {
			GuildSettingsWriter.createNewFile(GUILDS.get(guildID));
		}

		if (!containsGuild(guildID)) {
			if (GuildSettingsWriter.isTemplateExist()) {
				addGuild(guildID);
			}
		}
	}

	public static void reloadGuildSettings(long guildID) {
		if (GUILDS.containsKey(guildID)) {
			GUILDS.replace(guildID, GuildSettingsReader.loadSettingsFile(guildID));
		} else {
			System.err.println("Cannot reload Guild " + guildID + " it does not exist in the map");
		}

	}

	public static void addGuild(long guildID) {
		addGuild(new GuildSettings(guildID));
	}

	public static void addGuild(GuildSettings guild) {
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
			System.err.println("ERROR: Could not find TextChannel " + channelID + " in guild " + guildID);
		}

		System.err.println("ERROR: Could not find Guild " + guildID);
		return null;
	}

	public static VoiceChannel getVoiceChannel(long guildID, long channelID) {
		if (GUILDS.containsKey(guildID)) {
			VoiceChannel channel = GUILDS.get(guildID).guild.getVoiceChannelById(channelID);
			if (channel != null) {
				return channel;
			}
			System.err.println("ERROR: Could not find VoiceChannel " + channelID + " in guild " + guildID);
		}

		System.err.println("ERROR: Could not find Guild " + guildID);
		return null;
	}

	public static Role getRole(long guildID, long roleID) {
		if (GUILDS.containsKey(guildID)) {
			Role role = GUILDS.get(guildID).guild.getRoleById(roleID);
			if (role != null) {
				return role;
			}
			System.err.println("ERROR: Could not find Role " + roleID + " in guild " + guildID);
		}

		System.err.println("ERROR: Could not find Guild " + guildID);
		return null;
	}

	public static GuildSettings getGuild(long guildID) {
		if (GUILDS.containsKey(guildID)) {
			return GUILDS.get(guildID);
		}

		System.out.println("ERROR: Guild " + guildID + " does not exist in map");
		return null;
	}

	public static GuildSettings getGuild(Guild guild) {
		if (GUILDS.containsKey(guild.getIdLong())) {
			return GUILDS.get(guild.getIdLong());
		}

		System.out.println("ERROR: Guild " + guild.getIdLong() + " does not exist in map");
		return null;
	}

	public static void removeGuild(Guild guild) {
		if (GUILDS.containsKey(guild.getIdLong())) {
			GUILDS.remove(guild.getIdLong());
		}
	}

	public static void removeGuild(long guildID) {
		if (GUILDS.containsKey(guildID)) {
			GUILDS.remove(guildID);
		}
	}

	public static boolean containsGuild(long guildID) {
		return GUILDS.containsKey(guildID);
	}

}
