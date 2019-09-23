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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import kuroodo.swagbot.SwagBot;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.json.ConfigReader;
import kuroodo.swagbot.json.JSONKeys;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.HierarchyException;

public class BotUtility {

	public static void sendMessageToBotOwner(String message) {
		try {
			long ownerID = Long.parseLong(ConfigReader.getConfigValue(JSONKeys.CONIG_BOT_OWNER));
			User owner = SwagBot.getJDA().getUserById(ownerID);

			if (owner != null) {
				owner.openPrivateChannel().queue(new Consumer<PrivateChannel>() {
					@Override
					public void accept(PrivateChannel t) {
						t.sendMessage(message).queue();
					}
				});
			}

		} catch (NumberFormatException e) {
			System.err.println("Bot owner ID invalid in config file. Cannot send message to owner");
		}
	}

	public static void sendGuildMessage(Guild guild, TextChannel channel, String message) {
		// Check if has perms to send message
		if (hasPermission(Permission.MESSAGE_WRITE, channel, BotUtility.getSelfMember(guild))) {
			channel.sendMessage(message).queue();
		}
	}

	public static void sendEmbed(Guild guild, TextChannel channel, EmbedBuilder eb) {
		// Check if has perms to send message
		if (BotUtility.hasPermission(Permission.MESSAGE_EMBED_LINKS, channel, BotUtility.getSelfMember(guild))) {
			channel.sendMessage(eb.build()).queue();
		}
	}

	public static void addRoleToMember(Guild guild, Role role, Member member) {
		try {
			if (BotUtility.hasPermission(Permission.MANAGE_ROLES, getSelfMember(guild))) {
				guild.addRoleToMember(member, role).queue();
			}
		} catch (HierarchyException e) {
		}
	}

	public static void removeRoleFromMember(Guild guild, Role role, Member member) {
		try {
			if (BotUtility.hasPermission(Permission.MANAGE_ROLES, getSelfMember(guild))) {
				guild.removeRoleFromMember(member, role).queue();
			}
		} catch (HierarchyException e) {
		}
	}

	public static boolean hasPermission(Permission permission, TextChannel channel, Member member) {
		if (!member.getPermissions().contains(Permission.ADMINISTRATOR)) {
			return member.getPermissions(channel).contains(permission);
		}
		return true;
	}

	public static boolean hasPermission(Permission permission, Member member) {
		if (!member.getPermissions().contains(Permission.ADMINISTRATOR)) {
			return member.getPermissions().contains(permission);
		}
		return true;
	}

	public static boolean hasPermissions(List<Permission> permissions, TextChannel channel, Member member) {
		if (!member.getPermissions().contains(Permission.ADMINISTRATOR)) {
			for (Permission perm : permissions) {
				if (!member.getPermissionsExplicit(channel).contains(perm)) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean hasRole(Role role, Member member) {
		return member.getRoles().contains(role);
	}

	public static boolean doesRoleExist(Guild guild, String rolename) {
		return !guild.getRolesByName(rolename, true).isEmpty();
	}

	public static boolean doesRoleExist(Guild guild, long roleID) {
		return guild.getRoleById(roleID) != null;
	}

	public static User getSelfUser() {
		return SwagBot.getJDA().getSelfUser();
	}

	public static Member getSelfMember(Guild guild) {
		return guild.getMember(getSelfUser());
	}

	public static String[] splitString(String string) {
		return string.split("\\s+");
	}

	public static String removePrefix(String prefix, String message) {
		return message.substring(prefix.length());
	}

	public static String getGuildPrefix(long guildID) {
		return GuildManager.getGuild(guildID).commandPrefix;
	}

	public static String codifyText(String message) {
		return "```css\n" + message + "\n```";
	}

	public static String codifyTextBlank(String message) {
		return "```\n" + message + "\n```";
	}

	public static String quotifyText(String message) {
		return "> " + message;
	}
	
	public static String boldifyText(String message) {
		return "**" + message + "**";
	}

	public static String getCurrentDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/YYYY HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}

	// Removes an element by rebuilding the array without that index
	public static String[] removeElement(String[] array, int index) {
		String[] newArray = new String[array.length - 1];
		int replacingIndex = 0;

		for (int i = 0; i < array.length; i++) {
			if (i == index) {
				// Ignore this index and get the index that comes after
				replacingIndex++;
			}

			newArray[i] = array[replacingIndex];
			replacingIndex++;

			if (replacingIndex >= array.length) {
				return newArray;
			}
		}

		return newArray;
	}

	public static int getRandomInt(int range) {
		Random rand = new Random(System.nanoTime());
		return rand.nextInt(range);
	}
}
