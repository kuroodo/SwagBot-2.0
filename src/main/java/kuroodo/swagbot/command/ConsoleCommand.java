package kuroodo.swagbot.command;

public abstract class ConsoleCommand {
	protected String[] commandParams;

	public void executeCommand(String[] commandParams) {
		this.commandParams = commandParams;
	}

	protected String getParamsCongatenated(int startingIndex) {
		String reason = "";
		for (int i = startingIndex; i < commandParams.length; i++) {
			reason += commandParams[i] + " ";
		}

		return reason;
	}
}
