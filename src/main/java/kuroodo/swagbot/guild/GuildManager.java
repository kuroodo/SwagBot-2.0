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

		if (member.getUser() == BotUtility.getSelfUser()) {
			return false;
		}

		GuildSettings settings = getGuild(guild);
		Role permRole = guild.getRoleById(settings.permission0);
		if (permRole != null && BotUtility.hasRole(permRole, member)) {
			return false;
		}
		permRole = guild.getRoleById(settings.permission1);
		if (permRole != null && BotUtility.hasRole(permRole, member)) {
			return false;
		}
		permRole = guild.getRoleById(settings.permission2);
		if (permRole != null && BotUtility.hasRole(permRole, member)) {
			return false;
		}

		return true;
	}
	
	// Can member be removed from server
	public static boolean canMemberBeRemoved(Guild guild, Member member) {
		if (BotUtility.hasPermission(Permission.ADMINISTRATOR, member)) {
			return false;
		}

		if (member.getUser() == BotUtility.getSelfUser()) {
			return false;
		}

		GuildSettings settings = getGuild(guild);
		Role permRole = guild.getRoleById(settings.permission0);
		if (permRole != null && BotUtility.hasRole(permRole, member)) {
			return false;
		}
		permRole = guild.getRoleById(settings.permission1);
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
