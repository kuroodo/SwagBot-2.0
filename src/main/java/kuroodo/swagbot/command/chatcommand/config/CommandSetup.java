package kuroodo.swagbot.command.chatcommand.config;

import com.eclipsesource.json.ParseException;

import kuroodo.swagbot.SwagBot;
import kuroodo.swagbot.command.CommandKeys;
import kuroodo.swagbot.command.chatcommand.ChatCommand;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import kuroodo.swagbot.json.GuildSettingsReader;
import kuroodo.swagbot.json.GuildSettingsWriter;
import kuroodo.swagbot.json.JSONKeys;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandSetup extends ChatCommand {
	GuildSettings settings;

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
		if (!GuildSettingsReader.settingsFileExists(event.getGuild().getIdLong())) {
			sendMessage(BotUtility.codifyText("Error finding this server's configuration file."));
			if (!SwagBot.verifyGuildTemplateFile()) {
				sendMessage(BotUtility
						.quotifyText("There is an issue managing server configuration. A fix is being worked on."));
			}
			return;
		}

		// 3 params should be <prefix>setup key value
		int expectedParamsLength = 3;

		// Does member have a permission0/1/2 role
		if (!memberHasPermissions(event.getMember())) {
			sendNoPermissionsMessage();
			return;
			// If not enough params
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
		case JSONKeys.SETTINGS_MUSIC_CHANNEL:
			updateMusicChannel();
			return;
		case JSONKeys.SETTINGS_SPARTANKICK:
			updateEnableSpartankick();
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
			} catch (NumberFormatException | ParseException e) {
				sendFormatErrorMessage();
				return;
			}
			String result = settings.enableWelcome
					? "enabled. Don't forget to set a welcome message if you haven't already"
					: "disabled";
			sendMessage(BotUtility.codifyText("Welcome message has been " + result));
		}
	}

	private void updateWelcomeChannel() {
		if (commandParams[2].equals("-1")) {
			settings.welcomeChannel = 0;
			sendMessage(BotUtility.codifyText("Welcome channel removed from configuration"));
		} else {
			long channelID;
			try {
				channelID = Long.parseLong(commandParams[2]);
			} catch (NumberFormatException | ParseException e) {
				sendFormatErrorMessage();
				return;
			}

			Guild guild = settings.guild;
			TextChannel channel = guild.getTextChannelById(channelID);
			if (channel != null) {
				settings.welcomeChannel = channelID;
			} else {
				printTextChannelErrorMessage();
				return;
			}

			sendMessage(BotUtility.codifyText("Welcome channel has been set to " + channel.getName()));
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
			} catch (NumberFormatException | ParseException e) {
				sendFormatErrorMessage();
				return;
			}
			String result = settings.enableWelcomeRole
					? "enabled. Don't forget to set a welcome role if you haven't already"
					: "disabled";
			sendMessage(BotUtility.codifyText("Welcome role has been " + result));
		}
	}

	private void updateWelcomeRole() {
		if (commandParams[2].equals("-1")) {
			settings.welcomeRole = 0;
			sendMessage(BotUtility.codifyText("Welcome role removed from configuration"));
		} else {
			long roleID;
			try {
				roleID = Long.parseLong(commandParams[2]);
			} catch (NumberFormatException | ParseException e) {
				sendFormatErrorMessage();
				return;
			}

			Guild guild = settings.guild;
			Role role = guild.getRoleById(roleID);
			if (role != null) {
				settings.welcomeRole = roleID;
			} else {
				printRoleErrorMessage();
				return;
			}

			sendMessage(BotUtility.codifyText("Welcome role has been set to " + role.getName()));
		}

	}

	private void updateLogChannel() {
		if (commandParams[2].equals("-1")) {
			settings.logChannel = 0;
			sendMessage(BotUtility.codifyText("Log channel removed from configuration"));
		} else {
			long channelID;
			try {
				channelID = Long.parseLong(commandParams[2]);
			} catch (NumberFormatException | ParseException e) {
				sendFormatErrorMessage();
				return;
			}

			Guild guild = settings.guild;
			TextChannel channel = guild.getTextChannelById(channelID);
			if (channel != null) {
				settings.logChannel = channelID;
			} else {
				printTextChannelErrorMessage();
				return;
			}

			sendMessage(BotUtility.codifyText("Log channel has been set to " + channel.getName()));
		}
	}

	private void updateMuteRole() {
		if (commandParams[2].equals("-1")) {
			settings.muteRole = 0;
			sendMessage(BotUtility.codifyText("Mute role removed from configuration"));
		} else {
			long roleID;
			try {
				roleID = Long.parseLong(commandParams[2]);
			} catch (NumberFormatException | ParseException e) {
				sendFormatErrorMessage();
				return;
			}
			Guild guild = settings.guild;
			Role role = guild.getRoleById(roleID);
			if (role != null) {
				settings.muteRole = roleID;
			} else {
				printRoleErrorMessage();
				return;
			}

			sendMessage(BotUtility.codifyText("Mute role has been set to " + role.getName()));
		}

	}

	private void updateMuteChannel() {
		if (commandParams[2].equals("-1")) {
			settings.muteChannel = 0;
			sendMessage(BotUtility.codifyText("Mute channel removed from configuration"));
		} else {
			long channelID;
			try {
				channelID = Long.parseLong(commandParams[2]);
			} catch (NumberFormatException | ParseException e) {
				sendFormatErrorMessage();
				return;
			}

			Guild guild = settings.guild;
			VoiceChannel channel = guild.getVoiceChannelById(channelID);
			if (channel != null) {
				settings.muteChannel = channelID;
			} else {
				printVoiceChannelErrorMessage();
				return;
			}

			sendMessage(BotUtility.codifyText("Mute channel has been set to " + channel.getName()));
		}
	}

	private void updateMusicChannel() {
		if (commandParams[2].equals("-1")) {
			settings.musicchannel = 0;
			sendMessage(BotUtility.codifyText("Music channel removed from configuration"));
		} else {
			long channelID;
			try {
				channelID = Long.parseLong(commandParams[2]);
			} catch (NumberFormatException | ParseException e) {
				sendFormatErrorMessage();
				return;
			}

			Guild guild = settings.guild;
			VoiceChannel channel = guild.getVoiceChannelById(channelID);
			if (channel != null) {
				settings.musicchannel = channelID;
			} else {
				printVoiceChannelErrorMessage();
				return;
			}

			sendMessage(BotUtility.codifyText("Music channel has been set to " + channel.getName()));
		}
	}

	private void updateEnableSpartankick() {
		if (commandParams[2].equals("-1")) {
			settings.spartankick = false;
			sendMessage(BotUtility.codifyText("Spartankick command has been disabled."));
		} else {

			try {
				settings.spartankick = Boolean.parseBoolean(commandParams[2]);
			} catch (NumberFormatException | ParseException e) {
				sendFormatErrorMessage();
				return;
			}

			String result = settings.spartankick ? "enabled" : "disabled";
			sendMessage(BotUtility.codifyText("Spartankick command has been " + result + "."));
		}
	}

	private void updatePermission0() {
		if (commandParams[2].equals("-1")) {
			settings.permission0 = 0;
			sendMessage(BotUtility.codifyText("Permission0 role removed from configuration"));
		} else {
			long roleID;
			try {
				roleID = Long.parseLong(commandParams[2]);
			} catch (NumberFormatException | ParseException e) {
				sendFormatErrorMessage();
				return;
			}

			Guild guild = settings.guild;
			Role role = guild.getRoleById(roleID);
			if (role != null) {
				settings.permission0 = roleID;
			} else {
				printRoleErrorMessage();
				return;
			}

			sendMessage(BotUtility.codifyText("Permission0 role has been set to " + role.getName()));
		}

	}

	private void updatePermission1() {
		if (commandParams[2].equals("-1")) {
			settings.permission1 = 0;
			sendMessage(BotUtility.codifyText("Permission1 role removed from configuration"));
		} else {
			long roleID;
			try {
				roleID = Long.parseLong(commandParams[2]);
			} catch (NumberFormatException | ParseException e) {
				sendFormatErrorMessage();
				return;
			}

			Guild guild = settings.guild;
			Role role = guild.getRoleById(roleID);
			if (role != null) {
				settings.permission1 = roleID;
			} else {
				printRoleErrorMessage();
				return;
			}

			sendMessage(BotUtility.codifyText("rolePermission1 role has been set to " + role.getName()));
		}

	}

	private void updatePermission2() {
		if (commandParams[2].equals("-1")) {
			settings.permission2 = 0;
			sendMessage(BotUtility.codifyText("Permission2 role removed from configuration"));
		} else {
			long roleID;
			try {
				roleID = Long.parseLong(commandParams[2]);
			} catch (NumberFormatException | ParseException e) {
				sendFormatErrorMessage();
				return;
			}

			Guild guild = settings.guild;
			Role role = guild.getRoleById(roleID);
			if (role != null) {
				settings.permission2 = roleID;
			} else {
				printRoleErrorMessage();
				return;
			}

			sendMessage(BotUtility.codifyText("rolePermission2 role has been set to " + role.getName()));
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
		return "Use this command to set up your server configuration with the bot.\nUse " + commandPrefix
				+ CommandKeys.COMMAND_SETUPHELP + " for more information, and for information about keys and values.";
	}

	@Override
	public String commandFormat() {
		return "Usage: " + commandPrefix + CommandKeys.COMMAND_SETUP + " <key> <value>";
	}

	@Override
	public String commandUsageExample() {
		return commandPrefix + CommandKeys.COMMAND_SETUP + " mutechannel 110614880465227776";
	}
}
