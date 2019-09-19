package kuroodo.swagbot.command;

import java.util.ArrayList;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Command {
	protected ArrayList<Permission> requiredPermissions = new ArrayList<Permission>();
	
	protected abstract void setCommandPermissiosn();
	
	public abstract void executeCommand(String[] commandParams, MessageReceivedEvent event);

	public abstract String commandDescription();
}
