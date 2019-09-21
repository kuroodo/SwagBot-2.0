package kuroodo.swagbot.command.chatcommand.help;

import kuroodo.swagbot.command.Command;
import kuroodo.swagbot.command.CommandRegistry;
import kuroodo.swagbot.command.chatcommand.ChatCommand;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandHelp extends ChatCommand {

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

		// If no parameters
		if (commandParams.length == 1) {
			sendHelpOverview();
			// If asking for help with specific command
		} else if (commandParams.length == 2) {
			sendCommandHelp();
		}
	}

	private void sendHelpOverview() {
		sendMessage(event.getAuthor().getAsMention() + " https://github.com/kuroodo/SwagBot-2.0/blob/master/help.txt");
	}

	private void sendCommandHelp() {
		Command command = CommandRegistry.getCommand(commandParams[1]);
		sendMessage(BotUtility.codifyText(command.commandDescription()));
	}

	@Override
	public String commandDescription() {
		return "Gives information on how to use certain commands.";
	}

	@Override
	public String commandFormat() {
		return "For general help, " + commandPrefix + "help , for help with a specific command: " + commandPrefix
				+ "help <commandname>";
	}
}
