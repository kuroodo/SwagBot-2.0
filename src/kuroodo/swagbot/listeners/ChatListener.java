package kuroodo.swagbot.listeners;

import kuroodo.swagbot.command.Command;
import kuroodo.swagbot.command.CommandRegistry;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

// Listens for chat specific events like commands
public class ChatListener extends ListenerAdapter {

	@Override
	public void onGenericGuild(GenericGuildEvent event) {
		super.onGenericGuild(event);
		GuildManager.verifyGuildIntegrity(event.getGuild().getIdLong());
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		super.onMessageReceived(event);
		if (event.getAuthor().isFake() || event.getAuthor().isBot()) {
			return;
		}
		HandleCommandRequest(event);
		// TODO: LogChat or any other functionality()
	}

	private void HandleCommandRequest(MessageReceivedEvent event) {
		if (event.isFromGuild()) {
			GuildSettings guild = GuildManager.getGuild(event.getGuild().getIdLong());
			if (event.getMessage().getContentRaw().startsWith(guild.commandPrefix)) {
				String[] commandParams = BotUtility.splitString(event.getMessage().getContentRaw());
				String commandName = BotUtility.removePrefix(guild.commandPrefix, commandParams[0]);
				Command command = CommandRegistry.getCommand(commandName);
				command.executeCommand(commandParams, event);
			}
		} else if (event.isFromType(ChannelType.PRIVATE)) {
			// TODO: Private commands
		}
	}
}