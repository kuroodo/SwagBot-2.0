package kuroodo.swagbot.utils;

import java.util.List;

import kuroodo.swagbot.SwagBot;
import kuroodo.swagbot.guild.GuildManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class BotUtility {

	public static boolean hasPermission(Permission permission, TextChannel channel, Member member) {
		return member.getPermissions(channel).contains(permission);
	}

	public static boolean hasPermissions(List<Permission> permissions, TextChannel channel, Member member) {
		for (Permission perm : permissions) {
			if (!member.getPermissionsExplicit(channel).contains(perm)) {
				return false;
			}
		}
		return true;
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

	public static boolean doesRoleExist(Guild guild, String rolename) {
		return !guild.getRolesByName(rolename, true).isEmpty();
	}
}
