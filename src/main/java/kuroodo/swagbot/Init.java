package kuroodo.swagbot;

import java.util.Scanner;

import javax.security.auth.login.LoginException;

import kuroodo.swagbot.config.BotConfig;
import kuroodo.swagbot.json.ConfigReader;
import kuroodo.swagbot.json.JSONKeys;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Init {
	public static void main(String[] args) {
		startShutdownHook();
		startInputThread();
		verifyConfigFile();
		initializeBot();

		System.out.println("Set up complete");
		System.out.println("Hello I am " + BotConfig.BOTNAME + " v" + BotConfig.BOTVERSION);
	}

	private static void initializeBot() {
		// Attempt to get JDA
		JDA jda = initializeJDA();
		if (jda == null) {
			System.out.println("ERROR: Could not start JDA. Ensure the bot token is correct");
			exitApplication(-1);
			return;
		}

		// Set up the bot's configuration for normal operation
		BotConfig config = new BotConfig(jda);
		SwagBot.setConfig(config);
		SwagBot.initializeFresh();
	}

	private static JDA initializeJDA() {
		try {
			JDA jda = new JDABuilder(ConfigReader.getConfigValue(JSONKeys.CONFIG_BOT_TOKEN)).build().awaitReady();
			System.out.println("JDA build succesful");
			return jda;
		} catch (InterruptedException | LoginException e) {
		}
		return null;
	}

	private static void startInputThread() {
		final Thread inputThread = new Thread(new Runnable() {

			public void run() {
				Scanner scanner = new Scanner(System.in);
				while (true) {
					String input = scanner.nextLine();

					if (ConsoleInputKeys.isExitKey(input.toLowerCase())) {
						scanner.close();
						exitApplication(0);
					}
				}
			}
		});
		inputThread.start();
	}

	private static void startShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					SwagBot.getJDA().shutdown();
				} catch (NullPointerException e) {
				}
				System.out.println("Shutting down...");
			}
		});
	}

	private static void verifyConfigFile() {
		if (!ConfigReader.configFileExists()) {
			System.out.println("Error: config.json is missing");
			exitApplication(-1);
		}
	}

	private static void exitApplication(int exitCode) {
		System.exit(exitCode);
	}
}
