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
			JsonObject object = Json.parse(reader).asObject();

			settings = new GuildLogSettings(guildID);
			settings.nicknameLogging = Boolean.parseBoolean(object.get(JSONKeys.LOGSETTINGS_NICKNAME).asString());
			settings.memberRoleLogging = Boolean.parseBoolean(object.get(JSONKeys.LOGSETTINGS_MEMBE_ROLES).asString());
			settings.roleEditLogging = Boolean.parseBoolean(object.get(JSONKeys.LOGSETTINGS_ROLE_EDIT).asString());
			settings.messageDeleteLogging = Boolean
					.parseBoolean(object.get(JSONKeys.LOGSETTINGS_MESSAGE_DELETE).asString());

			reader.close();
			return settings;
		} catch (ParseException | IOException e) {
			System.err.println("Error retrieving guild or guild LOG settings for guild: " + guildID);
		}

		return settings;
	}

	public static String getSettingsValue(long guildID, String key) {
		String retrievedValue = "";

		Reader reader;
		try {
			reader = new BufferedReader(new FileReader(JSONKeys.SETTINGS_PATH + guildID + "_logs.json"));
			JsonObject object = Json.parse(reader).asObject();

			retrievedValue = object.get(key).asString();
			reader.close();
		} catch (IOException e) {
			System.err.println("ERROR: Could not get key: " + key + "\nEnsure that the key or the file " + guildID
					+ "_logs.json" + " exist");
		}

		return retrievedValue;
	}

	public static boolean settingsFileExists(long guildID) {
		String path = JSONKeys.SETTINGS_PATH + guildID + "_logs.json";
		return new File(path).exists();
	}

}
