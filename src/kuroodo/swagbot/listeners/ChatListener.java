package kuroodo.swagbot.listeners;

import kuroodo.swagbot.command.Command;
import kuroodo.swagbot.command.CommandRegistry;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

// Listens for chat specific events like commands
public class ChatListener extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		super.onMessageReceived(event);

		HandleCommandRequest(event);
		// TODO: LogChat or any other functionality()
	}

	private void HandleCommandRequest(MessageReceivedEvent event) {
		String[] commandParams = BotUtility.splitString(event.getMessage().getContentRaw());

		// TODO: Check if not from a guild
		GuildSettings guild = GuildManager.getGuild(event.getGuild().getIdLong());
		if (commandParams[0].startsWith(guild.commandPrefix)) {
			String commandName = BotUtility.removePrefix(guild.commandPrefix, commandParams[0]);
			Command command = CommandRegistry.getCommand(commandName);
			command.executeCommand(commandParams, event);
		}
	}

	// Just a test. This functionality should stay here or go to server listener
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		super.onGuildMemberJoin(event);
		GuildSettings guild = GuildManager.getGuild(event.getGuild().getIdLong());
		if (guild.enableWelcome) {
			GuildManager.getTextChannel(guild.guildID, guild.welcomeChannel).sendMessage(guild.welcomeMessage).queue();
			if (guild.enableWelcomeRole) {
				event.getGuild()
						.addRoleToMember(event.getMember(), GuildManager.getRole(guild.guildID, guild.welcomeRole))
						.queue();
				;
			}
		}
	}

}
