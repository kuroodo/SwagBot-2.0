package kuroodo.swagbot.json;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import kuroodo.swagbot.config.GuildSettings;

public class GuildSettingsReader {

	public static GuildSettings loadSettingsFile(long guildID) {
		Reader reader;
		try {
			reader = new FileReader(JSONKeys.SETTINGS_PATH + guildID + ".json");
			JsonObject object = Json.parse(reader).asObject();

			GuildSettings settings = new GuildSettings(guildID);
			// booleans
			settings.enableWelcome = Boolean.parseBoolean(object.get(JSONKeys.SETTINGS_ENABLE_WELCOME).asString());
			settings.enableWelcomeRole = Boolean
					.parseBoolean(object.get(JSONKeys.SETTINGS_ENABLE_WLECOME_ROLE).asString());

			// Strings
			settings.welcomeMessage = object.get(JSONKeys.SETTINGS_WELCOME_MESSAGE).asString();

			// Longs
			settings.welcomeChannel = Long.parseLong(object.get(JSONKeys.SETTINGS_WELCOME_CHANNEL).asString());
			settings.welcomeRole = Long.parseLong(object.get(JSONKeys.SETTINGS_WELCOME_ROLE).asString());
			settings.logChannel = Long.parseLong(object.get(JSONKeys.SETTINGS_LOG_CHANNEL).asString());
			settings.muteRole = Long.parseLong(object.get(JSONKeys.SETTINGS_MUTE_ROLE).asString());
			settings.muteChannel = Long.parseLong(object.get(JSONKeys.SETTINGS_MUTE_CHANNEL).asString());
			settings.rolePermission0 = Long.parseLong(object.get(JSONKeys.SETTINGS_ROLE_PERMISSION0).asString());
			settings.rolePermission1 = Long.parseLong(object.get(JSONKeys.SETTINGS_ROLE_PERMISSION1).asString());
			settings.rolePermission2 = Long.parseLong(object.get(JSONKeys.SETTINGS_ROLE_PERMISSION2).asString());

			return settings;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error retrieving guild or guild settings for guild: " + guildID);
		}

		return null;
	}

	public static String getSettingsValue(long guildID, String key) {
		String retrievedValue = "";

		Reader reader;
		try {
			reader = new FileReader(JSONKeys.SETTINGS_PATH + guildID + ".json");
			JsonObject object = Json.parse(reader).asObject();

			retrievedValue = object.get(key).asString();
		} catch (IOException e) {
			System.err.println("ERROR: Could not get key: " + key + "\nEnsure that the key or the file " + guildID
					+ ".json" + " exist");
		}

		return retrievedValue;
	}
}