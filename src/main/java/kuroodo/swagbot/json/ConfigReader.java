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
