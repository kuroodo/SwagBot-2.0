package kuroodo.swagbot.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Command {
	public abstract void executeCommand(String[] commandParams, MessageReceivedEvent event);

	public abstract String commandDescription();
}
