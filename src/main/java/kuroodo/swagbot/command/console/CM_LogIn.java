package kuroodo.swagbot.command.console;

import kuroodo.swagbot.Init;
import kuroodo.swagbot.SwagBot;
import kuroodo.swagbot.command.ConsoleCommand;
import net.dv8tion.jda.api.JDA.Status;

public class CM_LogIn extends ConsoleCommand {
	@Override
	public void executeCommand(String[] commandParams) {
		super.executeCommand(commandParams);
		if (SwagBot.getJDA().getStatus() == Status.SHUTDOWN) {
			Init.initializeBot();
		} else {
			System.out.println("Error logging in: JDA still logged in or not finished shutting down");
		}

	}
}
