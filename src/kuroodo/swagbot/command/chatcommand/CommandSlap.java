package kuroodo.swagbot.command.chatcommand;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandSlap extends ChatCommand {
	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!event.getMessage().getMentionedUsers().isEmpty()) {
			sendMessage(event.getAuthor().getAsMention() + " slaps "
					+ event.getMessage().getMentionedUsers().get(0).getAsMention());
		} else {
			sendMessage("Please mention a valid user");
		}
	}

	@Override
	public String commandDescription() {
		return "Slap a user\nUsage: " + commandPrefix + "slap @user";
	}
}