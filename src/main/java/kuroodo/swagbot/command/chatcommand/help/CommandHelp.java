package kuroodo.swagbot.command.chatcommand.help;

import kuroodo.swagbot.command.Command;
import kuroodo.swagbot.command.CommandRegistry;
import kuroodo.swagbot.command.chatcommand.ChatCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandHelp extends ChatCommand {

	@Override
	protected void setCommandPermissiosn() {
		requiredPermissions.add(Permission.MESSAGE_WRITE);
		requiredPermissions.add(Permission.MESSAGE_EMBED_LINKS);
	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!selfHasPermissions()) {
			return;
		}
		EmbedBuilder eb = null;

		// If no parameters
		if (commandParams.length == 1) {
			eb = sendHelpOverview();
			// If asking for help with specific command
		} else if (commandParams.length == 2) {
			eb = sendCommandHelp();
		}
		if (eb != null) {
			sendEmbed(eb);
		}
	}

	private EmbedBuilder sendHelpOverview() {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Bot Help");
		eb.setDescription(
				"[click here to see the manual for help on commands and bot functions: ](https://github.com/kuroodo/SwagBot-2.0/blob/master/help.txt)");

		return eb;
	}

	private EmbedBuilder sendCommandHelp() {
		EmbedBuilder eb = new EmbedBuilder();
		String commandName = commandParams[1];
		Command command = CommandRegistry.getCommand(commandParams[1]);
		command.commandPrefix = commandPrefix;

		eb.setTitle(commandName);
		eb.setDescription(command.commandDescription());
		eb.addField("Usage", command.commandFormat(), false);

		return eb;
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
