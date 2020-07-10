package kuroodo.swagbot.command.console;

import kuroodo.swagbot.command.ConsoleCommand;

public class CM_Error extends ConsoleCommand {
	@Override
	public void executeCommand(String[] commandParams) {
		super.executeCommand(commandParams);
		System.err.println("Error: Command " + commandParams[0] + " not found");
	}
}
