/**
 * Minimal-Json https://github.com/ralfstx/minimal-json/blob/master/README.md
 * Using under the MIT license https://opensource.org/licenses/MIT
 */

package kuroodo.swagbot.json;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class JSONReader {
	public static String getConfigValue(String key) {
		String retrievedValue = "";

		Reader reader;
		try {
			reader = new FileReader(ConfigKeys.CONFIG_FILE_NAME);
			JsonObject object = Json.parse(reader).asObject();

			retrievedValue = object.get(key).asString();
		} catch (IOException e) {
			System.err.println("ERROR: Could not get key: " + key + "\nEnsure that the key or the file "
					+ ConfigKeys.CONFIG_FILE_NAME + " exist");
		}

		return retrievedValue;
	}
}
