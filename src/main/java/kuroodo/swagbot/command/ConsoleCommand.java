package kuroodo.swagbot.command;

import net.dv8tion.jda.api.JDA;

public abstract class ConsoleCommand {
	protected String[] commandParams;
	protected boolean requiresJDA = false;

	public void executeCommand(String[] commandParams) {
		this.commandParams = commandParams;
	}

	public void executeCommand(String[] commandParams, JDA jda) {
		this.commandParams = commandParams;
	}

	protected String getParamsCongatenated(int startingIndex) {
		String reason = "";
		for (int i = startingIndex; i < commandParams.length; i++) {
			reason += commandParams[i] + " ";
		}

		return reason;
	}

	public boolean isRequiresJDA() {
		return requiresJDA;
	}
}
