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
package kuroodo.swagbot.command.bot.chatcommand.config;

import kuroodo.swagbot.command.CommandKeys;
import kuroodo.swagbot.command.bot.chatcommand.ChatCommand;
import kuroodo.swagbot.guild.GuildLogSettings;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.json.JSONKeys;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandLogSetupHelp extends ChatCommand {

	@Override
	protected void setCommandPermissiosn() {
		requiredPermissions.add(Permission.MESSAGE_WRITE);
		requiredPermissions.add(Permission.MESSAGE_EMBED_LINKS);
		isPermission0 = true;
	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);
		if (!selfHasPermissions() || !memberHasPermissions(event.getMember())) {
			return;
		}

		// If asking for the setup keys, <prefix>setuphelp keys
		if (commandParams.length > 1 && commandParams[1].toLowerCase().equals("keys")) {
			sendKeyDescriptions();
		} else {
			sendCurrentSettings();
		}
	}

	private void sendKeyDescriptions() {
		GuildLogSettings settings = GuildManager.getGuild(event.getGuild()).logSettings;
		sendEmbed(getDescriptionsEmbed(settings));
	}

	private void sendCurrentSettings() {
		GuildLogSettings settings = GuildManager.getGuild(event.getGuild()).logSettings;
		sendEmbed(getCurrentSettingsEmbed(settings));
	}

	public EmbedBuilder getDescriptionsEmbed(GuildLogSettings settings) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("List of Keys and Values");
		eb.setDescription("Use " + commandPrefix + CommandKeys.COMMAND_LOGSETUP + "<key> <value> to edit values");
		eb = getKeyDescriptions(settings, eb);

		return eb;
	}

	public EmbedBuilder getCurrentSettingsEmbed(GuildLogSettings settings) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Current Log Settings");
		eb = getCurrentSettings(settings, eb);
		eb.setDescription(
				"For information, see the [manual](https://github.com/kuroodo/SwagBot-2.0/blob/master/help.txt)");
		eb.setFooter("To edit these values, use " + commandPrefix + CommandKeys.COMMAND_LOGSETUP + " <key> <value>\n"
				+ "To get a description of each key and their values, enter: " + commandPrefix
				+ CommandKeys.COMMAND_LOGSHELP + " keys");
		return eb;
	}

	public EmbedBuilder getKeyDescriptions(GuildLogSettings settings, EmbedBuilder eb) {
		eb.addField(JSONKeys.LOGSETTINGS_NICKNAME,
				"Enable logging when a member changes their nickname. Accepted values: true or false", false);
		eb.addField(JSONKeys.LOGSETTINGS_MEMBE_ROLES,
				"Enable logging when a member is given or revoked roles. Accepted values: true or false", false);
		eb.addField(JSONKeys.LOGSETTINGS_MESSAGE_DELETE,
				"Enable logging when a message is deleted. Accepted values: true or false", false);

		return eb;
	}

	public EmbedBuilder getCurrentSettings(GuildLogSettings settings, EmbedBuilder eb) {

		// Enable Welcome Message
		eb.addField("Nickname logging", "" + settings.nicknameLogging, false);
		eb.addField("Member rolee logging", "" + settings.memberRoleLogging, false);
		eb.addField("Message deletee logging", "" + settings.messageDeleteLogging, false);

		return eb;
	}

	@Override
	public String commandDescription() {
		return "Insight on how to set up and configure the bot. Use " + commandPrefix + "setup to modify these values";
	}

	@Override
	public String commandFormat() {
		return "To get current key values: " + commandPrefix + CommandKeys.COMMAND_LOGSHELP
				+ "\nTo get information on a key and it's values: " + commandPrefix + CommandKeys.COMMAND_LOGSHELP
				+ " keys";
	}

	@Override
	public String commandUsageExample() {
		return commandPrefix + CommandKeys.COMMAND_LOGSHELP + "\n" + commandPrefix + CommandKeys.COMMAND_LOGSHELP
				+ " keys";
	}
}