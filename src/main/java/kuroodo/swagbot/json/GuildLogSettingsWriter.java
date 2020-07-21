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

import kuroodo.swagbot.guild.GuildLogSettings;

public class GuildLogSettingsWriter {

	public static void createNewFile(GuildLogSettings guild) {
		try {
			String path = JSONKeys.SETTINGS_PATH + guild.guildID + "_logs.json";
			// If template file is valid
			if (isTemplateExist()) {
				File templateFile = new File(JSONKeys.SETTINGS_PATH + JSONKeys.TEMPLATE_LOGSETTINGS_NAME);
				// Create a new settings file from template
				FileUtils.copyFile(templateFile, new File(path));
				writeSettings(guild);
			} else {
				System.err.println("***************\nERROR: LOGS TEMPLATE FILE DOES NOT EXIST\n***************");
			}

		} catch (IOException e) {
			System.err.println("ERROR: Error trying to create new file : " + guild.guildID
					+ ".json\nEnsure that the corresponding files exist");
		}
	}

	public static void writeSettings(GuildLogSettings guild) {
		String path = JSONKeys.SETTINGS_PATH + guild.guildID + "_logs.json";
		File settingsFile = new File(path);
		try {
			Reader reader;

			// Read and store the contents of the file into a JsonObject
			reader = new BufferedReader(new FileReader(settingsFile));
			JsonObject object = Json.parse(reader).asObject();

			object.set(JSONKeys.SETTINGS_GUILD_ID, Long.toString(guild.guildID));
			object.set(JSONKeys.LOGSETTINGS_NICKNAME, Boolean.toString(guild.nicknameLogging));
			object.set(JSONKeys.LOGSETTINGS_MEMBE_ROLES, Boolean.toString(guild.memberRoleLogging));
			object.set(JSONKeys.LOGSETTINGS_MESSAGE_DELETE, Boolean.toString(guild.messageDeleteLogging));
			object.set(JSONKeys.LOGSETTINGS_MEMBERJOIN, Boolean.toString(guild.memberJoinLogging));
			object.set(JSONKeys.LOGSETTINGS_MEMBERLEAVE, Boolean.toString(guild.memberLeaveLogging));

			// Overwrite original file and paste contents
			FileWriter writer = new FileWriter(settingsFile, false);
			object.writeTo(writer, WriterConfig.PRETTY_PRINT);

			writer.flush();
			writer.close();
			reader.close();
		} catch (ParseException | IOException e) {
			System.err.println("ERROR: Error trying to write to file : " + guild.guildID
					+ "_logs.json\nEnsure that the corresponding keys or files exist, or that the file valid json");
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
			System.err.println("ERROR: Error trying to write to file " + guildID + "_logs.json\nEnsure that key " + key
					+ "or file exists");
		}
	}

	public static boolean isTemplateExist() {
		return new File(JSONKeys.SETTINGS_PATH + JSONKeys.TEMPLATE_LOGSETTINGS_NAME).exists();
	}
}
