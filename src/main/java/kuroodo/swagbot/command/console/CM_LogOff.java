package kuroodo.swagbot.command.console;

import kuroodo.swagbot.SwagBot;
import kuroodo.swagbot.command.ConsoleCommand;

public class CM_LogOff extends ConsoleCommand {
	@Override
	public void executeCommand(String[] commandParams) {
		super.executeCommand(commandParams);
		SwagBot.getJDA().shutdown();
		System.out.println("Succesfully logged off, using login to log back in");
	}
}
