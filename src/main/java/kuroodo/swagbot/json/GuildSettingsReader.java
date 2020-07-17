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
import java.io.IOException;
import java.io.Reader;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.ParseException;

import kuroodo.swagbot.guild.GuildSettings;

public class GuildSettingsReader {

	public static GuildSettings loadSettingsFile(long guildID) {
		Reader reader;
		GuildSettings settings = new GuildSettings(guildID);
		try {
			reader = new BufferedReader(new FileReader(JSONKeys.SETTINGS_PATH + guildID + ".json"));
			JsonObject jsonObject = Json.parse(reader).asObject();
			settings = new GuildSettings(guildID);

			boolean needsWriting = false;
			// Load each guild setting by type
			needsWriting = loadBools(settings, jsonObject);
			needsWriting = loadStrings(settings, jsonObject);
			needsWriting = loadLongs(settings, jsonObject);

			reader.close();

			// Restore any objects that were missing or had issues
			if (needsWriting) {
				GuildSettingsWriter.writeSettings(settings);
			}
			return settings;
		} catch (ParseException | IOException e) {
			System.err.println("Error retrieving guild or guild settings for guild: " + guildID);
		}

		return settings;
	}

	private static boolean loadBools(GuildSettings settings, JsonObject jsonObject) {
		boolean needsWriting = false;
		if (!isObjectNull(JSONKeys.SETTINGS_ENABLE_WELCOME, jsonObject)) {
			settings.enableWelcome = getBool(JSONKeys.SETTINGS_ENABLE_WELCOME, jsonObject);
		} else {
			needsWriting = true;
		}

		if (!isObjectNull(JSONKeys.SETTINGS_ENABLE_WELCOME_ROLE, jsonObject)) {
			settings.enableWelcomeRole = getBool(JSONKeys.SETTINGS_ENABLE_WELCOME_ROLE, jsonObject);
		} else {
			needsWriting = true;
		}

		if (!isObjectNull(JSONKeys.SETTINGS_SPARTANKICK, jsonObject)) {
			settings.spartankick = getBool(JSONKeys.SETTINGS_SPARTANKICK, jsonObject);
		} else {
			needsWriting = true;
		}

		return needsWriting;
	}

	private static boolean loadStrings(GuildSettings settings, JsonObject jsonObject) {
		boolean needsWriting = false;
		if (!isObjectNull(JSONKeys.SETTINGS_COMMAND_PREFIX, jsonObject)) {
			settings.commandPrefix = getString(JSONKeys.SETTINGS_COMMAND_PREFIX, jsonObject);
		} else {
			needsWriting = true;
		}

		if (!isObjectNull(JSONKeys.SETTINGS_WELCOME_MESSAGE, jsonObject)) {
			settings.welcomeMessage = getString(JSONKeys.SETTINGS_WELCOME_MESSAGE, jsonObject);
		} else {
			needsWriting = true;
		}

		return needsWriting;
	}

	private static boolean loadLongs(GuildSettings settings, JsonObject jsonObject) {
		boolean needsWriting = false;
		if (!isObjectNull(JSONKeys.SETTINGS_WELCOME_CHANNEL, jsonObject)) {
			settings.welcomeChannel = getLong(JSONKeys.SETTINGS_WELCOME_CHANNEL, jsonObject);
		} else {
			needsWriting = true;
		}

		if (!isObjectNull(JSONKeys.SETTINGS_WELCOME_ROLE, jsonObject)) {
			settings.welcomeRole = getLong(JSONKeys.SETTINGS_WELCOME_ROLE, jsonObject);
		} else {
			needsWriting = true;
		}

		if (!isObjectNull(JSONKeys.SETTINGS_LOG_CHANNEL, jsonObject)) {
			settings.logChannel = getLong(JSONKeys.SETTINGS_LOG_CHANNEL, jsonObject);
		} else {
			needsWriting = true;
		}

		if (!isObjectNull(JSONKeys.SETTINGS_MUTE_ROLE, jsonObject)) {
			settings.muteRole = getLong(JSONKeys.SETTINGS_MUTE_ROLE, jsonObject);
		} else {
			needsWriting = true;
		}

		if (!isObjectNull(JSONKeys.SETTINGS_MUTE_CHANNEL, jsonObject)) {
			settings.muteChannel = getLong(JSONKeys.SETTINGS_MUTE_CHANNEL, jsonObject);
		} else {
			needsWriting = true;
		}

		if (!isObjectNull(JSONKeys.SETTINGS_MUSIC_CHANNEL, jsonObject)) {
			settings.musicchannel = getLong(JSONKeys.SETTINGS_MUSIC_CHANNEL, jsonObject);
		} else {
			needsWriting = true;
		}

		if (!isObjectNull(JSONKeys.SETTINGS_ROLE_PERMISSION0, jsonObject)) {
			settings.permission0 = getLong(JSONKeys.SETTINGS_ROLE_PERMISSION0, jsonObject);
		} else {
			needsWriting = true;
		}

		if (!isObjectNull(JSONKeys.SETTINGS_ROLE_PERMISSION1, jsonObject)) {
			settings.permission1 = getLong(JSONKeys.SETTINGS_ROLE_PERMISSION1, jsonObject);
		} else {
			needsWriting = true;
		}

		if (!isObjectNull(JSONKeys.SETTINGS_ROLE_PERMISSION2, jsonObject)) {
			settings.permission2 = getLong(JSONKeys.SETTINGS_ROLE_PERMISSION2, jsonObject);
		} else {
			needsWriting = true;
		}

		return needsWriting;
	}

	private static boolean isObjectNull(String key, JsonObject jsonObject) {
		return jsonObject.get(key) == null;
	}

	private static boolean getBool(String key, JsonObject jsonObject) {
		return Boolean.parseBoolean(jsonObject.get(key).asString());
	}

	private static long getLong(String key, JsonObject jsonObject) {
		return Long.parseLong(jsonObject.get(key).asString());
	}

	private static String getString(String key, JsonObject jsonObject) {
		return jsonObject.get(key).asString();
	}

	public static String getSettingsValue(long guildID, String key) {
		Reader reader;
		String retrievedValue = "";
		try {
			reader = new BufferedReader(new FileReader(JSONKeys.SETTINGS_PATH + guildID + ".json"));
			JsonObject jsonObject = Json.parse(reader).asObject();

			if (!isObjectNull(key, jsonObject)) {
				getString(key, jsonObject);
			}

			reader.close();

			return retrievedValue;
		} catch (ParseException | IOException e) {
			System.err.println("Error retrieving key, " + key + " for guild, " + guildID);
		}

		return retrievedValue;
	}

	public static boolean settingsFileExists(long guildID) {
		String path = JSONKeys.SETTINGS_PATH + guildID + ".json";
		return new File(path).exists();
	}

}