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
package kuroodo.swagbot.json;

public class JSONKeys {
	// Config.json
	public static final String CONFIG_FILE_NAME = "config.json";
	public static final String CONFIG_BOT_TOKEN = "bottoken";
	public static final String CONIG_BOT_OWNER = "botownerid";

	// Guild Settings
	public static final String SETTINGS_PATH = "guildsettings/";
	public static final String TEMPLATE_NAME = "_TEMPLATE.json";
	public static final String SETTINGS_GUILD_ID = "guildid";
	public static final String SETTINGS_COMMAND_PREFIX = "commandprefix";
	public static final String SETTINGS_ENABLE_WELCOME = "enablewelcome";
	public static final String SETTINGS_WELCOME_CHANNEL = "welcomechannel";
	public static final String SETTINGS_WELCOME_MESSAGE = "welcomemessage";
	public static final String SETTINGS_ENABLE_WELCOME_ROLE = "enablewelcomerole";
	public static final String SETTINGS_WELCOME_ROLE = "welcomerole";
	public static final String SETTINGS_LOG_CHANNEL = "logchannel";
	public static final String SETTINGS_MUTE_ROLE = "muterole";
	public static final String SETTINGS_MUTE_CHANNEL = "mutechannel";
	public static final String SETTINGS_MUSIC_CHANNEL = "musicchannel";
	public static final String SETTINGS_SPARTANKICK = "spartankick";
	public static final String SETTINGS_ROLE_PERMISSION0 = "permission0";
	public static final String SETTINGS_ROLE_PERMISSION1 = "permission1";
	public static final String SETTINGS_ROLE_PERMISSION2 = "permission2";

	// Magic Ball
	public static final String MAGICBALL_FILE_NAME = "magicball.json";
	public static final String MAGICBALL_PASSIVE_RESPONSE = "nullpassive";
	public static final String MAGICBALL_OTHER_RESPONSE = "nullother";
}
