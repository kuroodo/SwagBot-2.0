package kuroodo.swagbot.command.console;

import kuroodo.swagbot.SwagBot;
import kuroodo.swagbot.command.ConsoleCommand;

public class CM_ServerCount extends ConsoleCommand {
	@Override
	public void executeCommand(String[] commandParams) {
		super.executeCommand(commandParams);
		System.out.println(SwagBot.getJDA().getGuilds().size());
	}
}
