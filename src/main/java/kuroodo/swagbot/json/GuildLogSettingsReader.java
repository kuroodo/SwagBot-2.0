package kuroodo.swagbot.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.ParseException;

import kuroodo.swagbot.guild.GuildLogSettings;

public class GuildLogSettingsReader {

	public static GuildLogSettings loadSettingsFile(long guildID) {
		Reader reader;
		GuildLogSettings settings = new GuildLogSettings(guildID);
		try {
			reader = new BufferedReader(new FileReader(JSONKeys.SETTINGS_PATH + guildID + "_logs.json"));
			JsonObject jsonObject = Json.parse(reader).asObject();

			boolean needsWriting = false;
			// Load log settings by type
			needsWriting = loadBools(settings, jsonObject);

			reader.close();

			// Restore any objects that were missing or had issues
			if (needsWriting) {
				GuildLogSettingsWriter.writeSettings(settings);
			}
			return settings;
		} catch (ParseException | IOException e) {
			System.err.println("Error retrieving guild or guild LOG settings for guild: " + guildID);
		}

		return settings;
	}

	private static boolean loadBools(GuildLogSettings settings, JsonObject jsonObject) {
		boolean needsWriting = false;
		if (!isObjectNull(JSONKeys.LOGSETTINGS_NICKNAME, jsonObject)) {
			settings.nicknameLogging = getBool(JSONKeys.LOGSETTINGS_NICKNAME, jsonObject);
		} else {
			needsWriting = true;
		}

		if (!isObjectNull(JSONKeys.LOGSETTINGS_MEMBE_ROLES, jsonObject)) {
			settings.memberRoleLogging = getBool(JSONKeys.LOGSETTINGS_MEMBE_ROLES, jsonObject);
		} else {
			needsWriting = true;
		}

		if (!isObjectNull(JSONKeys.LOGSETTINGS_MESSAGE_DELETE, jsonObject)) {
			settings.messageDeleteLogging = getBool(JSONKeys.LOGSETTINGS_MESSAGE_DELETE, jsonObject);
		} else {
			needsWriting = true;
		}

		if (!isObjectNull(JSONKeys.LOGSETTINGS_MEMBERJOIN, jsonObject)) {
			settings.memberJoinLogging = getBool(JSONKeys.LOGSETTINGS_MEMBERJOIN, jsonObject);
		} else {
			needsWriting = true;
		}

		if (!isObjectNull(JSONKeys.LOGSETTINGS_MEMBERLEAVE, jsonObject)) {
			settings.memberLeaveLogging = getBool(JSONKeys.LOGSETTINGS_MEMBERLEAVE, jsonObject);
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

	private static String getString(String key, JsonObject jsonObject) {
		return jsonObject.get(key).asString();
	}

	public static String getLogsValue(long guildID, String key) {
		Reader reader;
		String retrievedValue = "";
		try {
			reader = new BufferedReader(new FileReader(JSONKeys.SETTINGS_PATH + guildID + "_logs.json"));
			JsonObject jsonObject = Json.parse(reader).asObject();

			if (!isObjectNull(key, jsonObject)) {
				retrievedValue = getString(key, jsonObject);
			}

			reader.close();

			return retrievedValue;
		} catch (ParseException | IOException e) {
			System.err.println("Error retrieving key, " + key + " for guild, " + guildID);
		}

		return retrievedValue;
	}

	public static boolean settingsFileExists(long guildID) {
		String path = JSONKeys.SETTINGS_PATH + guildID + "_logs.json";
		return new File(path).exists();
	}

}
