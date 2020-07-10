package kuroodo.swagbot.command.console;

import kuroodo.swagbot.SwagBot;
import kuroodo.swagbot.command.ConsoleCommand;
import net.dv8tion.jda.api.entities.Activity;

public class CM_Activity extends ConsoleCommand {
	@Override
	public void executeCommand(String[] commandParams) {
		super.executeCommand(commandParams);
		String activity = getParamsCongatenated(1);
		SwagBot.getJDA().getPresence().setActivity(Activity.listening(activity));
	}
}
