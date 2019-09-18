package kuroodo.swagbot.json;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.io.FileUtils;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.WriterConfig;

import kuroodo.swagbot.config.GuildSettings;

public class GuildSettingsWriter {

	public static void createNewFile(GuildSettings guild) {
		try {
			String path = JSONKeys.SETTINGS_PATH + guild.guildID + ".json";

			// Create a new settings folder from template
			// TODO: Check if template file doesn't exist
			FileUtils.copyFile(new File(JSONKeys.SETTINGS_PATH + JSONKeys.TEMPLATE_NAME), new File(path));
			writeSettings(guild);
			
		} catch (IOException e) {
			System.err.println("ERROR: Error trying to create new file : " + guild.guildID
					+ ".json\nEnsure that the corresponding files exist");
		}
	}

	
	public static void writeSettings(GuildSettings guild) {
		try {
			Reader reader;
			String path = JSONKeys.SETTINGS_PATH + guild.guildID + ".json";
			File settingsFile = new File(path);

			// Read and store the contents of the file into a JsonObject
			reader = new FileReader(settingsFile);
			JsonObject object = Json.parse(reader).asObject();

			object.set(JSONKeys.SETTINGS_GUILD_ID, Long.toString(guild.guildID));
			object.set(JSONKeys.SETTINGS_ENABLE_WELCOME, Boolean.toString(guild.enableWelcome));
			object.set(JSONKeys.SETTINGS_WELCOME_CHANNEL, Long.toString(guild.welcomeChannel));
			object.set(JSONKeys.SETTINGS_WELCOME_MESSAGE, guild.welcomeMessage);
			object.set(JSONKeys.SETTINGS_ENABLE_WLECOME_ROLE, Boolean.toString(guild.enableWelcomeRole));
			object.set(JSONKeys.SETTINGS_WELCOME_ROLE, Long.toString(guild.welcomeRole));
			object.set(JSONKeys.SETTINGS_LOG_CHANNEL, Long.toString(guild.logChannel));
			object.set(JSONKeys.SETTINGS_MUTE_ROLE, Long.toString(guild.muteRole));
			object.set(JSONKeys.SETTINGS_MUTE_CHANNEL, Long.toString(guild.muteChannel));
			object.set(JSONKeys.SETTINGS_ROLE_PERMISSION0, Long.toString(guild.rolePermission0));
			object.set(JSONKeys.SETTINGS_ROLE_PERMISSION1, Long.toString(guild.rolePermission1));
			object.set(JSONKeys.SETTINGS_ROLE_PERMISSION2, Long.toString(guild.rolePermission2));

			// Overwrite original file and paste contents
			FileWriter writer = new FileWriter(settingsFile, false);
			object.writeTo(writer, WriterConfig.PRETTY_PRINT);

			writer.flush();
			writer.close();
		} catch (IOException e) {
			System.err.println("ERROR: Error trying to write to file : " + guild.guildID
					+ ".json\nEnsure that the corresponding keys or files exist");
		}
	}
	
	
	public static void writeSetting(long guildID, String key, String value) {
		try {
			Reader reader;
			String path = JSONKeys.SETTINGS_PATH + guildID + ".json";
			File settingsFile = new File(path);

			// Read and store the contents of the file into a JsonObject
			reader = new FileReader(settingsFile);
			JsonObject object = Json.parse(reader).asObject();

			object.set(key, value);

			// Overwrite original file and paste contents
			FileWriter writer = new FileWriter(settingsFile, false);
			object.writeTo(writer, WriterConfig.PRETTY_PRINT);

			writer.flush();
			writer.close();
		} catch (IOException e) {
			System.err.println("ERROR: Error trying to write to file " + guildID + ".json\nEnsure that key " + key
					+ "or file exists");
		}
	}
}
