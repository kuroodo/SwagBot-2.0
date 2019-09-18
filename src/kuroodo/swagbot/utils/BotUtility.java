package kuroodo.swagbot.utils;

public class BotUtility {
	public static String[] splitString(String string) {
		return string.split("\\s+");
	}
	
	public static String removePrefix(String prefix, String message) {
		return message.substring(prefix.length());
	}
}
