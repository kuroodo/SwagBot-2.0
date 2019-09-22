package kuroodo.swagbot.listeners;

import kuroodo.swagbot.command.Command;
import kuroodo.swagbot.command.CommandKeys;
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

			String commandName = "";
			String[] commandParams = BotUtility.splitString(event.getMessage().getContentRaw());

			// If starts with command prefix
			if (commandParams[0].startsWith(guild.commandPrefix)) {
				commandName = BotUtility.removePrefix(guild.commandPrefix, commandParams[0]);
				// Else starts with bot mention
			} else if (commandParams[0].equals((BotUtility.getSelfUser().getAsMention()))) {
				// If entered parameters
				if (commandParams.length > 1) {
					commandName = commandParams[1];
					// Remove bot mention
					commandParams = BotUtility.removeElement(commandParams, 0);
					// Set to magicball command
					commandName = CommandKeys.COMMAND_MAGICBALL;
				} else {// If just a blank mention, do magicball
					commandName = CommandKeys.COMMAND_MAGICBALL;
				}
			} else {
				return;
			}

			Command command = CommandRegistry.getCommand(commandName);
			command.executeCommand(commandParams, event);

		} else if (event.isFromType(ChannelType.PRIVATE)) {
			// TODO: Private commands
		}
	}
}