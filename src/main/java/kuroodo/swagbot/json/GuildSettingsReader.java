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
		boolean needsWriting = false;
		try {
			reader = new BufferedReader(new FileReader(JSONKeys.SETTINGS_PATH + guildID + ".json"));
			JsonObject jsObject = Json.parse(reader).asObject();
			settings = new GuildSettings(guildID);

			// booleans
			if (!isObjectNull(JSONKeys.SETTINGS_ENABLE_WELCOME, jsObject)) {
				settings.enableWelcome = getBool(JSONKeys.SETTINGS_ENABLE_WELCOME, jsObject);
			} else {
				needsWriting = true;
			}

			if (!isObjectNull(JSONKeys.SETTINGS_ENABLE_WELCOME_ROLE, jsObject)) {
				settings.enableWelcomeRole = getBool(JSONKeys.SETTINGS_ENABLE_WELCOME_ROLE, jsObject);
			} else {
				needsWriting = true;
			}

			if (!isObjectNull(JSONKeys.SETTINGS_SPARTANKICK, jsObject)) {
				settings.spartankick = getBool(JSONKeys.SETTINGS_SPARTANKICK, jsObject);
			} else {
				needsWriting = true;
			}

			// Strings
			if (!isObjectNull(JSONKeys.SETTINGS_COMMAND_PREFIX, jsObject)) {
				settings.commandPrefix = getString(JSONKeys.SETTINGS_COMMAND_PREFIX, jsObject);
			} else {
				needsWriting = true;
			}

			if (!isObjectNull(JSONKeys.SETTINGS_WELCOME_MESSAGE, jsObject)) {
				settings.welcomeMessage = getString(JSONKeys.SETTINGS_WELCOME_MESSAGE, jsObject);
			} else {
				needsWriting = true;
			}

			// Longs
			if (!isObjectNull(JSONKeys.SETTINGS_WELCOME_CHANNEL, jsObject)) {
				settings.welcomeChannel = getLong(JSONKeys.SETTINGS_WELCOME_CHANNEL, jsObject);
			} else {
				needsWriting = true;
			}

			if (!isObjectNull(JSONKeys.SETTINGS_WELCOME_ROLE, jsObject)) {
				settings.welcomeRole = getLong(JSONKeys.SETTINGS_WELCOME_ROLE, jsObject);
			} else {
				needsWriting = true;
			}

			if (!isObjectNull(JSONKeys.SETTINGS_LOG_CHANNEL, jsObject)) {
				settings.logChannel = getLong(JSONKeys.SETTINGS_LOG_CHANNEL, jsObject);
			} else {
				needsWriting = true;
			}

			if (!isObjectNull(JSONKeys.SETTINGS_MUTE_ROLE, jsObject)) {
				settings.muteRole = getLong(JSONKeys.SETTINGS_MUTE_ROLE, jsObject);
			} else {
				needsWriting = true;
			}

			if (!isObjectNull(JSONKeys.SETTINGS_MUTE_CHANNEL, jsObject)) {
				settings.muteChannel = getLong(JSONKeys.SETTINGS_MUTE_CHANNEL, jsObject);
			} else {
				needsWriting = true;
			}

			if (!isObjectNull(JSONKeys.SETTINGS_MUSIC_CHANNEL, jsObject)) {
				settings.musicchannel = getLong(JSONKeys.SETTINGS_MUSIC_CHANNEL, jsObject);
			} else {
				needsWriting = true;
			}

			if (!isObjectNull(JSONKeys.SETTINGS_ROLE_PERMISSION0, jsObject)) {
				settings.permission0 = getLong(JSONKeys.SETTINGS_ROLE_PERMISSION0, jsObject);
			} else {
				needsWriting = true;
			}

			if (!isObjectNull(JSONKeys.SETTINGS_ROLE_PERMISSION1, jsObject)) {
				settings.permission1 = getLong(JSONKeys.SETTINGS_ROLE_PERMISSION1, jsObject);
			} else {
				needsWriting = true;
			}

			if (!isObjectNull(JSONKeys.SETTINGS_ROLE_PERMISSION2, jsObject)) {
				settings.permission2 = getLong(JSONKeys.SETTINGS_ROLE_PERMISSION2, jsObject);
			} else {
				needsWriting = true;
			}

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
		String retrievedValue = "";

		Reader reader;
		try {
			reader = new BufferedReader(new FileReader(JSONKeys.SETTINGS_PATH + guildID + ".json"));
			JsonObject object = Json.parse(reader).asObject();

			retrievedValue = object.get(key).asString();
			reader.close();
		} catch (IOException e) {
			System.err.println("ERROR: Could not get key: " + key + "\nEnsure that the key or the file " + guildID
					+ ".json" + " exist");
		}

		return retrievedValue;
	}

	public static boolean settingsFileExists(long guildID) {
		String path = JSONKeys.SETTINGS_PATH + guildID + ".json";
		return new File(path).exists();
	}

}