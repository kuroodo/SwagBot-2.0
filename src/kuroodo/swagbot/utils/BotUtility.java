package kuroodo.swagbot.utils;

public class BotUtility {
	public static String[] splitString(String string) {
		return string.split("\\s+");
	}
	
	public static String removePrefix(String string) {
		return string.substring(1);
	}
}
