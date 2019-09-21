package kuroodo.swagbot.command;

import java.util.ArrayList;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Command {
	protected ArrayList<Permission> requiredPermissions = new ArrayList<Permission>();

	/**
	 * Specify any permissions required for normal command function
	 */
	protected abstract void setCommandPermissiosn();

	/**
	 * Begin executing command function. Call this method to begin a commands
	 * execution
	 * 
	 * @param commandParams Parameters from user input required for command function
	 * @param event         The event that initiated the command
	 */
	public abstract void executeCommand(String[] commandParams, MessageReceivedEvent event);

	/**
	 * Returns a tooltip of what the command does
	 */
	public abstract String commandDescription();
	
	/**
	 * Returns a tooltip of the proper command format
	 */
	public abstract String commandFormat();
}
