package kuroodo.swagbot.command.console;

import kuroodo.swagbot.command.ConsoleCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

public class CM_Activity extends ConsoleCommand {
	public CM_Activity() {
		requiresJDA = true;
	}

	@Override
	public void executeCommand(String[] commandParams, JDA jda) {
		super.executeCommand(commandParams, jda);
		String activity = getParamsCongatenated(1);
		jda.getPresence().setActivity(Activity.listening(activity));
	}
}
