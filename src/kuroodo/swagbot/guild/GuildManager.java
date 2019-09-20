package kuroodo.swagbot.guild;

import java.util.HashMap;

import kuroodo.swagbot.json.GuildSettingsReader;
import kuroodo.swagbot.json.GuildSettingsWriter;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class GuildManager {
	private static final HashMap<Long, GuildSettings> GUILDS = new HashMap<Long, GuildSettings>();

	public static void verifyGuildIntegrity(long guildID) {
		if (containsGuild(guildID)) {
			// If file for guild doesn't exist
			if (!GuildSettingsReader.settingsFileExists(guildID)) {
				GuildSettingsWriter.createNewFile(GUILDS.get(guildID));
			}
		} else {
			// Add missing guild to manager if the template file exists
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

	public static boolean canMemberBeMuted(Guild guild, Member member) {
		if (BotUtility.hasPermission(Permission.ADMINISTRATOR, member)) {
			return false;
		}

		GuildSettings settings = getGuild(guild);
		Role permRole = guild.getRoleById(settings.rolePermission0);
		if (permRole != null && BotUtility.hasRole(permRole, member)) {
			return false;
		}
		permRole = guild.getRoleById(settings.rolePermission1);
		if (permRole != null && BotUtility.hasRole(permRole, member)) {
			return false;
		}
		permRole = guild.getRoleById(settings.rolePermission2);
		if (permRole != null && BotUtility.hasRole(permRole, member)) {
			return false;
		}

		return true;
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

	public static boolean containsGuild(long guildID) {
		return GUILDS.containsKey(guildID);
	}

}
