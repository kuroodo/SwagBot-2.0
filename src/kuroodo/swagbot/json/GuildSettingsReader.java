package kuroodo.swagbot.json;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class GuildSettingsReader {
	
	public static String getSettingsValue(String guildID, String key) {
		String retrievedValue = "";

		Reader reader;
		try {
			reader = new FileReader(guildID + ".json");
			JsonObject object = Json.parse(reader).asObject();

			retrievedValue = object.get(key).asString();
		} catch (IOException e) {
			System.err.println("ERROR: Could not get key: " + key + "\nEnsure that the key or the file "
					+ guildID + ".json" + " exist");
		}

		return retrievedValue;
	}
}
