package kuroodo.swagbot.command.chatcommand;

import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandSetupHelp extends ChatCommand {

	@Override
	protected void setCommandPermissiosn() {
		requiredPermissions.add(Permission.MESSAGE_WRITE);
		requiredPermissions.add(Permission.MESSAGE_EMBED_LINKS);
	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);
		// If asking for the setup keys, <prefix>setuphelp keys
		if (commandParams.length > 1 && commandParams[1].toLowerCase().equals("keys")) {
			sendKeyDescriptions();
		} else {
			sendCurrentSettings();
		}
	}

	private void sendKeyDescriptions() {
		GuildSettings settings = GuildManager.getGuild(event.getGuild());
		sendEmbed(getDescriptionsEmbed(settings));
	}

	private void sendCurrentSettings() {
		GuildSettings settings = GuildManager.getGuild(event.getGuild());
		sendEmbed(getCurrentSettingsEmbed(settings));
	}

	public EmbedBuilder getDescriptionsEmbed(GuildSettings settings) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("List of Keys and Values");
		eb.setDescription("Set keys to a vlue of -1 to reset/disable them");
		eb = getKeyDescriptions(settings, eb);

		return eb;
	}

	public EmbedBuilder getCurrentSettingsEmbed(GuildSettings settings) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Current Build Settings");
		eb = getCurrentSettings(settings, eb);
		eb.setDescription(
				"For information, see the [manual](https://github.com/kuroodo/SwagBot-2.0/blob/master/help.txt)");
		eb.setFooter("To edit these values, use " + commandPrefix + "setup <key> <value>\n"
				+ "To get a description of each key and their values, enter: " + commandPrefix + "setuphelp keys");
		return eb;
	}

	public EmbedBuilder getKeyDescriptions(GuildSettings settings, EmbedBuilder eb) {
		eb.addField("commandprefix",
				"The prefix that will be used to activate a command. Accepted values: Text with no spaces ", true);
		eb.addField("enablewelcome",
				"Enable the bot to send a welcome message when someone joins the server. Accepted values: true or false",
				true);
		eb.addField("welcomechannel",
				"The channel the bot posts welcome messages to when a user joins the server. Accepted values: A TEXT CHANNEL ID",
				true);
		eb.addField("welcomemessage", "The welcome message sent to new members. Accepted values: Any text", true);
		eb.addField("enablewelcomerole",
				"Enable the bot to give a role when someoene joins the server. Accepted values: true or false", true);
		eb.addField("welcomerole", "The role the bot gives to new joining members. Accepted values: A ROLE ID", true);
		eb.addField("logchannel", "The channel for the bot to post logs. Accepted values: A TEXT CHANNEL ID", true);
		eb.addField("muterole", "Role given to members who are to be muted. Accepted values: A ROLE ID", true);
		eb.addField("muterole",
				"A voice channel where it instantly gives members the muterole if manually placed in. Accepted values: A VOICE CHANNEL ID",
				true);
		eb.addField("rolepermission0",
				"Define what role has Admin control/Access to all commands. Roles with Admin permissions inherit this ability by default. Therefore use this command if a role does not have Admin permissions. "
						+ "Accepted values: A ROLE ID",
				true);
		eb.addField("rolepermission1",
				"Define what role has Moderator-level of control. Access to Ban, clearchat, and silencing commands. Commands do not affect admins. Accepted values: A ROLE ID",
				true);
		eb.addField("rolepermission2",
				"Extra permissions role that has access to silencing commands. Accepted values: A ROLE ID", true);
		return eb;
	}

	public EmbedBuilder getCurrentSettings(GuildSettings settings, EmbedBuilder eb) {
		TextChannel channel;
		VoiceChannel vChannel;
		Role role;

		// Prefix
		eb.addField("Command Prefix", commandPrefix, true);

		// Enable Welcome Message
		eb.addField("Enable Welcome Message", "" + settings.enableWelcome, true);

		// Welcome Channel
		channel = GuildManager.getTextChannel(settings.guildID, settings.welcomeChannel);
		if (channel != null) {
			eb.addField("Welcome Channel", "" + channel.getName(), true);
		} else {
			eb.addField("Welcome Channel", "" + "None", true);
		}

		// Welcome Message
		eb.addField("Welcome Message", settings.welcomeMessage, true);
		// Enable Welcome Role
		eb.addField("Enable Welcome Role", "" + settings.enableWelcomeRole, true);

		// Welcome Role
		role = GuildManager.getRole(settings.guildID, settings.welcomeRole);
		if (role != null) {
			eb.addField("Welcome Role", role.getName(), true);
		} else {
			eb.addField("Welcome Role", "None", true);
		}

		// Log Channel
		channel = GuildManager.getTextChannel(settings.guildID, settings.logChannel);
		if (channel != null) {
			eb.addField("Log Channel", channel.getName(), true);
		} else {
			eb.addField("Log Channel", "None", true);
		}

		// Mute Role
		role = GuildManager.getRole(settings.guildID, settings.muteRole);
		if (role != null) {
			eb.addField("Mute Role: ", role.getName(), true);
		} else {
			eb.addField("Mute Role", "None", true);
		}

		// Mute Channel
		vChannel = GuildManager.getVoiceChannel(settings.guildID, settings.muteChannel);
		if (vChannel != null) {
			eb.addField("Mute Channel", vChannel.getName(), true);
		} else {
			eb.addField("Mute Channel", "None", true);
		}

		// Permission 0
		role = GuildManager.getRole(settings.guildID, settings.rolePermission0);
		if (role != null) {
			eb.addField("Permission0 Role", role.getName(), true);
		} else {
			eb.addField("Permission0 Role", "None", true);
		}

		// Permission 1
		role = GuildManager.getRole(settings.guildID, settings.rolePermission1);
		if (role != null) {
			eb.addField("Permission1 Role", role.getName(), true);
		} else {
			eb.addField("Permission1 Role", "None", true);
		}

		// Permission 2
		role = GuildManager.getRole(settings.guildID, settings.rolePermission2);
		if (role != null) {
			eb.addField("Permission2 Role", role.getName(), true);
		} else {
			eb.addField("Permission2 Role", "None", true);
		}

		return eb;
	}

	@Override
	public String commandDescription() {
		return "Insight on how to set up and configure the bot";
	}
}