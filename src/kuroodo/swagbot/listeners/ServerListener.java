package kuroodo.swagbot.listeners;

import java.util.function.Consumer;

import kuroodo.swagbot.SwagBot;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import kuroodo.swagbot.json.GuildSettingsReader;
import kuroodo.swagbot.json.GuildSettingsWriter;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Guild.Ban;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
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
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
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

		GuildManager.addGuild(settings);
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

		sendWelcomeMessage(guild, member);
		giveWelcomeRole(guild, member);

		if (hasLogChannel(event.getGuild())) {
			long guildID = event.getGuild().getIdLong();
			long logchannel = GuildManager.getGuild(guildID).logChannel;
			TextChannel logChannel = GuildManager.getTextChannel(guildID, logchannel);
			EmbedBuilder eb = new EmbedBuilder();

			eb.setTitle("A USER has JOINED THE SERVER");
			eb.setDescription(event.getMember().getAsMention());
			eb.addField("Tag", event.getUser().getAsTag(), true);
			eb.setImage(event.getUser().getAvatarUrl());
			eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");
			BotUtility.sendEmbed(event.getGuild(), logChannel, eb);
		}
	}

	// Called when a user leaves a guild
	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		super.onGuildMemberLeave(event);
		if (hasLogChannel(event.getGuild())) {
			long guildID = event.getGuild().getIdLong();
			long logchannel = GuildManager.getGuild(guildID).logChannel;
			TextChannel logChannel = GuildManager.getTextChannel(guildID, logchannel);
			EmbedBuilder eb = new EmbedBuilder();

			eb.setTitle("A USER has LEFT THE SERVER");
			eb.setDescription(event.getMember().getAsMention());
			eb.addField("Tag", event.getUser().getAsTag(), true);
			eb.setImage(event.getUser().getAvatarUrl());
			eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");
			BotUtility.sendEmbed(event.getGuild(), logChannel, eb);
		}
	}

	@Override
	public void onGuildUnban(GuildUnbanEvent event) {
		super.onGuildUnban(event);
		if (hasLogChannel(event.getGuild())) {
			long guildID = event.getGuild().getIdLong();
			long logchannel = GuildManager.getGuild(guildID).logChannel;
			TextChannel logChannel = GuildManager.getTextChannel(guildID, logchannel);
			EmbedBuilder eb = new EmbedBuilder();

			eb.setTitle("A USER has been UNBANNED");
			eb.setDescription(event.getUser().getName());
			eb.addField("Tag", event.getUser().getAsTag(), true);
			eb.setImage(event.getUser().getAvatarUrl());
			eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");

			BotUtility.sendEmbed(event.getGuild(), logChannel, eb);
		}
	}

	@Override
	public void onGuildBan(GuildBanEvent event) {
		super.onGuildBan(event);
		if (hasLogChannel(event.getGuild())) {
			long guildID = event.getGuild().getIdLong();
			long logchannel = GuildManager.getGuild(guildID).logChannel;
			TextChannel logChannel = GuildManager.getTextChannel(guildID, logchannel);
			EmbedBuilder eb = new EmbedBuilder();

			eb.setTitle("A USER has been BANNED");
			eb.setDescription(event.getUser().getName());
			eb.addField("Tag", event.getUser().getAsTag(), true);

			if (BotUtility.hasPermission(Permission.BAN_MEMBERS, BotUtility.getSelfMember(event.getGuild()))) {
				event.getGuild().retrieveBan(event.getUser()).queue(new Consumer<Ban>() {

					@Override
					public void accept(Ban t) {
						if (t.getReason() != null) {
							eb.setDescription("Banned for: " + t.getReason());
							eb.setImage(event.getUser().getAvatarUrl());
							eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");

							BotUtility.sendEmbed(event.getGuild(), logChannel, eb);
						}
					}
				});
			} else {
				eb.setImage(event.getUser().getAvatarUrl());
				eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");

				BotUtility.sendEmbed(event.getGuild(), logChannel, eb);
			}
		}
	}

	@Override
	public void onTextChannelUpdateNSFW(TextChannelUpdateNSFWEvent event) {
		super.onTextChannelUpdateNSFW(event);
		if (hasLogChannel(event.getGuild())) {
			long guildID = event.getGuild().getIdLong();
			long logchannel = GuildManager.getGuild(guildID).logChannel;
			TextChannel logChannel = GuildManager.getTextChannel(guildID, logchannel);
			EmbedBuilder eb = new EmbedBuilder();

			if (event.getNewValue()) {
				eb.setTitle("The following TEXT CHANNEL is now NSFW");
			} else {
				eb.setTitle("The following TEXT CHANNEL is NO LONGER NSFW");
			}
			eb.addField("#" + event.getChannel().getName(), "", true);
			eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");

			BotUtility.sendEmbed(event.getGuild(), logChannel, eb);
		}
	}

	@Override
	public void onTextChannelUpdateName(TextChannelUpdateNameEvent event) {
		super.onTextChannelUpdateName(event);
		if (hasLogChannel(event.getGuild())) {
			long guildID = event.getGuild().getIdLong();
			long logchannel = GuildManager.getGuild(guildID).logChannel;
			TextChannel logChannel = GuildManager.getTextChannel(guildID, logchannel);
			EmbedBuilder eb = new EmbedBuilder();

			eb.setTitle("The following TEXT CHANNEL has been RENAMED:");
			eb.addField("Old name", "#" + event.getOldName(), true);
			eb.addField("New name", "#" + event.getNewName(), true);
			eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");

			BotUtility.sendEmbed(event.getGuild(), logChannel, eb);
		}
	}

	@Override
	public void onTextChannelDelete(TextChannelDeleteEvent event) {
		super.onTextChannelDelete(event);
		if (hasLogChannel(event.getGuild())) {
			long guildID = event.getGuild().getIdLong();
			long logchannel = GuildManager.getGuild(guildID).logChannel;
			TextChannel logChannel = GuildManager.getTextChannel(guildID, logchannel);
			EmbedBuilder eb = new EmbedBuilder();

			eb.setTitle("The following TEXT CHANNEL has been DELETED:");
			eb.addField("#" + event.getChannel().getName(), "", true);
			eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");

			BotUtility.sendEmbed(event.getGuild(), logChannel, eb);
		}
	}

	@Override
	public void onTextChannelCreate(TextChannelCreateEvent event) {
		super.onTextChannelCreate(event);
		if (hasLogChannel(event.getGuild())) {
			long guildID = event.getGuild().getIdLong();
			long logchannel = GuildManager.getGuild(guildID).logChannel;
			TextChannel logChannel = GuildManager.getTextChannel(guildID, logchannel);
			EmbedBuilder eb = new EmbedBuilder();

			eb.setTitle("The following TEXT CHANNEL has been CREATED:");
			eb.addField("#" + event.getChannel().getName(), "", true);
			eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");

			BotUtility.sendEmbed(event.getGuild(), logChannel, eb);
		}
	}

	@Override
	public void onVoiceChannelUpdateName(VoiceChannelUpdateNameEvent event) {
		super.onVoiceChannelUpdateName(event);
		if (hasLogChannel(event.getGuild())) {
			long guildID = event.getGuild().getIdLong();
			long logchannel = GuildManager.getGuild(guildID).logChannel;
			TextChannel logChannel = GuildManager.getTextChannel(guildID, logchannel);
			EmbedBuilder eb = new EmbedBuilder();

			eb.setTitle("The following VOICE CHANNEL has been RENAMED:");
			eb.addField("Old name", event.getOldName(), true);
			eb.addField("New name", event.getNewName(), true);
			eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");

			BotUtility.sendEmbed(event.getGuild(), logChannel, eb);
		}
	}

	@Override
	public void onVoiceChannelDelete(VoiceChannelDeleteEvent event) {
		super.onVoiceChannelDelete(event);
		if (hasLogChannel(event.getGuild())) {
			long guildID = event.getGuild().getIdLong();
			long logchannel = GuildManager.getGuild(guildID).logChannel;
			TextChannel logChannel = GuildManager.getTextChannel(guildID, logchannel);
			EmbedBuilder eb = new EmbedBuilder();

			eb.setTitle("The following VOICE CHANNEL has been DELETED:");
			eb.addField(event.getChannel().getName(), "", true);
			eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");

			BotUtility.sendEmbed(event.getGuild(), logChannel, eb);
		}
	}

	@Override
	public void onVoiceChannelCreate(VoiceChannelCreateEvent event) {
		super.onVoiceChannelCreate(event);
		if (hasLogChannel(event.getGuild())) {
			long guildID = event.getGuild().getIdLong();
			long logchannel = GuildManager.getGuild(guildID).logChannel;
			TextChannel logChannel = GuildManager.getTextChannel(guildID, logchannel);
			EmbedBuilder eb = new EmbedBuilder();

			eb.setTitle("The following VOICE CHANNEL has been CREATED:");
			eb.addField(event.getChannel().getName(), "", true);
			eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");

			BotUtility.sendEmbed(event.getGuild(), logChannel, eb);
		}
	}

	@Override
	public void onCategoryUpdateName(CategoryUpdateNameEvent event) {
		super.onCategoryUpdateName(event);
		if (hasLogChannel(event.getGuild())) {
			long guildID = event.getGuild().getIdLong();
			long logchannel = GuildManager.getGuild(guildID).logChannel;
			TextChannel logChannel = GuildManager.getTextChannel(guildID, logchannel);
			EmbedBuilder eb = new EmbedBuilder();

			eb.setTitle("The following CATEGORY has been RENAMED:");
			eb.addField("Old name", event.getOldName(), true);
			eb.addField("New name", event.getNewName(), true);
			eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");

			BotUtility.sendEmbed(event.getGuild(), logChannel, eb);
		}
	}

	@Override
	public void onCategoryDelete(CategoryDeleteEvent event) {
		super.onCategoryDelete(event);
		if (hasLogChannel(event.getGuild())) {
			long guildID = event.getGuild().getIdLong();
			long logchannel = GuildManager.getGuild(guildID).logChannel;
			TextChannel logChannel = GuildManager.getTextChannel(guildID, logchannel);
			EmbedBuilder eb = new EmbedBuilder();

			eb.setTitle("The following CATEGORY has been DELETED:");
			eb.addField(event.getCategory().getName(), "", true);
			eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");

			BotUtility.sendEmbed(event.getGuild(), logChannel, eb);
		}
	}

	@Override
	public void onCategoryCreate(CategoryCreateEvent event) {
		super.onCategoryCreate(event);
		if (hasLogChannel(event.getGuild())) {
			long guildID = event.getGuild().getIdLong();
			long logchannel = GuildManager.getGuild(guildID).logChannel;
			TextChannel logChannel = GuildManager.getTextChannel(guildID, logchannel);
			EmbedBuilder eb = new EmbedBuilder();

			eb.setTitle("The following CATEGORY has been CREATED:");
			eb.addField(event.getCategory().getName(), "", true);
			eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");

			BotUtility.sendEmbed(event.getGuild(), logChannel, eb);
		}
	}

	@Override
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
		super.onGuildMemberRoleAdd(event);
		if (hasLogChannel(event.getGuild())) {
			long guildID = event.getGuild().getIdLong();
			long logchannel = GuildManager.getGuild(guildID).logChannel;
			TextChannel logChannel = GuildManager.getTextChannel(guildID, logchannel);
			EmbedBuilder eb = new EmbedBuilder();

			eb.setTitle("A user has been GIVEN the following ROLES");
			eb.setDescription(event.getMember().getAsMention());

			for (Role role : event.getRoles()) {
				eb.addField(role.getName(), "", true);
			}

			eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");

			BotUtility.sendEmbed(event.getGuild(), logChannel, eb);
		}
	}

	@Override
	public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
		super.onGuildMemberRoleRemove(event);

		if (hasLogChannel(event.getGuild())) {
			long guildID = event.getGuild().getIdLong();
			long logchannel = GuildManager.getGuild(guildID).logChannel;
			TextChannel logChannel = GuildManager.getTextChannel(guildID, logchannel);
			EmbedBuilder eb = new EmbedBuilder();

			eb.setTitle("A user has had the following ROLES REMOVED");
			eb.setDescription(event.getMember().getAsMention());

			for (Role role : event.getRoles()) {
				eb.addField(role.getName(), "", true);
			}

			eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");

			BotUtility.sendEmbed(event.getGuild(), logChannel, eb);
		}
	}

	@Override
	public void onRoleCreate(RoleCreateEvent event) {
		super.onRoleCreate(event);
		if (hasLogChannel(event.getGuild())) {
			long guildID = event.getGuild().getIdLong();
			long logchannel = GuildManager.getGuild(guildID).logChannel;
			TextChannel logChannel = GuildManager.getTextChannel(guildID, logchannel);
			EmbedBuilder eb = new EmbedBuilder();

			eb.setTitle("The following CROLE has been CREATED:");
			eb.addField(event.getRole().getName(), "", true);
			eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");

			BotUtility.sendEmbed(event.getGuild(), logChannel, eb);
		}
	}

	@Override
	public void onRoleDelete(RoleDeleteEvent event) {
		super.onRoleDelete(event);
		if (hasLogChannel(event.getGuild())) {
			long guildID = event.getGuild().getIdLong();
			long logchannel = GuildManager.getGuild(guildID).logChannel;
			TextChannel logChannel = GuildManager.getTextChannel(guildID, logchannel);
			EmbedBuilder eb = new EmbedBuilder();

			eb.setTitle("The following ROLE has been DELETED:");
			eb.addField(event.getRole().getName(), "", true);
			eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");

			BotUtility.sendEmbed(event.getGuild(), logChannel, eb);
		}
	}

	@Override
	public void onRoleUpdateName(RoleUpdateNameEvent event) {
		super.onRoleUpdateName(event);
		if (hasLogChannel(event.getGuild())) {
			long guildID = event.getGuild().getIdLong();
			long logchannel = GuildManager.getGuild(guildID).logChannel;
			TextChannel logChannel = GuildManager.getTextChannel(guildID, logchannel);
			EmbedBuilder eb = new EmbedBuilder();

			eb.setTitle("The following ROLE has been RENAMED");
			eb.addField("Old name", event.getOldName(), true);
			eb.addField("New name", event.getNewName(), true);
			eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");

			BotUtility.sendEmbed(event.getGuild(), logChannel, eb);
		}
	}

	@Override
	public void onRoleUpdatePermissions(RoleUpdatePermissionsEvent event) {
		super.onRoleUpdatePermissions(event);
		if (hasLogChannel(event.getGuild())) {
			long guildID = event.getGuild().getIdLong();
			long logchannel = GuildManager.getGuild(guildID).logChannel;
			TextChannel logChannel = GuildManager.getTextChannel(guildID, logchannel);
			EmbedBuilder eb = new EmbedBuilder();

			eb.setTitle("The following ROLE has NEW PERMISSIONS:");
			eb.setDescription("These are the new permissions for " + event.getRole().getName());
			for (Permission perm : event.getNewPermissions()) {
				eb.addField(perm.getName(), "", false);
			}
			eb.setFooter("Time of event: " + BotUtility.getCurrentDate() + " EST");

			BotUtility.sendEmbed(event.getGuild(), logChannel, eb);
		}
	}

	private void sendWelcomeMessage(Guild guild, Member member) {
		GuildSettings settings = GuildManager.getGuild(guild.getIdLong());
		if (settings.enableWelcome) {
			TextChannel channel = GuildManager.getTextChannel(settings.guildID, settings.welcomeChannel);
			if (channel != null) {
				BotUtility.sendGuildMessage(settings.guild, channel,
						member.getAsMention() + " " + settings.welcomeMessage);
			}
		}
	}

	private boolean hasLogChannel(Guild guild) {
		return GuildManager.getTextChannel(guild.getIdLong(), GuildManager.getGuild(guild).logChannel) != null;
	}

	private void giveWelcomeRole(Guild guild, Member member) {
		GuildSettings settings = GuildManager.getGuild(guild.getIdLong());
		if (settings.enableWelcomeRole) {
			Role role = GuildManager.getRole(settings.guildID, settings.welcomeRole);

			if (role != null) {
				BotUtility.addRoleToMember(settings.guild, role, member);
			}
		}
	}
}
