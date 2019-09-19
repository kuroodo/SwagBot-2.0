package kuroodo.swagbot.listeners;

import kuroodo.swagbot.SwagBot;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import kuroodo.swagbot.json.GuildSettingsReader;
import kuroodo.swagbot.json.GuildSettingsWriter;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
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

	private void giveWelcomeRole(Guild guild, Member member) {
		System.out.println("enter");
		GuildSettings settings = GuildManager.getGuild(guild.getIdLong());
		if (settings.enableWelcomeRole) {
			System.out.println("true");
			Role role = GuildManager.getRole(settings.guildID, settings.welcomeRole);

			if (role != null) {
				System.out.println("not null");
				BotUtility.addRoleToMember(settings.guild, role, member);
			}
		}
	}
}
