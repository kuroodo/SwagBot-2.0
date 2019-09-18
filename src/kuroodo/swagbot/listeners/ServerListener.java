package kuroodo.swagbot.listeners;

import kuroodo.swagbot.SwagBot;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import kuroodo.swagbot.json.GuildSettingsReader;
import kuroodo.swagbot.json.GuildSettingsWriter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ServerListener extends ListenerAdapter {
	@Override
	public void onReconnect(ReconnectedEvent event) {
		super.onReconnect(event);
		SwagBot.setToDefaultActivity();
	}

	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		super.onGuildJoin(event);
		Guild guild = event.getGuild();
		long guildID = guild.getIdLong();
		GuildSettings settings = new GuildSettings(guild);

		if (GuildSettingsReader.settingsFileExists(guildID)) {
			settings = GuildSettingsReader.loadSettingsFile(guildID);
		} else {
			System.out.println("Generating new Guild file for guild " + guild.getName() + " ID: " + guildID);
			GuildSettingsWriter.createNewFile(settings);
		}
		GuildManager.addGuild(settings);
	}

	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		super.onGuildLeave(event);
		GuildManager.removeGuild(event.getGuild());
	}
}
