/*
 * Copyright 2019 Leandro Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package kuroodo.swagbot.command;

import java.util.ArrayList;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Command {
	protected ArrayList<Permission> requiredPermissions = new ArrayList<Permission>();
	public String commandPrefix = "";
	
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
	
	public abstract String commandUsageExample();
}
