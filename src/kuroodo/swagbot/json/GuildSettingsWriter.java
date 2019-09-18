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
		Reader reader;
		try {
			String path = JSONKeys.SETTINGS_PATH + guild.guildID + ".json";
			File settingsFile = new File(path);
			// Create a new settings folder from template
			FileUtils.copyFile(new File(JSONKeys.SETTINGS_PATH + JSONKeys.TEMPLATE_NAME), settingsFile);

			// Read and store the contents of the file into a JsonObject
			reader = new FileReader(settingsFile);
			JsonObject object = Json.parse(reader).asObject();

			object.set(JSONKeys.SETTINGS_GUILD_ID, guild.guildID);
			object.set(JSONKeys.SETTINGS_ENABLE_WELCOME, guild.enableWelcome);
			object.set(JSONKeys.SETTINGS_WELCOME_CHANNEL, guild.welcomeChannel);
			object.set(JSONKeys.SETTINGS_WELCOME_MESSAGE, guild.welcomeMessage);
			object.set(JSONKeys.SETTINGS_ENABLE_WLECOME_ROLE, guild.enableWelcomeRole);
			object.set(JSONKeys.SETTINGS_WELCOME_ROLE, guild.welcomeRole);
			object.set(JSONKeys.SETTINGS_LOG_CHANNEL, guild.logChannel);
			object.set(JSONKeys.SETTINGS_MUTE_ROLE, guild.muteRole);
			object.set(JSONKeys.SETTINGS_MUTE_CHANNEL, guild.muteChannel);
			object.set(JSONKeys.SETTINGS_ROLE_PERMISSION0, guild.rolePermission0);
			object.set(JSONKeys.SETTINGS_ROLE_PERMISSION1, guild.rolePermission1);
			object.set(JSONKeys.SETTINGS_ROLE_PERMISSION2, guild.rolePermission2);

			// Overwrite original file and paste contents
			FileWriter writer = new FileWriter(settingsFile, false);
			object.writeTo(writer, WriterConfig.PRETTY_PRINT);

			writer.flush();
			writer.close();
		} catch (IOException e) {
			System.err.println("ERROR: Could not get key: " + guild.guildID + "\nEnsure that the key or the file "
					+ JSONKeys.CONFIG_FILE_NAME + " exist");
		}
	}
}
