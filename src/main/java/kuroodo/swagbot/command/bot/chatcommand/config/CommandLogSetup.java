package kuroodo.swagbot.command.bot.chatcommand.config;

import com.eclipsesource.json.ParseException;

import kuroodo.swagbot.SwagBot;
import kuroodo.swagbot.command.CommandKeys;
import kuroodo.swagbot.command.bot.chatcommand.ChatCommand;
import kuroodo.swagbot.guild.GuildLogSettings;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.json.GuildLogSettingsReader;
import kuroodo.swagbot.json.GuildLogSettingsWriter;
import kuroodo.swagbot.json.JSONKeys;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandLogSetup extends ChatCommand {
	GuildLogSettings settings;

	@Override
	protected void setCommandPermissiosn() {
		requiredPermissions.add(Permission.MESSAGE_WRITE);
		isPermission0 = true;
	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!selfHasPermissions()) {
			return;
		}

		// Check if settings file for guild exists
		if (!GuildLogSettingsReader.settingsFileExists(event.getGuild().getIdLong())) {
			sendMessage(BotUtility.quotifyText("Error finding this server's configuration file."));
			if (!SwagBot.verifyGuildLogsTemplateFile()) {
				sendMessage(BotUtility
						.quotifyText("There is an issue managing server configuration. A fix is being worked on."));
			}
			return;
		}

		// 3 params should be <prefix>logs key value
		int expectedParamsLength = 3;

		// Does member have a permission0/1/2 role
		if (!memberHasPermissions(event.getMember())) {
			sendNoPermissionsMessage();
			return;
			// If not enough params
		} else if (commandParams.length < expectedParamsLength) {
			sendEmbed(getCommandInfoAsEmbed());
			return;
		}

		settings = GuildManager.getGuild(event.getGuild()).logSettings;
		modifyLogSettings();
		saveGuildSettings();
	}

	private void modifyLogSettings() {
		switch (commandParams[1].toLowerCase()) {
		case JSONKeys.LOGSETTINGS_NICKNAME:
			updateNickname();
			return;
		case JSONKeys.LOGSETTINGS_MEMBE_ROLES:
			updateMemberRoles();
			return;
		case JSONKeys.LOGSETTINGS_ROLE_EDIT:
			updateRoleEdit();
			return;
		default:
			sendKeyNotFoundMessage();
		}
	}

	private void updateNickname() {
		if (commandParams[2].equals("-1")) {
			settings.nicknameLogging = false;
		} else {

			try {
				settings.nicknameLogging = Boolean.parseBoolean(commandParams[2]);
			} catch (NumberFormatException | ParseException e) {
				sendFormatErrorMessage();
				return;
			}
		}
		String result = settings.nicknameLogging ? "enabled" : "disabled";
		sendMessage(BotUtility.codifyText("Nickname logging as been " + result));
	}

	private void updateMemberRoles() {
		if (commandParams[2].equals("-1")) {
			settings.memberRoleLogging = false;
		} else {

			try {
				settings.memberRoleLogging = Boolean.parseBoolean(commandParams[2]);
			} catch (NumberFormatException | ParseException e) {
				sendFormatErrorMessage();
				return;
			}

		}
		String result = settings.memberRoleLogging ? "enabled" : "disabled";
		sendMessage(BotUtility.codifyText("Member role logging as been " + result));
	}

	private void updateRoleEdit() {
		if (commandParams[2].equals("-1")) {
			settings.roleEditLogging = false;
		} else {

			try {
				settings.roleEditLogging = Boolean.parseBoolean(commandParams[2]);
			} catch (NumberFormatException | ParseException e) {
				sendFormatErrorMessage();
				return;
			}
		}

		String result = settings.memberRoleLogging ? "enabled" : "disabled";
		sendMessage(BotUtility.codifyText("Member role logging as been " + result));
	}

	private void saveGuildSettings() {
		GuildLogSettingsWriter.writeSettings(settings);
		GuildManager.reloadGuildLogSettings(settings.guildID);
	}

	private void sendFormatErrorMessage() {
		sendMessage(BotUtility.quotifyText("Parameters incorrect.\n")
				+ BotUtility.quotifyText("Correct format: " + commandPrefix + CommandKeys.COMMAND_LOGSETUP
						+ "<key> <value> AND ensure that the key AND value are correct. Enter " + commandPrefix
						+ CommandKeys.COMMAND_LOGSHELP + "for more information"));
	}

	private void sendKeyNotFoundMessage() {
		sendMessage(BotUtility
				.quotifyText("Incorrect key entered. Enter " + commandPrefix + "logshelp for more information"));
	}

	@Override
	public String commandDescription() {
		return "Use this command to set up what type of log messages the bot will send to the log channel if a log channel is setup."
				+ "\nUse " + commandPrefix + CommandKeys.COMMAND_LOGSHELP
				+ " for more information, and for information about keys and values.";
	}

	@Override
	public String commandFormat() {
		return "Usage: " + commandPrefix + CommandKeys.COMMAND_LOGSETUP + " <key> <value>";
	}

	@Override
	public String commandUsageExample() {
		return commandPrefix + CommandKeys.COMMAND_LOGSETUP + " roleedits false";
	}
}
