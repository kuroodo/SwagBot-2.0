package kuroodo.swagbot.command.chatcommand.fun;

import kuroodo.swagbot.command.chatcommand.ChatCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandSlap extends ChatCommand {
	@Override
	protected void setCommandPermissiosn() {
		requiredPermissions.add(Permission.MESSAGE_WRITE);
	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);
		if (!selfHasPermissions()) {
			return;
		}

		if (!event.getMessage().getMentionedUsers().isEmpty()) {
			sendMessage(event.getAuthor().getAsMention() + " slaps "
					+ event.getMessage().getMentionedUsers().get(0).getAsMention());
		} else {
			sendMessage("Please mention a valid user");
		}
	}

	@Override
	public String commandDescription() {
		return "Slap a user";
	}

	@Override
	public String commandFormat() {
		return commandPrefix + "slap @user";
	}
}
