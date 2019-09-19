package kuroodo.swagbot;

import java.util.function.Consumer;

import kuroodo.swagbot.config.BotConfig;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import kuroodo.swagbot.json.ConfigReader;
import kuroodo.swagbot.json.GuildSettingsReader;
import kuroodo.swagbot.json.GuildSettingsWriter;
import kuroodo.swagbot.json.JSONKeys;
import kuroodo.swagbot.listeners.ChatListener;
import kuroodo.swagbot.listeners.ServerListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.PrivateChannel;

public class SwagBot {
	private static BotConfig config;

	public static void initializeFresh() {
		loadGuilds();

		setToDefaultActivity();
		config.addEventListener(new ChatListener());
		config.addEventListener(new ServerListener());
	}

	private static void loadGuilds() {
		
		if (!GuildSettingsWriter.isTemplateExist()) {
			// Send a message to the bot owner
			getJDA().getUserById(ConfigReader.getConfigValue(JSONKeys.CONIG_BOT_OWNER)).openPrivateChannel()
					.queue(new Consumer<PrivateChannel>() {

						@Override
						public void accept(PrivateChannel t) {
							t.sendMessage("THE _TEMPLATE.json FILE IS MISSING. CANNOT CREATE GUILDSETTINGS. FIX THIS ASAP").queue();
						}
					});
		}

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

	public static void setToDefaultActivity() {
		setActivity(Activity.listening(BotConfig.DEFAULT_GAMEMESSAGE));
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
