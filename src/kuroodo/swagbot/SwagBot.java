package kuroodo.swagbot;

import kuroodo.swagbot.config.BotConfig;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import kuroodo.swagbot.json.GuildSettingsReader;
import kuroodo.swagbot.json.GuildSettingsWriter;
import kuroodo.swagbot.listeners.ChatListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;

public class SwagBot {
	private static BotConfig config;

	public static void initializeFresh() {
		loadGuilds();

		setActivity(Activity.listening(BotConfig.DEFAULT_GAMEMESSAGE));
		config.addEventListener(new ChatListener());
	}

	private static void loadGuilds() {
		int newGuilds = 0;
		int oldGuilds = 0;
		for (Guild guild : getJDA().getGuilds()) {
			long guildID = guild.getIdLong();
			GuildSettings settings = new GuildSettings(guild);

			if (GuildSettingsReader.settingsFileExists(guildID)) {
				settings = GuildSettingsReader.loadSettingsFile(guildID);
				oldGuilds++;
			} else {
				System.out.println("Generating new Guild file for guild " + guild.getName() + " ID: " + guildID);
				GuildSettingsWriter.createNewFile(settings);
				newGuilds++;
			}
			GuildManager.addGuild(settings);
		}

		System.out.println("Loaded " + oldGuilds + " guild configurations and generated " + newGuilds
				+ " new guild configurations");
	}

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
