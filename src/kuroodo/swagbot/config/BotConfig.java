package kuroodo.swagbot.config;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotConfig {
	public static final String BOTNAME = "SwagBot";
	public static final String BOTVERSION = "2.0.0";
	public static final String DEFAULT_GAMEMESSAGE = "@me help for help";
	private JDA jda;

	public BotConfig(JDA jda) {
		this.jda = jda;
	}

	public void addEventListener(ListenerAdapter event) {
		jda.addEventListener(event);
	}

	public JDA getJDA() {
		return jda;
	}
}
