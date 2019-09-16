package kuroodo.swagbot;

import kuroodo.swagbot.config.BotConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

public class SwagBot {
	private static BotConfig config;

	public static void setActivity(Activity activity) {
		getJDA().getPresence().setActivity(activity);
	}

	public static BotConfig getConfig() {
		return config;
	}

	public static void setConfig(BotConfig botconfig) {
		config = botconfig;
	}

	public static JDA getJDA() {
		return config.getJDA();
	}
}
