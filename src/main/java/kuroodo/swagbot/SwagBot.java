/*
 * /*
 * Copyright 2019 Leandro Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package kuroodo.swagbot;

import kuroodo.swagbot.config.BotConfig;
import kuroodo.swagbot.guild.GuildLogSettings;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import kuroodo.swagbot.json.GuildLogSettingsReader;
import kuroodo.swagbot.json.GuildLogSettingsWriter;
import kuroodo.swagbot.json.GuildSettingsReader;
import kuroodo.swagbot.json.GuildSettingsWriter;
import kuroodo.swagbot.lavaplayer.AudioPlayer;
import kuroodo.swagbot.listeners.ChatListener;
import kuroodo.swagbot.listeners.ServerListener;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;

public class SwagBot {
	private static BotConfig config;

	public static void initializeFresh() {
		verifyGuildTemplateFile();
		loadGuilds();
		setToDefaultActivity();

		config.addEventListener(new ChatListener());
		config.addEventListener(new ServerListener());
		config.addEventListener(new AudioPlayer());
	}

	private static void loadGuilds() {
		int newGuilds = 0;
		int oldGuilds = 0;
		int newLogs = 0;
		int oldLogs = 0;
		
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

			if (GuildLogSettingsReader.settingsFileExists(guildID)) {
				settings.logSettings = GuildLogSettingsReader.loadSettingsFile(guildID);
				oldLogs++;
			} else {
				settings.logSettings = new GuildLogSettings(guild);
				System.out.println("Generating new log settings file for guild " + guild.getName() + " ID: " + guildID);
				GuildLogSettingsWriter.createNewFile(settings.logSettings);
				newLogs++;
			}

			GuildManager.addGuild(settings);
		}

		System.out.println("Loaded " + oldGuilds + " guild configurations and generated " + newGuilds
				+ " new guild configurations");
		System.out.println("Loaded " + oldLogs + " guild LOG configurations and generated " + newLogs
				+ " new guild LOG configurations");
	}

	public static boolean verifyGuildTemplateFile() {
		if (!GuildSettingsWriter.isTemplateExist()) {
			// Big angry message
			String message = "**********************************************************************"
					+ "\nTHE _TEMPLATE.json FILE IS MISSING. CANNOT CREATE GUILDSETTINGS. FIX THIS ASAP\n"
					+ "**********************************************************************";
			System.err.println(message);
			BotUtility.sendMessageToBotOwner(message);
			return false;
		}
		return true;
	}

	public static boolean verifyGuildLogsTemplateFile() {
		if (!GuildLogSettingsWriter.isTemplateExist()) {
			// Big angry message
			String message = "**********************************************************************"
					+ "\nTHE _TEMPLATE_LOGS.json FILE IS MISSING. CANNOT CREATE LOG SETTINGS. FIX THIS ASAP\n"
					+ "**********************************************************************";
			System.err.println(message);
			BotUtility.sendMessageToBotOwner(message);
			return false;
		}
		return true;
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
