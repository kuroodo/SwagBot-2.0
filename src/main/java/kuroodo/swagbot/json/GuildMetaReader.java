/*
 * Copyright 2019 Leandro Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

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
import com.eclipsesource.json.ParseException;

public class GuildMetaReader {

	public static String getValue(long guildID, String key) {
		Reader reader;
		String retrievedValue = "";
		try {
			reader = new BufferedReader(new FileReader(JSONKeys.SETTINGS_PATH + guildID + "_meta.json"));
			JsonObject jsonObject = Json.parse(reader).asObject();

			if (!isObjectNull(key, jsonObject)) {
				retrievedValue = getString(key, jsonObject);
			}

			reader.close();
			return retrievedValue;
		} catch (ParseException | IOException e) {
			System.err.println("Error retrieving key, " + key + " for guild, " + guildID);
		}
		System.out.println("ooo");
		return retrievedValue;
	}

	private static boolean isObjectNull(String key, JsonObject jsonObject) {
		return jsonObject.get(key) == null;
	}

	private static String getString(String key, JsonObject jsonObject) {
		return jsonObject.get(key).asString();
	}

	public static boolean metaFileExists(long guildID) {
		String path = JSONKeys.SETTINGS_PATH + guildID + "_meta.json";
		return new File(path).exists();
	}

}