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

public class ConfigReader {
	public static String getConfigValue(String key) {
		String retrievedValue = "";

		Reader reader;
		try {
			reader = new BufferedReader(new FileReader(JSONKeys.CONFIG_FILE_NAME));
			JsonObject object = Json.parse(reader).asObject();

			if (object.get(key) == null) {
				throw new IOException();
			}

			retrievedValue = object.get(key).asString();
			reader.close();
		} catch (IOException e) {
			System.out.println("ERROR: Could not get key: " + key + "\nEnsure that the key or the file "
					+ JSONKeys.CONFIG_FILE_NAME + " exist");
		}

		return retrievedValue;
	}

	public static boolean configFileExists() {
		return new File(JSONKeys.CONFIG_FILE_NAME).exists();
	}
}
