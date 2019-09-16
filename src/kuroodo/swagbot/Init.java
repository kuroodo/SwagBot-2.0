package kuroodo.swagbot;

import java.util.Scanner;

import javax.security.auth.login.LoginException;

import kuroodo.swagbot.config.BotConfig;
import kuroodo.swagbot.json.ConfigKeys;
import kuroodo.swagbot.json.JSONReader;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class Init {
	public static void main(String[] args) {
		// Attempt to get JDA
		JDA jda = makeJDA();
		if (jda == null) {
			System.err.println("ERROR: Could not get JDA. Exitting program");
			return;
		}

		// Set up the bot's configuration for normal operation
		BotConfig config = new BotConfig(jda);
		SwagBot.setConfig(config);

		startShutdownHook();
		startInputThread();

		SwagBot.setActivity(Activity.listening(BotConfig.DEFAULT_GAMEMESSAGE));
		System.out.println("Set up complete");
		System.out.println("Hello I am " + BotConfig.BOTNAME + "v" + BotConfig.BOTVERSION);
	}

	private static JDA makeJDA() {
		try {
			JDA jda = new JDABuilder(JSONReader.getConfigValue(ConfigKeys.BOT_TOKEN)).build();
			System.out.println("JDA build succesful");
			return jda;
		} catch (LoginException e) {
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
