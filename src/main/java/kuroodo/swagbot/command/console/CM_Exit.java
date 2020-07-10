package kuroodo.swagbot.command.console;

import kuroodo.swagbot.Init;
import kuroodo.swagbot.command.ConsoleCommand;

public class CM_Exit extends ConsoleCommand {
	@Override
	public void executeCommand(String[] commandParams) {
		Init.exitApplication(0);
	}
}
