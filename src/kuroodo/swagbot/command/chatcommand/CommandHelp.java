package kuroodo.swagbot.command.chatcommand;

import kuroodo.swagbot.command.Command;
import kuroodo.swagbot.command.CommandRegistry;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandHelp extends ChatCommand {

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (commandParams.length == 1) {
			sendHelpOverview();
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
}
