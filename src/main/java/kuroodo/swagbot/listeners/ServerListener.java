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
package kuroodo.swagbot.listeners;

import java.awt.Color;
import java.util.function.Consumer;

import kuroodo.swagbot.SwagBot;
import kuroodo.swagbot.command.CommandKeys;
import kuroodo.swagbot.config.BotConfig;
import kuroodo.swagbot.guild.GuildLogSettings;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import kuroodo.swagbot.json.GuildLogSettingsReader;
import kuroodo.swagbot.json.GuildLogSettingsWriter;
import kuroodo.swagbot.json.GuildSettingsReader;
import kuroodo.swagbot.json.GuildSettingsWriter;
import kuroodo.swagbot.utils.BotUtility;
import kuroodo.swagbot.utils.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Guild.Ban;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.channel.category.CategoryCreateEvent;
import net.dv8tion.jda.api.events.channel.category.CategoryDeleteEvent;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdateNameEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateNSFWEvent;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateNameEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateNameEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdatePermissionsEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ServerListener extends ListenerAdapter {
	@Override
	public void onReconnect(ReconnectedEvent event) {
		super.onReconnect(event);
		SwagBot.setToDefaultActivity();
	}

	// Called when bot joins a new guild
	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		super.onGuildJoin(event);
		// Begin initializing a new guild
		Guild guild = event.getGuild();
		long guildID = guild.getIdLong();
		GuildSettings settings = new GuildSettings(guild);

		// If guild file exists
		if (GuildSettingsReader.settingsFileExists(guildID)) {
			settings = GuildSettingsReader.loadSettingsFile(guildID);
		} else {
			System.out.println("Generating new Guild file for guild " + guild.getName() + " ID: " + guildID);
			GuildSettingsWriter.createNewFile(settings);
		}

		// If guild log settings file exists
		if (GuildLogSettingsReader.settingsFileExists(guildID)) {
			settings.logSettings = GuildLogSettingsReader.loadSettingsFile(guildID);
		} else {
			settings.logSettings = new GuildLogSettings(guild);
			System.out.println("Generating new log settings file for guild " + guild.getName() + " ID: " + guildID);
			GuildLogSettingsWriter.createNewFile(settings.logSettings);
		}

		GuildManager.addGuild(settings);
		sendBotJoinMessage(guild);
	}

	// Called when bot leaves guild
	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		super.onGuildLeave(event);
		GuildManager.removeGuild(event.getGuild());
	}

	// Called when a user joins a guild
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		super.onGuildMemberJoin(event);
		Guild guild = event.getGuild();
		Member member = event.getMember();
		GuildSettings settings = GuildManager.getGuild(guild);

		if (settings.enableWelcome) {
			sendWelcomeMessage(guild, settings, member);
		}

		if (settings.enableWelcomeRole) {
			giveWelcomeRole(guild, settings, member);
		}

		// Logging
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("A USER has JOINED THE SERVER");
		eb.setColor(new Color(BotUtility.EMBED_CORE_COLOR));
		eb.setDescription(member.getAsMention());
		eb.addField("Tag", member.getUser().getAsTag(), true);
		eb.setImage(member.getUser().getAvatarUrl());
		eb.setFooter("User ID: " + member.getIdLong() + " | Time of event: " + BotUtility.getCurrentDate() + " EST");
		Logger.sendLogEmbed(settings, eb);
	}

	// Called when a user leaves a guild
	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
		super.onGuildMemberRemove(event);
		Guild guild = event.getGuild();
		Member member = event.getMember();
		GuildSettings settings = GuildManager.getGuild(guild);

		// Logging
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("A USER has LEFT THE SERVER");
		eb.setColor(new Color(BotUtility.EMBED_CORE_COLOR));
		eb.setDescription(member.getAsMention());
		eb.addField("Tag", member.getUser().getAsTag(), true);
		eb.setImage(member.getUser().getAvatarUrl());
		eb.setFooter("User ID: " + member.getIdLong() + " | Time of event: " + BotUtility.getCurrentDate() + " EST");
		Logger.sendLogEmbed(settings, eb);
	}

	@Override
	public void onGuildUnban(GuildUnbanEvent event) {
		super.onGuildUnban(event);
		Guild guild = event.getGuild();
		User user = event.getUser();
		GuildSettings settings = GuildManager.getGuild(guild);

		// Logging
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("A USER has been UNBANNED");
		eb.setColor(new Color(BotUtility.EMBED_ALERT_COLOR));
		eb.setDescription(user.getName());
		eb.addField("Tag", user.getAsTag(), true);
		eb.setImage(user.getAvatarUrl());
		eb.setFooter("User ID: " + user.getIdLong() + " | Time of event: " + BotUtility.getCurrentDate() + " EST");
		Logger.sendLogEmbed(settings, eb);
	}

	@Override
	public void onGuildBan(GuildBanEvent event) {
		super.onGuildBan(event);
		Guild guild = event.getGuild();
		User user = event.getUser();
		GuildSettings settings = GuildManager.getGuild(guild);

		// Logging
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("A USER has been BANNED");
		eb.setColor(new Color(BotUtility.EMBED_ALERT_COLOR));
		eb.setDescription(user.getName());
		eb.addField("Tag", user.getAsTag(), true);

		if (BotUtility.hasPermission(Permission.BAN_MEMBERS, BotUtility.getSelfMember(guild))) {
			guild.retrieveBan(user).queue(new Consumer<Ban>() {

				@Override
				public void accept(Ban t) {
					if (t.getReason() != null) {
						eb.setDescription("Banned for: " + t.getReason());
						eb.setImage(user.getAvatarUrl());
						eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");

						Logger.sendLogEmbed(settings, eb);
					}
				}
			});
		} else {
			eb.setImage(event.getUser().getAvatarUrl());
			eb.setFooter("User ID: " + event.getUser().getIdLong() + " | Time of event: " + BotUtility.getCurrentDate()
					+ " EST");

			Logger.sendLogEmbed(settings, eb);
		}
	}

	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
		super.onGuildVoiceMove(event);
		if (hasMuteChannel(event.getGuild())) {
			Guild guild = event.getGuild();
			GuildSettings settings = GuildManager.getGuild(guild);
			VoiceChannel muteChannel = guild.getVoiceChannelById(settings.muteChannel);

			Role muteRole = guild.getRoleById(settings.muteRole);
			Member member = event.getMember();

			// Joined/Moved to mute channel
			if (event.getChannelJoined() == muteChannel && GuildManager.canMemberBeMuted(guild, member)) {
				// Give mute role if permission
				if (BotUtility.hasPermission(Permission.MANAGE_ROLES, BotUtility.getSelfMember(guild))) {
					giveMuteRole(guild, GuildManager.getGuild(guild), member);
				}
				// If muted member was moved away
			} else if (muteRole != null && BotUtility.hasRole(muteRole, member)) {
				BotUtility.removeRoleFromMember(guild, muteRole, member);
			}

		}
	}

	@Override
	public void onTextChannelUpdateNSFW(TextChannelUpdateNSFWEvent event) {
		super.onTextChannelUpdateNSFW(event);
		Guild guild = event.getGuild();
		GuildSettings settings = GuildManager.getGuild(guild);

		// Logging
		EmbedBuilder eb = new EmbedBuilder();

		if (event.getNewValue()) {
			eb.setDescription("The following TEXT CHANNEL is now NSFW");
		} else {
			eb.setDescription("The following TEXT CHANNEL is NO LONGER NSFW");
		}
		eb.setColor(new Color(BotUtility.EMBDED_CHANNEL_COLOR));
		eb.addField("Channel:", "#" + event.getChannel().getName(), true);
		eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");

		Logger.sendLogEmbed(settings, eb);
	}

	@Override
	public void onTextChannelUpdateName(TextChannelUpdateNameEvent event) {
		super.onTextChannelUpdateName(event);
		Guild guild = event.getGuild();
		GuildSettings settings = GuildManager.getGuild(guild);

		// Logging
		EmbedBuilder eb = new EmbedBuilder();
		eb.setDescription("The following TEXT CHANNEL has been RENAMED:");
		eb.setColor(new Color(BotUtility.EMBDED_CHANNEL_COLOR));
		eb.addField("Old name", "#" + event.getOldName(), true);
		eb.addField("New name", "#" + event.getNewName(), true);
		eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");
		Logger.sendLogEmbed(settings, eb);
	}

	@Override
	public void onTextChannelDelete(TextChannelDeleteEvent event) {
		super.onTextChannelDelete(event);
		Guild guild = event.getGuild();
		GuildSettings settings = GuildManager.getGuild(guild);

		// Logging
		EmbedBuilder eb = new EmbedBuilder();
		eb.setDescription("The following TEXT CHANNEL has been DELETED:");
		eb.setColor(new Color(BotUtility.EMBDED_CHANNEL_COLOR));
		eb.addField("Channel:", "#" + event.getChannel().getName(), true);
		eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");
		Logger.sendLogEmbed(settings, eb);
	}

	@Override
	public void onTextChannelCreate(TextChannelCreateEvent event) {
		super.onTextChannelCreate(event);
		Guild guild = event.getGuild();
		GuildSettings settings = GuildManager.getGuild(guild);

		// Logging
		EmbedBuilder eb = new EmbedBuilder();
		eb.setDescription("The following TEXT CHANNEL has been CREATED:");
		eb.setColor(new Color(BotUtility.EMBDED_CHANNEL_COLOR));
		eb.addField("Channel:", "#" + event.getChannel().getName(), true);
		eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");
		Logger.sendLogEmbed(settings, eb);
	}

	@Override
	public void onMessageDelete(MessageDeleteEvent event) {
		super.onMessageDelete(event);

		Message message = GuildManager.getMessageFromCache(event.getMessageIdLong());
		if (message == null)
			return;

		Guild guild = event.getGuild();
		GuildSettings settings = GuildManager.getGuild(guild);

		if (!settings.logSettings.messageDeleteLogging)
			return;

		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor(message.getAuthor().getAsTag(), message.getAuthor().getAvatarUrl(),
				message.getAuthor().getAvatarUrl());
		eb.setTitle("Message deleted in #" + event.getChannel().getName());
		eb.setDescription(message.getContentDisplay());

		if (!message.getAttachments().isEmpty()) {
			eb.addField("Attachments", message.getAttachments().get(0).getFileName(), true);
		}

		eb.setFooter("Message ID: " + event.getMessageId() + " | " + "Time of event: " + BotUtility.getCurrentDate()
				+ " EST");
		eb.setColor(new Color(BotUtility.EMBDED_USER_MESSAGE_COLOR));
		Logger.sendLogEmbed(settings, eb);

		GuildManager.removeMessageFromCache(message);
	}

	@Override
	public void onVoiceChannelUpdateName(VoiceChannelUpdateNameEvent event) {
		super.onVoiceChannelUpdateName(event);
		Guild guild = event.getGuild();
		GuildSettings settings = GuildManager.getGuild(guild);

		// Logging
		EmbedBuilder eb = new EmbedBuilder();
		eb.setDescription("The following VOICE CHANNEL has been RENAMED:");
		eb.setColor(new Color(BotUtility.EMBDED_CHANNEL_COLOR));
		eb.addField("Old name", event.getOldName(), true);
		eb.addField("New name", event.getNewName(), true);
		eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");
		Logger.sendLogEmbed(settings, eb);
	}

	@Override
	public void onVoiceChannelDelete(VoiceChannelDeleteEvent event) {
		super.onVoiceChannelDelete(event);
		Guild guild = event.getGuild();
		GuildSettings settings = GuildManager.getGuild(guild);

		// Logging
		EmbedBuilder eb = new EmbedBuilder();
		eb.setDescription("The following VOICE CHANNEL has been DELETED:");
		eb.setColor(new Color(BotUtility.EMBDED_CHANNEL_COLOR));
		eb.addField("Channel:", event.getChannel().getName(), true);
		eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");
		Logger.sendLogEmbed(settings, eb);
	}

	@Override
	public void onVoiceChannelCreate(VoiceChannelCreateEvent event) {
		super.onVoiceChannelCreate(event);
		Guild guild = event.getGuild();
		GuildSettings settings = GuildManager.getGuild(guild);

		// Logging
		EmbedBuilder eb = new EmbedBuilder();
		eb.setDescription("The following VOICE CHANNEL has been CREATED:");
		eb.setColor(new Color(BotUtility.EMBDED_CHANNEL_COLOR));
		eb.addField("Channel:", event.getChannel().getName(), true);
		eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");
		Logger.sendLogEmbed(settings, eb);
	}

	@Override
	public void onCategoryUpdateName(CategoryUpdateNameEvent event) {
		super.onCategoryUpdateName(event);
		Guild guild = event.getGuild();
		GuildSettings settings = GuildManager.getGuild(guild);

		// Logging
		EmbedBuilder eb = new EmbedBuilder();
		eb.setDescription("The following CATEGORY has been RENAMED:");
		eb.setColor(new Color(BotUtility.EMBDED_CHANNEL_COLOR));
		eb.addField("Old name", event.getOldName(), true);
		eb.addField("New name", event.getNewName(), true);
		eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");
		Logger.sendLogEmbed(settings, eb);
	}

	@Override
	public void onCategoryDelete(CategoryDeleteEvent event) {
		super.onCategoryDelete(event);
		Guild guild = event.getGuild();
		GuildSettings settings = GuildManager.getGuild(guild);

		// Logging
		EmbedBuilder eb = new EmbedBuilder();
		eb.setDescription("The following CATEGORY has been DELETED:");
		eb.setColor(new Color(BotUtility.EMBDED_CHANNEL_COLOR));
		eb.addField("Category:", event.getCategory().getName(), true);
		eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");
		Logger.sendLogEmbed(settings, eb);
	}

	@Override
	public void onCategoryCreate(CategoryCreateEvent event) {
		super.onCategoryCreate(event);
		Guild guild = event.getGuild();
		GuildSettings settings = GuildManager.getGuild(guild);

		// Logging
		EmbedBuilder eb = new EmbedBuilder();
		eb.setDescription("The following CATEGORY has been CREATED:");
		eb.setColor(new Color(BotUtility.EMBDED_CHANNEL_COLOR));
		eb.addField("Category:", event.getCategory().getName(), true);
		eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");
		Logger.sendLogEmbed(settings, eb);
	}

	@Override
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
		super.onGuildMemberRoleAdd(event);
		Guild guild = event.getGuild();
		GuildSettings settings = GuildManager.getGuild(guild);
		if (!settings.logSettings.memberRoleLogging)
			return;

		// Logging
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(new Color(BotUtility.EMBED_ROLE_COLOR));
		eb.setDescription(event.getMember().getAsMention() + " has been GIVEN the following ROLES");

		for (Role role : event.getRoles()) {
			eb.addField("", role.getName(), false);
		}

		eb.setFooter("User ID: " + event.getMember().getIdLong() + " | Time of event: " + BotUtility.getCurrentDate()
				+ " EST");
		Logger.sendLogEmbed(settings, eb);
	}

	@Override
	public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
		super.onGuildMemberRoleRemove(event);

		Guild guild = event.getGuild();
		GuildSettings settings = GuildManager.getGuild(guild);
		if (!settings.logSettings.memberRoleLogging)
			return;

		// Logging
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(new Color(BotUtility.EMBED_ROLE_COLOR));
		eb.setDescription(event.getMember().getAsMention() + " has had the following ROLES REMOVED");

		for (Role role : event.getRoles()) {
			eb.addField("", role.getName(), false);
		}

		eb.setFooter("User ID: " + event.getMember().getIdLong() + " | Time of event: " + BotUtility.getCurrentDate()
				+ " EST");
		Logger.sendLogEmbed(settings, eb);
	}

	@Override
	public void onRoleCreate(RoleCreateEvent event) {
		super.onRoleCreate(event);
		Guild guild = event.getGuild();
		GuildSettings settings = GuildManager.getGuild(guild);

		// Logging
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(new Color(BotUtility.EMBED_ROLE_COLOR));
		eb.setDescription("The following ROLE has been CREATED:");
		eb.addField("Role:", event.getRole().getName(), true);
		eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");
		Logger.sendLogEmbed(settings, eb);
		// C9932E
	}

	@Override
	public void onRoleDelete(RoleDeleteEvent event) {
		super.onRoleDelete(event);
		Guild guild = event.getGuild();
		GuildSettings settings = GuildManager.getGuild(guild);

		// Logging
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(new Color(BotUtility.EMBED_ROLE_COLOR));
		eb.setDescription("The following ROLE has been DELETED:");
		eb.addField("Role:", event.getRole().getName(), true);
		eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");
		Logger.sendLogEmbed(settings, eb);
	}

	@Override
	public void onRoleUpdateName(RoleUpdateNameEvent event) {
		super.onRoleUpdateName(event);
		Guild guild = event.getGuild();
		GuildSettings settings = GuildManager.getGuild(guild);
		if (!settings.logSettings.roleEditLogging)
			return;

		// Logging
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(new Color(BotUtility.EMBED_ROLE_COLOR));
		eb.setDescription("The following ROLE has been RENAMED");
		eb.addField("Old name", event.getOldName(), true);
		eb.addField("New name", event.getNewName(), true);
		eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");
		Logger.sendLogEmbed(settings, eb);
	}

	@Override
	public void onRoleUpdatePermissions(RoleUpdatePermissionsEvent event) {
		super.onRoleUpdatePermissions(event);
		Guild guild = event.getGuild();
		GuildSettings settings = GuildManager.getGuild(guild);

		// Logging
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(new Color(BotUtility.EMBED_ROLE_COLOR));
		eb.setDescription("The following ROLE has NEW PERMISSIONS:");
		eb.addField("", "These are the new permissions for: " + event.getRole().getName(), false);
		for (Permission perm : event.getNewPermissions()) {
			eb.addField(perm.getName(), "", false);
		}
		eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");
		Logger.sendLogEmbed(settings, eb);
	}

	@Override
	public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
		super.onGuildMemberUpdateNickname(event);
		Guild guild = event.getGuild();
		GuildSettings settings = GuildManager.getGuild(guild);
		if (!settings.logSettings.nicknameLogging) {
			return;
		}

		// Logging
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(new Color(BotUtility.EMBDED_CHANNEL_COLOR));
		eb.setDescription("The following USER has changed NICKNAMES");
		eb.addField("Old nickname", event.getOldNickname(), true);
		eb.addField("New nickname", event.getNewNickname(), true);
		eb.setFooter("User ID: " + event.getMember().getIdLong() + " | Time of event: " + BotUtility.getCurrentDate()
				+ " EST");
		Logger.sendLogEmbed(settings, eb);
	}

	private void sendWelcomeMessage(Guild guild, GuildSettings settings, Member member) {
		TextChannel channel = guild.getTextChannelById(settings.welcomeChannel);
		if (channel != null) {
			BotUtility.sendGuildMessage(settings.guild, channel, member.getAsMention() + " " + settings.welcomeMessage);
		}
	}

	private boolean hasMuteChannel(Guild guild) {
		return guild.getVoiceChannelById(GuildManager.getGuild(guild).muteChannel) != null;
	}

	private void giveWelcomeRole(Guild guild, GuildSettings settings, Member member) {
		Role role = guild.getRoleById(settings.welcomeRole);

		if (role != null) {
			BotUtility.addRoleToMember(settings.guild, role, member);
		}
	}

	private void giveMuteRole(Guild guild, GuildSettings settings, Member member) {
		Role role = guild.getRoleById(settings.muteRole);
		if (role != null) {
			BotUtility.addRoleToMember(settings.guild, role, member);
		}
	}

	private void sendBotJoinMessage(Guild guild) {
		EmbedBuilder eb = new EmbedBuilder();
		User self = BotUtility.getSelfUser();

		eb.setTitle("Introductions");
		eb.setColor(new Color(BotUtility.EMBED_CORE_COLOR));
		eb.setDescription("Hello, I am [" + BotConfig.BOTNAME
				+ "](https://github.com/kuroodo/SwagBot-2.0). I was just added to your server: " + guild.getName());

		eb.addField("Setup", "To begin setting up the bot, use the " + CommandKeys.COMMAND_SETUPHELP + " and "
				+ CommandKeys.COMMAND_SETUP
				+ " commands.\nThe default command prefix should be !, therefore use !setuphelp.\nIn an emergency, mention the bot with help or setuphelp, i.e "
				+ self.getAsMention() + " help or " + self.getAsMention() + " setuphelp", true);

		eb.addField("Manual",
				"Be sure to [read the manual for more info](https://github.com/kuroodo/SwagBot-2.0/blob/master/help.txt)",
				true);
		eb.setFooter("Bot join date: " + BotUtility.getCurrentDate());

		guild.getOwner().getUser().openPrivateChannel().queue(new Consumer<PrivateChannel>() {

			@Override
			public void accept(PrivateChannel t) {
				t.sendMessage(eb.build()).queue();
			}

		});
	}
}