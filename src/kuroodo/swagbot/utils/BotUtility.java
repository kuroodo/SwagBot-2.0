package kuroodo.swagbot.utils;

import kuroodo.swagbot.guild.GuildManager;

public class BotUtility {
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
}
