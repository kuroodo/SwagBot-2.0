package kuroodo.swagbot.command.chatcommand.fun;

import kuroodo.swagbot.command.chatcommand.ChatCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandPoke extends ChatCommand {
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
		Member member = findParamsMember();
		if (member == null) {
			sendMessage("Please mention a valid user");
			return;
		}

		sendMessage(event.getAuthor().getAsMention() + " pokes " + member.getAsMention());
	}

	@Override
	public String commandDescription() {
		return "Poke a user";
	}

	@Override
	public String commandFormat() {
		return commandPrefix + "poke @user";
	}
}
