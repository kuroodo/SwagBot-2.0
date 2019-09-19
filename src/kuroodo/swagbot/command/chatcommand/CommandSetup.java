package kuroodo.swagbot.command.chatcommand;

import com.eclipsesource.json.ParseException;

import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import kuroodo.swagbot.json.GuildSettingsWriter;
import kuroodo.swagbot.json.JSONKeys;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandSetup extends ChatCommand {
	GuildSettings settings;

	public CommandSetup() {
		isPermission0 = true;
	}

	@Override
	protected void setCommandPermissiosn() {
		requiredPermissions.add(Permission.MESSAGE_WRITE);
	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!selfHasPermissions()) {
			return;
		}
		int expectedParamsLength = 3;
		if (!memberHasPermissions(GuildManager.getGuild(event.getGuild().getIdLong()), event.getMember())) {
			sendNoPermissionsMessage();
			return;
		} else if (commandParams.length < expectedParamsLength) {
			sendFormatErrorMessage();
			return;
		}

		settings = GuildManager.getGuild(event.getGuild());
		modifyGuildSettings();
		updateGuildSettings();
	}

	private void modifyGuildSettings() {
		switch (commandParams[1].toLowerCase()) {
		case JSONKeys.SETTINGS_COMMAND_PREFIX:
			updatePrefix();
			return;
		case JSONKeys.SETTINGS_ENABLE_WELCOME:
			updateEnableWelcome();
			return;
		case JSONKeys.SETTINGS_WELCOME_CHANNEL:
			updateWelcomeChannel();
			return;
		case JSONKeys.SETTINGS_WELCOME_MESSAGE:
			updateWelcomeMessage();
			return;
		case JSONKeys.SETTINGS_ENABLE_WELCOME_ROLE:
			updateEnableWelcomeRole();
			return;
		case JSONKeys.SETTINGS_WELCOME_ROLE:
			updateWelcomeRole();
			return;
		case JSONKeys.SETTINGS_LOG_CHANNEL:
			updateLogChannel();
			return;
		case JSONKeys.SETTINGS_MUTE_ROLE:
			updateMuteRole();
			return;
		case JSONKeys.SETTINGS_MUTE_CHANNEL:
			updateMuteChannel();
			return;
		case JSONKeys.SETTINGS_ROLE_PERMISSION0:
			updatePermission0();
			return;
		case JSONKeys.SETTINGS_ROLE_PERMISSION1:
			updatePermission1();
			return;
		case JSONKeys.SETTINGS_ROLE_PERMISSION2:
			updatePermission2();
			return;
		default:
			sendFormatErrorMessage();
		}
	}

	private void updatePrefix() {
		if (commandParams[2].equals("-1")) {
			settings.commandPrefix = "!";
		} else {
			settings.commandPrefix = commandParams[2];
		}
		sendMessage(BotUtility.codifyText("Command prefix has been set to " + settings.commandPrefix));
	}

	private void updateEnableWelcome() {
		if (commandParams[2].equals("-1")) {
			settings.enableWelcome = false;
			sendMessage(BotUtility.codifyText("Welcome message has been disabled."));
		} else {

			try {
				settings.enableWelcome = Boolean.parseBoolean(commandParams[2]);
			} catch (ParseException e) {
				sendFormatErrorMessage();
				return;
			}

			sendMessage(BotUtility.codifyText(
					"Welcome message has been enabled. Don't forget to set a welcome message if you haven't already"));
		}
	}

	private void updateWelcomeChannel() {
		if (commandParams[2].equals("-1")) {
			settings.welcomeChannel = 0;
			sendMessage(BotUtility.codifyText("Welcome channel removed from configuration"));
		} else {
			Long channelID;
			try {
				channelID = Long.parseLong(commandParams[2]);
			} catch (ParseException e) {
				sendFormatErrorMessage();
				return;
			}

			// Check if the channel exists
			if (GuildManager.getTextChannel(settings.guildID, channelID) != null) {
				settings.welcomeChannel = channelID;
			} else {
				printTextChannelErrorMessage();
				return;
			}

			sendMessage(BotUtility.codifyText("Welcome channel has been set to "
					+ GuildManager.getTextChannel(settings.guildID, settings.welcomeChannel)));
		}
	}

	private void updateWelcomeMessage() {
		if (commandParams[2].equals("-1")) {
			settings.welcomeMessage = "";
			sendMessage(BotUtility.codifyText("Welcome message has been removed"));
		} else {
			String msg = "";
			for (int i = 2; i < commandParams.length; i++) {
				msg += commandParams[i];

				// Don't add space to the last String
				if (i + 1 < commandParams.length) {
					msg += " ";
				}
			}
			settings.welcomeMessage = msg;
			sendMessage(BotUtility.codifyText("Welcome message has been set"));
		}
	}

	private void updateEnableWelcomeRole() {
		if (commandParams[2].equals("-1")) {
			settings.enableWelcomeRole = false;
			sendMessage(BotUtility.codifyText("Welcome role has been disabled."));
		} else {
			try {
				settings.enableWelcomeRole = Boolean.parseBoolean(commandParams[2]);
			} catch (ParseException e) {
				sendFormatErrorMessage();
				return;
			}
			sendMessage(BotUtility.codifyText(
					"Welcome role has been enabled. Don't forget to set a welcome role if you haven't already"));
		}
	}

	private void updateWelcomeRole() {
		if (commandParams[2].equals("-1")) {
			settings.welcomeRole = 0;
			sendMessage(BotUtility.codifyText("Welcome role removed from configuration"));
		} else {
			Long roleID;
			try {
				roleID = Long.parseLong(commandParams[2]);
			} catch (ParseException e) {
				sendFormatErrorMessage();
				return;
			}

			if (GuildManager.getRole(settings.guildID, roleID) != null) {
				settings.welcomeRole = roleID;
			} else {
				printRoleErrorMessage();
				return;
			}

			sendMessage(BotUtility.codifyText(
					"Welcome role has been set to " + GuildManager.getRole(settings.guildID, settings.welcomeRole)));
		}

	}

	private void updateLogChannel() {
		if (commandParams[2].equals("-1")) {
			settings.logChannel = 0;
			sendMessage(BotUtility.codifyText("Log channel removed from configuration"));
		} else {
			Long channelID;
			try {
				channelID = Long.parseLong(commandParams[2]);
			} catch (ParseException e) {
				sendFormatErrorMessage();
				return;
			}

			// Check if the channel exists
			if (GuildManager.getTextChannel(settings.guildID, channelID) != null) {
				settings.logChannel = channelID;
			} else {
				printTextChannelErrorMessage();
				return;
			}

			sendMessage(BotUtility.codifyText("Log channel has been set to "
					+ GuildManager.getTextChannel(settings.guildID, settings.logChannel)));
		}
	}

	private void updateMuteRole() {
		if (commandParams[2].equals("-1")) {
			settings.muteRole = 0;
			sendMessage(BotUtility.codifyText("Mute role removed from configuration"));
		} else {
			Long roleID;
			try {
				roleID = Long.parseLong(commandParams[2]);
			} catch (ParseException e) {
				sendFormatErrorMessage();
				return;
			}

			if (GuildManager.getRole(settings.guildID, roleID) != null) {
				settings.muteRole = roleID;
			} else {
				printRoleErrorMessage();
				return;
			}

			sendMessage(BotUtility.codifyText(
					"Mute role has been set to " + GuildManager.getRole(settings.guildID, settings.muteRole)));
		}

	}

	private void updateMuteChannel() {
		if (commandParams[2].equals("-1")) {
			settings.muteChannel = 0;
			sendMessage(BotUtility.codifyText("Mute channel removed from configuration"));
		} else {
			Long channelID;
			try {
				channelID = Long.parseLong(commandParams[2]);
			} catch (ParseException e) {
				sendFormatErrorMessage();
				return;
			}

			// Check if the channel exists
			if (GuildManager.getVoiceChannel(settings.guildID, channelID) != null) {
				settings.muteChannel = channelID;
			} else {
				printVoiceChannelErrorMessage();
				return;
			}

			sendMessage(BotUtility.codifyText("Mute channel has been set to "
					+ GuildManager.getVoiceChannel(settings.guildID, settings.muteChannel)));
		}
	}

	private void updatePermission0() {
		if (commandParams[2].equals("-1")) {
			settings.rolePermission0 = 0;
			sendMessage(BotUtility.codifyText("Permission0 role removed from configuration"));
		} else {
			Long roleID;
			try {
				roleID = Long.parseLong(commandParams[2]);
			} catch (ParseException e) {
				sendFormatErrorMessage();
				return;
			}

			if (GuildManager.getRole(settings.guildID, roleID) != null) {
				settings.rolePermission0 = roleID;
			} else {
				printRoleErrorMessage();
				return;
			}

			sendMessage(BotUtility.codifyText("Permission0 role has been set to "
					+ GuildManager.getRole(settings.guildID, settings.rolePermission0)));
		}

	}

	private void updatePermission1() {
		if (commandParams[2].equals("-1")) {
			settings.rolePermission1 = 0;
			sendMessage(BotUtility.codifyText("Permission0 role removed from configuration"));
		} else {
			Long roleID;
			try {
				roleID = Long.parseLong(commandParams[2]);
			} catch (ParseException e) {
				sendFormatErrorMessage();
				return;
			}

			if (GuildManager.getRole(settings.guildID, roleID) != null) {
				settings.rolePermission1 = roleID;
			} else {
				printRoleErrorMessage();
				return;
			}

			sendMessage(BotUtility.codifyText("rolePermission1 role has been set to "
					+ GuildManager.getRole(settings.guildID, settings.rolePermission1)));
		}

	}

	private void updatePermission2() {
		if (commandParams[2].equals("-1")) {
			settings.rolePermission2 = 0;
			sendMessage(BotUtility.codifyText("Permission0 role removed from configuration"));
		} else {
			Long roleID;
			try {
				roleID = Long.parseLong(commandParams[2]);
			} catch (ParseException e) {
				sendFormatErrorMessage();
				return;
			}

			if (GuildManager.getRole(settings.guildID, roleID) != null) {
				settings.rolePermission2 = roleID;
			} else {
				printRoleErrorMessage();
				return;
			}

			sendMessage(BotUtility.codifyText("rolePermission1 role has been set to "
					+ GuildManager.getRole(settings.guildID, settings.rolePermission2)));
		}

	}

	private void updateGuildSettings() {
		GuildSettingsWriter.writeSettings(settings);
		GuildManager.reloadGuildSettings(settings.guildID);
	}

	private void sendFormatErrorMessage() {
		sendMessage(BotUtility.quotifyText("Parameters incorrect.\nCorrect format: " + commandPrefix
				+ "setup <key> <value> AND ensure that the value is correct OR enter " + commandPrefix
				+ "setuphelp for more information"));
	}

	private void printTextChannelErrorMessage() {
		sendMessage(BotUtility.quotifyText(
				"ERROR: The specified TEXT CHANNEL does not exist. Ensure you copied a correct TEXT CHANNEL ID"));
	}

	private void printVoiceChannelErrorMessage() {
		sendMessage(BotUtility.quotifyText(
				"ERROR: The specified VOICE CHANNEL does not exist. Ensure you copied a correct VOICE CHANNEL ID"));
	}

	private void printRoleErrorMessage() {
		sendMessage(BotUtility
				.quotifyText("ERROR: The specified ROLE does not exist. Ensure you copied a correct ROLE ID"));
	}

	@Override
	public String commandDescription() {
		return "Use this command to set up your server configuration with the bot.\nUse !setuphelp for help and usage";
	}
}
