package kuroodo.swagbot.command.chatcommand;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandLaser extends ChatCommand {
	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!event.getMessage().getMentionedUsers().isEmpty()) {
			sendMessage(event.getAuthor().getAsMention() + " points a laser at "
					+ event.getMessage().getMentionedUsers().get(0).getAsMention() + "'s eye");
		} else {
			sendMessage("Please mention a valid user");
		}
	}

	@Override
	public String commandDescription() {
		return "Point a laser at someones eye\nUsage: " + commandPrefix + "laser @user";
	}
}