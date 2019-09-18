package kuroodo.swagbot;

import java.util.Scanner;

import javax.security.auth.login.LoginException;

import kuroodo.swagbot.config.BotConfig;
import kuroodo.swagbot.config.GuildSettings;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.json.ConfigReader;
import kuroodo.swagbot.json.GuildSettingsReader;
import kuroodo.swagbot.json.GuildSettingsWriter;
import kuroodo.swagbot.json.JSONKeys;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class Init {
	public static void main(String[] args) {

		initializeBot();
		startShutdownHook();
		startInputThread();

		SwagBot.setActivity(Activity.listening(BotConfig.DEFAULT_GAMEMESSAGE));
		SwagBot.getConfig().addEventListener(new ChatListener());
		System.out.println("Set up complete");
		System.out.println("Hello I am " + BotConfig.BOTNAME + "v" + BotConfig.BOTVERSION);

		// Testing reading and writing
		GuildSettingsWriter.createNewFile(new GuildSettings(SwagBot.getJDA().getGuilds().get(0)));
		GuildSettings settings = GuildSettingsReader.loadSettingsFile(SwagBot.getJDA().getGuilds().get(0).getIdLong());

		settings.enableWelcome = true;
		settings.welcomeMessage = "Suh homofag";
		settings.enableWelcomeRole = true;
		settings.welcomeChannel = 155780162254929921L;
		settings.welcomeRole = 110624804536614912L;

		GuildSettingsWriter.writeSettings(settings);
		GuildManager.addGuild(settings);
		// TODO: Exception and error checking
	}

	private static void initializeBot() {
		// Attempt to get JDA
		JDA jda = makeJDA();
		if (jda == null) {
			System.err.println("ERROR: Could not get JDA. Exitting program");
			return;
		}

		// Set up the bot's configuration for normal operation
		BotConfig config = new BotConfig(jda);
		SwagBot.setConfig(config);
	}

	private static JDA makeJDA() {
		try {
			JDA jda = new JDABuilder(ConfigReader.getConfigValue(JSONKeys.CONFIG_BOT_TOKEN)).build().awaitReady();
			System.out.println("JDA build succesful");
			return jda;
		} catch (InterruptedException | LoginException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void startInputThread() {
		final Thread inputThread = new Thread(new Runnable() {

			public void run() {
				System.out.println("running");
				Scanner scanner = new Scanner(System.in);
				while (true) {
					String input = scanner.nextLine();

					if (ConsoleInputKeys.isExitKey(input.toLowerCase())) {
						scanner.close();
						System.exit(0);
					}
				}
			}
		});
		inputThread.start();
	}

	private static void startShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("Shutdown Hook is running !");
			}
		});
	}
}
