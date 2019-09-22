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
			JsonObject object = Json.parse(reader).asObject();

			settings = new GuildSettings(guildID);
			// booleans
			settings.enableWelcome = Boolean.parseBoolean(object.get(JSONKeys.SETTINGS_ENABLE_WELCOME).asString());
			settings.enableWelcomeRole = Boolean
					.parseBoolean(object.get(JSONKeys.SETTINGS_ENABLE_WELCOME_ROLE).asString());
			settings.spartankick = Boolean.parseBoolean(object.get(JSONKeys.SETTINGS_SPARTANKICK).asString());

			// Strings
			settings.commandPrefix = object.get(JSONKeys.SETTINGS_COMMAND_PREFIX).asString();
			settings.welcomeMessage = object.get(JSONKeys.SETTINGS_WELCOME_MESSAGE).asString();

			// Longs
			settings.welcomeChannel = Long.parseLong(object.get(JSONKeys.SETTINGS_WELCOME_CHANNEL).asString());
			settings.welcomeRole = Long.parseLong(object.get(JSONKeys.SETTINGS_WELCOME_ROLE).asString());
			settings.logChannel = Long.parseLong(object.get(JSONKeys.SETTINGS_LOG_CHANNEL).asString());
			settings.muteRole = Long.parseLong(object.get(JSONKeys.SETTINGS_MUTE_ROLE).asString());
			settings.muteChannel = Long.parseLong(object.get(JSONKeys.SETTINGS_MUTE_CHANNEL).asString());
			settings.muteChannel = Long.parseLong(object.get(JSONKeys.SETTINGS_MUSIC_CHANNEL).asString());
			settings.permission0 = Long.parseLong(object.get(JSONKeys.SETTINGS_ROLE_PERMISSION0).asString());
			settings.permission1 = Long.parseLong(object.get(JSONKeys.SETTINGS_ROLE_PERMISSION1).asString());
			settings.permission2 = Long.parseLong(object.get(JSONKeys.SETTINGS_ROLE_PERMISSION2).asString());

			reader.close();
			return settings;
		} catch (ParseException | IOException e) {
			System.err.println("Error retrieving guild or guild settings for guild: " + guildID);
		}

		return settings;
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