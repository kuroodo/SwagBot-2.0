/*
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

/**
 * Minimal-Json https://github.com/ralfstx/minimal-json/blob/master/README.md
 * Using under the MIT license https://opensource.org/licenses/MIT
 */

package kuroodo.swagbot.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.io.FileUtils;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.ParseException;
import com.eclipsesource.json.WriterConfig;

import kuroodo.swagbot.guild.GuildSettings;

public class GuildSettingsWriter {

	public static void createNewFile(GuildSettings guild) {
		try {
			String path = JSONKeys.SETTINGS_PATH + guild.guildID + ".json";
			// If template file is valid
			if (isTemplateExist()) {
				File templateFile = new File(JSONKeys.SETTINGS_PATH + JSONKeys.TEMPLATE_NAME);
				// Create a new settings file from template
				FileUtils.copyFile(templateFile, new File(path));
				writeSettings(guild);
			} else {
				System.err.println("***************\nERROR: TEMPLATE FILE DOES NOT EXIST\n***************");
			}

		} catch (IOException e) {
			System.err.println("ERROR: Error trying to create new file : " + guild.guildID
					+ ".json\nEnsure that the corresponding files exist");
		}
	}

	public static void writeSettings(GuildSettings guild) {
		String path = JSONKeys.SETTINGS_PATH + guild.guildID + ".json";
		File settingsFile = new File(path);
		try {
			Reader reader;

			// Read and store the contents of the file into a JsonObject
			reader = new BufferedReader(new FileReader(settingsFile));
			JsonObject object = Json.parse(reader).asObject();

			object.set(JSONKeys.SETTINGS_GUILD_ID, Long.toString(guild.guildID));
			object.set(JSONKeys.SETTINGS_COMMAND_PREFIX, guild.commandPrefix);
			object.set(JSONKeys.SETTINGS_ENABLE_WELCOME, Boolean.toString(guild.enableWelcome));
			object.set(JSONKeys.SETTINGS_WELCOME_CHANNEL, Long.toString(guild.welcomeChannel));
			object.set(JSONKeys.SETTINGS_WELCOME_MESSAGE, guild.welcomeMessage);
			object.set(JSONKeys.SETTINGS_ENABLE_WELCOME_ROLE, Boolean.toString(guild.enableWelcomeRole));
			object.set(JSONKeys.SETTINGS_WELCOME_ROLE, Long.toString(guild.welcomeRole));
			object.set(JSONKeys.SETTINGS_LOG_CHANNEL, Long.toString(guild.logChannel));
			object.set(JSONKeys.SETTINGS_MUTE_ROLE, Long.toString(guild.muteRole));
			object.set(JSONKeys.SETTINGS_MUTE_CHANNEL, Long.toString(guild.muteChannel));
			object.set(JSONKeys.SETTINGS_MUSIC_CHANNEL, Long.toString(guild.musicchannel));
			object.set(JSONKeys.SETTINGS_SPARTANKICK, Boolean.toString(guild.spartankick));
			object.set(JSONKeys.SETTINGS_ROLE_PERMISSION0, Long.toString(guild.permission0));
			object.set(JSONKeys.SETTINGS_ROLE_PERMISSION1, Long.toString(guild.permission1));
			object.set(JSONKeys.SETTINGS_ROLE_PERMISSION2, Long.toString(guild.permission2));

			// Overwrite original file and paste contents
			FileWriter writer = new FileWriter(settingsFile, false);
			object.writeTo(writer, WriterConfig.PRETTY_PRINT);

			writer.flush();
			writer.close();
			reader.close();
		} catch (ParseException | IOException e) {
			System.err.println("ERROR: Error trying to write to file : " + guild.guildID
					+ ".json\nEnsure that the corresponding keys or files exist, or that the file valid json");
		}
	}

	public static void writeSetting(long guildID, String key, String value) {
		try {
			Reader reader;
			String path = JSONKeys.SETTINGS_PATH + guildID + ".json";
			File settingsFile = new File(path);

			// Read and store the contents of the file into a JsonObject
			reader = new BufferedReader(new FileReader(settingsFile));
			JsonObject object = Json.parse(reader).asObject();

			object.set(key, value);

			// Overwrite original file and paste contents
			FileWriter writer = new FileWriter(settingsFile, false);
			object.writeTo(writer, WriterConfig.PRETTY_PRINT);

			writer.flush();
			writer.close();
			reader.close();
		} catch (IOException e) {
			System.err.println("ERROR: Error trying to write to file " + guildID + ".json\nEnsure that key " + key
					+ "or file exists");
		}
	}

	public static boolean isTemplateExist() {
		return new File(JSONKeys.SETTINGS_PATH + JSONKeys.TEMPLATE_NAME).exists();
	}
}
